/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.controller;

import eu.europa.ec.etrustex.admin.old.web.dto.CertificateWrapper;
import eu.europa.ec.etrustex.admin.old.web.dto.CertificatesForm;
import eu.europa.ec.etrustex.admin.old.web.dto.SessionUserInformation;
import eu.europa.ec.etrustex.admin.old.web.security.ActiveUser;
import eu.europa.ec.etrustex.admin.old.web.utils.AjaxResult;
import eu.europa.ec.etrustex.admin.old.web.utils.CertificateUsage;
import eu.europa.ec.etrustex.admin.old.web.validators.CertificateValidator;
import eu.europa.ec.etrustex.domain.Certificate;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.services.IPartyService;
import org.apache.commons.collections.CollectionUtils;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * @author batrian
 */
@Controller
@RequestMapping(value = "/certificate")
@SessionAttributes({"certificatesForm", "certificateEditForm"})
@PreAuthorize("hasAnyRole('ADM', 'CBO')")
public class CertificateController {
    private static final String FRAG_CERTIFICATES = "page.certificates.inner";
    private static final String FRAG_CERTIFICATE_EDIT = "page.certificates.edit";
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IPartyService partyService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private CertificateValidator certificateValidator;


    /*-----------------------------------------------------------------*/
    /*---------------------------- Binding ----------------------------*/
    /*-----------------------------------------------------------------*/
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        //messageSource.getMessage("date.format", null, LocaleContextHolder.getLocale()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @InitBinder("certificatesForm")
    public void initCertificateFormBinder(WebDataBinder binder) {
        binder.setValidator(certificateValidator);
    }


    /*-----------------------------------------------------------------*/
    /*------------------------ Model Attributes -----------------------*/
    /*-----------------------------------------------------------------*/
    @ModelAttribute("certificatesForm")
    public CertificatesForm certificatesForm() {
        return new CertificatesForm();
    }

    @ModelAttribute("certificateEditForm")
    public Certificate certificateEditForm() {
        return new Certificate();
    }


    /*-----------------------------------------------------------------*/
    /*-------------------- Handler methods ----------------------------*/
    /*-----------------------------------------------------------------*/
    @RequestMapping(method = RequestMethod.POST)
    public String load(@ModelAttribute("certificatesForm") CertificatesForm certificatesForm, Model model) {
        populateCertificatesForm(model, certificatesForm);
        model.addAttribute("manageCertsMsg", certificatesForm.getResultMsg());

        return FRAG_CERTIFICATES;
    }

    /*
     * Opens the "Manage Party Certificates" popup dialog.
     * If it is edit mode and first time dialog is opened (partyId != null&& certificatesForm.getParty() != null),
     * populates certificatesForm with the party's values
     * and map of party's certificates giving an index as map key.
     * Otherwise populates certificatesForm with default values and empty map of certificates.
     */
    @RequestMapping(value = "/manage", method = RequestMethod.POST)
    public String manage(@ModelAttribute("certificatesForm") CertificatesForm certificatesForm,
                         @RequestParam(value = "partyId", required = false) Long partyId, Model model) {
        if (partyId != null && certificatesForm.getParty() == null) {
            Party party = partyService.getParty(partyId);
            certificatesForm.setParty(party);
            if (CollectionUtils.isNotEmpty(party.getCertificates())) {
                for (Certificate c : party.getCertificates()) {
                    if (!certificatesForm.contains(c)) {
                        certificatesForm.getCertificates().add(new CertificateWrapper(c));
                    }
                }
            }
        }


        populateCertificatesForm(model, certificatesForm);

        return FRAG_CERTIFICATES;
    }

    /*
     * Called when the user's click on "Import" button of "Manage Party Certificates" popup.
     * Extracts the certificate file data to create a CertificateWrapper and adds it to the list of certificates
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public String importCertificate(@RequestParam("file") MultipartFile file,
                                    @ModelAttribute("certificatesForm") CertificatesForm certificatesForm, @ActiveUser SessionUserInformation userInfo,
                                    Model model) {
        String result = "success";

        /*
         * UC10_BR49 A Party can have more than one certificate, as certificates have different usages.
         * However only one active certificate per usage is allowed at a given time.
         */
        if (certificatesForm.getNewCertificate().getCertificate().getIsActive()) {
            String usage = certificatesForm.getNewCertificate().getCertificate().getUsage();

            for (CertificateWrapper certificateWrapper : certificatesForm.getCertificates()) {
                if (certificateWrapper.getCertificate().getUsage().equals(usage) && certificateWrapper.getCertificate().getIsActive()) {
                    result = messageSource.getMessage("error.certificate.isActive.usage", null, LocaleContextHolder.getLocale());
                    break;
                }
            }
        }

        if (result.equals("success")) {
            if (file == null || file.getSize() == 0) {
                result = messageSource.getMessage("error.certificate.file.empty", null, LocaleContextHolder.getLocale());
            } else if (file.getSize() > 3072) {
                result = messageSource.getMessage("error.certificate.file.size", null, LocaleContextHolder.getLocale());
            } else {
                try {
                    // Extract certificate data
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    X509Certificate certificateFile = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(file.getBytes()));

                    // Populate certificate instance
                    Certificate certificate = new Certificate();
                    certificate.setSerialNumber(hexify(certificateFile.getSerialNumber().toByteArray()));
                    certificate.setValidityStartDate(certificateFile.getNotBefore());
                    certificate.setValidityEndDate(certificateFile.getNotAfter());
                    certificate.setIssuer(certificateFile.getIssuerX500Principal().getName());
                    certificate.setEncodedData(new String(Base64.encode(certificateFile.getEncoded())));
                    certificate.setHolder(certificateFile.getSubjectX500Principal().getName());
                    certificate.setSignatureAlgorithm(certificateFile.getSigAlgName());
                    certificate.setType(certificateFile.getType());
                    certificate.setVersion("" + certificateFile.getVersion());
                    certificate.setSignatureValue(getThumbPrint(certificateFile));

                    certificate.setIsActive(certificatesForm.getNewCertificate().getCertificate().getIsActive());
                    certificate.setIsRevoked(certificatesForm.getNewCertificate().getCertificate().getIsRevoked());
                    certificate.setUsage(certificatesForm.getNewCertificate().getCertificate().getUsage());

                    EntityAccessInfo eai = new EntityAccessInfo();
                    eai.setCreationId(userInfo.getUsername());
                    certificate.setAccessInfo(eai);

                    // Add certificate to the list
                    certificatesForm.getCertificates().add(new CertificateWrapper(certificate));

                } catch (CertificateException e) {
                    result = messageSource.getMessage("error.certificate.import", null, LocaleContextHolder.getLocale());
                    log.error(e.getMessage(), e);
                } catch (IOException | NoSuchAlgorithmException e) {
                    result = messageSource.getMessage("error.certificate.read", null, LocaleContextHolder.getLocale());
                    log.debug(e.getMessage(), e);
                }
            }
        }

        certificatesForm.setResultMsg(result);

        populateCertificatesForm(model, certificatesForm);

//    	return FRAG_CERTIFICATES;
        return result; // Whatever. Return value doesn't matter
    }

    /*
     * Called when the user's click on "Add" button of "Manage Party Certificates" popup.
     * Validates certificates and marks them as to be added to the party.
     *
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addToParty(@ModelAttribute("certificatesForm") @Valid CertificatesForm certificatesForm, BindingResult result,
                                 @ActiveUser SessionUserInformation userInfo, HttpServletRequest request) {

        Long partyId = certificatesForm.getParty() != null && certificatesForm.getParty().getId() != null ? certificatesForm.getParty().getId() : null;

        if (result.hasErrors()) {
            log.debug("Binding errors");
            StringBuilder message = new StringBuilder("There are validation errors: <br/><ul>");
            for (ObjectError e : result.getAllErrors()) {
                String resolvedMsg = messageSource.getMessage(e.getCode(), e.getArguments(), LocaleContextHolder.getLocale());
                log.debug(e.getObjectName() + " - " + resolvedMsg + " - " + e.toString());
                message.append("<li type='disc'>").append(resolvedMsg).append("</li>");
            }
            message.append("</ul>");
            return new AjaxResult(false, messageSource.getMessage("error.certificates.add", new Object[]{message.toString()}, LocaleContextHolder.getLocale()), partyId);
        }

        for (CertificateWrapper certificateWrapper : certificatesForm.getCertificates()) {
            certificateWrapper.setAddToParty(true);
        }

        return new AjaxResult(true, messageSource.getMessage("certificates.success.add", null, LocaleContextHolder.getLocale()), partyId);
    }

    /*
     * Called when the user's click on "Cancel" button of "Manage Party Certificates" popup.
     * Removes all the certificates that have not been yet marked as to be added to the party
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult cancel(@ModelAttribute("certificatesForm") CertificatesForm certificatesForm) {

        for (Iterator<CertificateWrapper> iterator = certificatesForm.getCertificates().iterator(); iterator.hasNext(); ) {
            CertificateWrapper certificateWrapper = iterator.next();
            if (!certificateWrapper.getAddToParty()) {
                iterator.remove();
            }
        }

        return new AjaxResult(true, "cancel", null);
    }

    /*
     * Called when the user's click on "Edit" icon of a record in the list of certificates of "Manage Party Certificates" popup.
     * Adds certificateEditForm model attribute to popuplate the "Certificate Modification Popup"
     * Adds the certificateIndex model attribute to keep record of which certificate is modified.
     */
    @RequestMapping(value = "/{index}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable("index") int index,
                       @ModelAttribute("certificatesForm") CertificatesForm certificatesForm, Model model) {

        model.addAttribute("certificateEditForm", certificatesForm.getCertificates().get(index).getCertificate());
        model.addAttribute("certificateUsages", CertificateUsage.values());
        model.addAttribute("certificateIndex", index);

        return FRAG_CERTIFICATE_EDIT;
    }

    /*
     * Called when the user's click on "Save" button of "Certificate Modification Popup" popup.
     * Replaces the modified certificate with the corresponding item of the list of certificates
     */
    @RequestMapping(value = "/edit/{index}/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult editSave(@PathVariable("index") int index, @ModelAttribute("certificateEditForm") Certificate certificate, Model model, HttpSession session) {
        // TODO VALIDATE

        CertificatesForm certificatesForm = (CertificatesForm) session.getAttribute("certificatesForm");
        certificatesForm.getCertificates().set(index, new CertificateWrapper(certificate));

        populateCertificatesForm(model, certificatesForm);

        return new AjaxResult(true, messageSource.getMessage("certificate.success.save", new Object[]{certificate.getSerialNumber()}, LocaleContextHolder.getLocale()), null);
    }

    /*
     * Called when the user's click on "Delete" icon of a record in the list of certificates of "Manage Party Certificates" popup.
     * Removes the certificate with the corresponding item of the list of certificates
     */
    @RequestMapping(value = "/{index}/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(@PathVariable("index") int index,
                             @ModelAttribute("certificatesForm") CertificatesForm certificatesForm, Model model) {
        //TODO: add here specific validation rules before deletion

        certificatesForm.getCertificates().remove(index);

        return new AjaxResult(true, messageSource.getMessage("common.success.delete", null, LocaleContextHolder.getLocale()), null);
    }


    private void populateCertificatesForm(Model model, CertificatesForm certificatesForm) {
        Certificate certificate = new Certificate();
        certificate.setIsActive(Boolean.TRUE);
        certificate.setIsRevoked(Boolean.FALSE);
        certificate.setUsage(CertificateUsage.KEY_ENCIPHERMENT.toString());
        certificatesForm.setNewCertificate(new CertificateWrapper(certificate));
        model.addAttribute("certificateUsages", CertificateUsage.values());
        model.addAttribute("certificatesForm", certificatesForm);
    }
	
/*	private void populateCertificateEditForm(Model model, CertificatesForm certificatesForm, Certificate certificate) {
		certificatesForm.setNewCertificate(new CertificateWrapper(certificate));
    	model.addAttribute("certificateUsages", CertificateUsage.values());
		model.addAttribute("certificatesForm", certificatesForm);
	}*/

    private String getThumbPrint(X509Certificate cert) throws NoSuchAlgorithmException, CertificateEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] der = cert.getEncoded();
        md.update(der);
        byte[] digest = md.digest();
        return hexify(digest);

    }

    private String hexify(byte bytes[]) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        StringBuffer buf = new StringBuffer(bytes.length * 2);

        for (int i = 0; i < bytes.length; ++i) {
            buf.append(hexDigits[(bytes[i] & 0xf0) >> 4]);
            buf.append(hexDigits[bytes[i] & 0x0f]);
            buf.append(" ");
        }

        return buf.toString();
    }
}
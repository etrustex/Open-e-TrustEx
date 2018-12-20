/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.validators;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import eu.europa.ec.etrustex.admin.old.web.dto.PartyAgreementForm;
import eu.europa.ec.etrustex.admin.old.web.dto.SessionUserInformation;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyAgreement;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.services.IMessageService;
import eu.europa.ec.etrustex.services.IPartyAgreementService;

@Component
public class PartyAgreementValidator implements Validator {
    @Autowired
    private IPartyAgreementService partyAgreementService;
    @Autowired
    private IMessageService messageService;

    @Override
    public boolean supports(Class<?> clazz) {
        return PartyAgreementForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SessionUserInformation userInfo = (SessionUserInformation) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PartyAgreementForm form = (PartyAgreementForm) target;
        Party authorizingParty = form.getAuthorizingParty();
        Party delegateParty = form.getDelegateParty();

        // UC160_BR03	Authorizing party is mandatory.
        if (authorizingParty == null || authorizingParty.getId() == null) {
            errors.rejectValue("authorizingParty", "error.partyAgreement.authorizingParty.mandatory");
        }

        // UC160_BR04	Delegated party is mandatory.
        if (delegateParty == null || delegateParty.getId() == null) {
            errors.rejectValue("delegateParty", "error.partyAgreement.delegateParty.mandatory");
        }

        if (errors.hasErrors()) {
            return;
        }

        // UC160_BR06	If Authorized on all transactions is set to No, at least one transaction needs to be specified.
        if (!form.getAllTransactions() && CollectionUtils.isEmpty(form.getTransactions())) {
            errors.rejectValue("allTransactions", "error.partyAgreement.allTransactions.no");
        }

        validParties(authorizingParty, delegateParty, userInfo.getBusinessDomain().getId(), errors);

        // update rules
        if (form.getId() != null) {
            PartyAgreement existingPA = partyAgreementService.findPartyAgreementById(form.getId());

            if (existingMessagesForParties(authorizingParty, delegateParty)) {
                // UC160_BR11 Authorized transactions cannot be deleted if there are messages in the system having the authorized party as sender and the delegated party as issuer.
                if (!form.getAllTransactions() && CollectionUtils.isEmpty(existingPA.getTransactions())) {
                    errors.rejectValue("transactions", "error.partyAgreement.transactions.inUse");
                } else {
                    Set<Transaction> existingTransactions = existingPA.getTransactions();
                    List<Transaction> newTransactions = form.getTransactions();
                    for (Transaction tx : existingTransactions) {
                        if (!newTransactions.contains(tx)) {
                            errors.rejectValue("transactions", "error.partyAgreement.transactions.inUse");
                            break;
                        }
                    }
                }
            }

            if (!existingPA.getDelegateParty().equals(delegateParty) || !existingPA.getAuthorizingParty().equals(authorizingParty)) {
                // If any of the parties has been changed, check for UC160_BR16
                validateExisting(authorizingParty, delegateParty, errors);

                // UC160_BR10	Delegated party cannot be changed if there are messages in the system having the authorized party as sender and the delegated party as issuer.
                if (!existingPA.getDelegateParty().equals(delegateParty)) {
                    errors.rejectValue("delegateParty", "error.partyAgreement.delegateParty.inUse");
                }
            }

        } else {
            validateExisting(authorizingParty, delegateParty, errors);
        }

    }

    private void validateExisting(Party authorizingParty, Party delegateParty, Errors errors) {
        // UC160_BR16 No other party agreement must already exist between the authorizing party and the delegated party.
        if (partyAgreementService.existsPartyAgreement(authorizingParty, delegateParty)) {
            errors.rejectValue("delegateParty", "error.partyAgreement.exists");
        }
    }

    private void validParties(Party authorizingParty, Party delegateParty, Long businessDomainId, Errors errors) {
        if (authorizingParty != null && authorizingParty.getId() != null && delegateParty != null && delegateParty.getId() != null) {
            // UC160_BR05	Authorizing and Delegated parties must pertain to the business domain selected in the application header.
            if (!authorizingParty.getBusinessDomain().getId().equals(businessDomainId) || !delegateParty.getBusinessDomain().getId().equals(businessDomainId)) {
                errors.rejectValue("delegateParty", "error.partyAgreement.parties.businessDomain");
            }

            // UC160_BR14	Authorizing party must not be a third party.
            if (authorizingParty.getThirdPartyFlag()) {
                errors.rejectValue("authorizingParty", "error.partyAgreement.authorizing.thirdPartyFlag");
            }

            // UC160_BR15	Delegated party must be a third party.
            if (!delegateParty.getThirdPartyFlag()) {
                errors.rejectValue("delegateParty", "error.partyAgreement.delegate.thirdPartyFlag");
            }
        }
    }

    public boolean existingMessagesForParties(Party authorizingParty, Party delegateParty) {
        Message messageExample = new Message();
        messageExample.setAccessInfo(new EntityAccessInfo());
        messageExample.setSender(authorizingParty);
        messageExample.setIssuer(delegateParty);

        return messageService.countMessagesByCriteria(messageExample, null, null, null, null) > 0;
    }
}
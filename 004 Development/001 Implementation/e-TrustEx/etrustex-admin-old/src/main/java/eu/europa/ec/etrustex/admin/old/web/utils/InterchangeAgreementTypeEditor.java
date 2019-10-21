/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.utils;

import eu.europa.ec.etrustex.dao.IProfileDAO;
import eu.europa.ec.etrustex.domain.Profile;

import java.beans.PropertyEditorSupport;

/**
 * @author batrian
 */
public class InterchangeAgreementTypeEditor extends PropertyEditorSupport {

    private IProfileDAO profileDAO;

    public InterchangeAgreementTypeEditor(IProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Profile profile = profileDAO.read(Long.parseLong(text));
        setValue(profile);
    }

}

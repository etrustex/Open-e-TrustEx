/**
 * 
 */
package eu.europa.ec.cipa.admin.web.utils;

import java.beans.PropertyEditorSupport;

import eu.europa.ec.cipa.etrustex.domain.Profile;
import eu.europa.ec.cipa.etrustex.services.dao.IProfileDAO;

/**
 * @author batrian
 *
 */
public class InterchangeAgreementTypeEditor extends PropertyEditorSupport {
	
	private IProfileDAO profileDAO;
	
	public InterchangeAgreementTypeEditor(IProfileDAO profileDAO){
		this.profileDAO = profileDAO;
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException{
			Profile profile = profileDAO.read(Long.parseLong(text));
			setValue(profile);
	}

}

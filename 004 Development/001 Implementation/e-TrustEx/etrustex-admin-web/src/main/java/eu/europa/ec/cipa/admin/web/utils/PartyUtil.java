/**
 * 
 */
package eu.europa.ec.cipa.admin.web.utils;

import eu.europa.ec.cipa.admin.web.dto.SessionUserInformation;
import eu.europa.ec.cipa.etrustex.domain.admin.User;
import eu.europa.ec.cipa.etrustex.domain.admin.UserAccessRights;
import eu.europa.ec.cipa.etrustex.services.dto.PartyListItemDTO;
import org.apache.commons.lang.StringUtils;

/**
 * @author batrian
 *
 */
public class PartyUtil {

    /*
     *  Acting on behalf: We look at the creation user of the concerned party, then we search the corresponding user access rights record
     *  and see to which parties he/she is associated with for the business domain (as a LBO of course, otherwise no party is associated to him/her).
     *  Then we just need to check that the current user party is in that list.
     */
	public static boolean isCreatedByLBOOnBehalf(User creationUser, SessionUserInformation userInfo, Long businessDomainId) {
	    if (creationUser != null) {
            for (UserAccessRights uar : creationUser.getAccessRights()) {
                if (StringUtils.equalsIgnoreCase(uar.getRole().getCode(), UserRoleEnum.LBO.name())
                        && uar.getBusinessDomain().getId().equals(businessDomainId)) {
                    for (PartyListItemDTO pe : userInfo.getParties()) {
                        if (pe.getId().equals(uar.getParty().getId())) {
                            return true;
                        }
                    }
                }
            }
        }

		return false;
	}
}

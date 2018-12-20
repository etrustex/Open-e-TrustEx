/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.utils;

import eu.europa.ec.etrustex.admin.old.web.dto.BusinessDomainElement;
import eu.europa.ec.etrustex.admin.old.web.dto.ProfileElement;
import eu.europa.ec.etrustex.admin.old.web.dto.SessionUserInformation;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Role;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.services.IBusinessDomainService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.EqualPredicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BusinessDomainUtil {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private IBusinessDomainService businessDomainService;

    public List<BusinessDomainElement> domainToElement(List<BusinessDomain> domainObjList) {

        List<BusinessDomainElement> bdElList = new ArrayList<BusinessDomainElement>();

        if (CollectionUtils.isNotEmpty(domainObjList)) {
            for (BusinessDomain domainObject : domainObjList) {
                BusinessDomainElement bdEl = new BusinessDomainElement();
                bdEl.setId(domainObject.getId());
                bdEl.setName(domainObject.getName());
                bdElList.add(bdEl);
            }
        }

        Collections.sort(bdElList, new Comparator<BusinessDomainElement>() {
            @Override
            public int compare(BusinessDomainElement o1, BusinessDomainElement o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });

        return bdElList;
    }

    public List<ProfileElement> getProfilesElementsForUser(Set<Profile> profiles, SessionUserInformation userInfo) {

        List<ProfileElement> profilesForView = new ArrayList<ProfileElement>();


        for (Profile profile : profiles) {
            List<Role> profileRoles = new ArrayList<Role>();
            Set<Transaction> transactions = profile.getTransactions();
            for (Transaction transaction : transactions) {
                Role role = transaction.getReceiverRole();
                if (role != null
                        && CollectionUtils.countMatches(profileRoles, new EqualPredicate(role)) == 0) {
                    if (StringUtils.equalsIgnoreCase(userInfo.getRole().getCode(), UserRoleEnum.ADM.name())) {
                        profileRoles.add(role);
                    } else {
                        if (!role.getTechnicalFlag()) {
                            profileRoles.add(role);
                        }
                    }
                }
                role = transaction.getSenderRole();
                if (role != null
                        && CollectionUtils.countMatches(profileRoles, new EqualPredicate(role)) == 0) {
                    if (StringUtils.equalsIgnoreCase(userInfo.getRole().getCode(), UserRoleEnum.ADM.name())) {
                        profileRoles.add(role);
                    } else {
                        if (!role.getTechnicalFlag()) {
                            profileRoles.add(role);
                        }
                    }
                }
            }

            profilesForView.add(new ProfileElement(profile.getId(), profile.getName(), profileRoles));
        }

        Collections.sort(profilesForView, new Comparator<ProfileElement>() {
            @Override
            public int compare(ProfileElement o1, ProfileElement o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });

        return profilesForView;
    }

    public List<Profile> getAllProfilesForUser(Set<Profile> profiles, String userRoleCode, String profileNoneMsg) {
        List<Profile> profilesForView = new ArrayList<Profile>();

        for (Profile profile : profiles) {
            List<Role> profileRoles = new ArrayList<Role>();
            Set<Transaction> transactions = profile.getTransactions();
            for (Transaction transaction : transactions) {
                Role role = transaction.getReceiverRole();
                if (role != null && CollectionUtils.countMatches(profileRoles, new EqualPredicate(role)) == 0) {
                    if (StringUtils.equalsIgnoreCase(userRoleCode, UserRoleEnum.ADM.name())) {
                        profileRoles.add(role);
                    } else {
                        if (!role.getTechnicalFlag()) {
                            profileRoles.add(role);
                        }
                    }
                }
                role = transaction.getSenderRole();
                if (role != null && CollectionUtils.countMatches(profileRoles, new EqualPredicate(role)) == 0) {
                    if (StringUtils.equalsIgnoreCase(userRoleCode, UserRoleEnum.ADM.name())) {
                        profileRoles.add(role);
                    } else {
                        if (!role.getTechnicalFlag()) {
                            profileRoles.add(role);
                        }
                    }
                }
            }

            profilesForView.add(profile);
        }

        Collections.sort(profilesForView, new Comparator<Profile>() {
            @Override
            public int compare(Profile o1, Profile o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });

        Profile none = new Profile();
        none.setId(Long.valueOf(-1));
        none.setName(profileNoneMsg);
        profilesForView.add(0, none);

        return profilesForView;
    }

    public BusinessDomain getTransientBD(Long id, boolean noneOption) {
        BusinessDomain bd = new BusinessDomain();
        bd.setId(id);
        bd.setName(messageSource.getMessage(!noneOption ? "choose.please" : "common.none", null, LocaleContextHolder.getLocale()));

        return bd;
    }

    public List<BusinessDomain> getUserBusinessDomains(boolean includeEmptyOption) {
        SessionUserInformation userInfo = (SessionUserInformation) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<BusinessDomain> businessDomains = new ArrayList<>();
        for (BusinessDomainElement bde : userInfo.getBusinessDomains()) {
            businessDomains.add(businessDomainService.getBusinessDomain(bde.getId()));
        }
        businessDomains.add(0, getTransientBD(Long.valueOf("-1"), false));

        return businessDomains;
    }
}

/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.dto;

import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;

import java.io.Serializable;

/**
 * @author batrian
 */
public class PartyIdentifierDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private IdentifierIssuingAgency type;
    private String value;

//	private EntityAccessInfo eai;

    private Boolean isMarkedForDeletion;

    public PartyIdentifierDTO() {
    }

    public PartyIdentifierDTO(PartyIdentifier p) {
        super();

        if (p != null) {
//			this.eai = p.getAccessInfo();

            this.id = p.getId();
            this.type = p.getSchemeId();
            this.value = p.getValue();

            this.isMarkedForDeletion = false;
        }
    }

    public PartyIdentifier toDomain() {
        PartyIdentifier pi = new PartyIdentifier();
		/*if (this.eai == null){
			pi.setAccessInfo(new EntityAccessInfo());
		} else {
			pi.setAccessInfo(this.eai);
		}*/
        // pi.setSchemeId(IdentifierIssuingAgency.valueOf(this.type));
        pi.setId(this.id);
        pi.setSchemeId(this.type);
        pi.setValue(this.value);
        return pi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IdentifierIssuingAgency getType() {
        return type;
    }

    public void setType(IdentifierIssuingAgency type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

//	public EntityAccessInfo getEai() {
//		return eai;
//	}
//
//	public void setEai(EntityAccessInfo eai) {
//		this.eai = eai;
//	}

    public Boolean getIsMarkedForDeletion() {
        return isMarkedForDeletion;
    }

    public void setIsMarkedForDeletion(Boolean isMarkedForDeletion) {
        this.isMarkedForDeletion = isMarkedForDeletion;
    }

}

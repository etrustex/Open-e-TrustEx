/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.dto;

import eu.europa.ec.etrustex.domain.admin.BusinessDomain;

import java.io.Serializable;

/**
 * @author batrian
 */
public class BusinessDomainElement implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

    public BusinessDomainElement() {
        super();
    }

    public BusinessDomainElement(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public BusinessDomainElement(BusinessDomain bd) {
        super();
        this.id = bd.getId();
        this.name = bd.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusinessDomainElement that = (BusinessDomainElement) o;

        if (!id.equals(that.id)) return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}

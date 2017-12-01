/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dto;

import java.io.Serializable;

/**
 * @author guerrpa
 *
 * To be used for lists of parties where not all properties need to be populated,
 * e.g. in UI select dropdowns
 */
public class PartyListItemDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
    private String name;

    public PartyListItemDTO(Long id, String name) {
        this.id = id;
        this.name = name;
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
}

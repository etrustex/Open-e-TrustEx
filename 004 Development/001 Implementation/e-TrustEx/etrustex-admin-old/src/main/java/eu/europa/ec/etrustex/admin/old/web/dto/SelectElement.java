/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.dto;

import java.io.Serializable;

/**
 * @author batrian
 */
public class SelectElement implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

    public SelectElement() {
    }

    public SelectElement(Long id, String name) {
        super();
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

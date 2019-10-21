/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.dto;

import java.io.Serializable;

public class CredentialsForm implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String password;
    private String password2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

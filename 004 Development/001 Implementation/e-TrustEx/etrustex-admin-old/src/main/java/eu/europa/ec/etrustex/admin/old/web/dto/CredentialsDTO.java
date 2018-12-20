/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.dto;

import eu.europa.ec.etrustex.domain.*;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;

import java.io.Serializable;

/**
 * @author batrian
 */
public class CredentialsDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String user;
    private String password;
    private String password2;
    private Boolean isSignatureRequired;
    private Boolean isPasswordEncrypted;
    private Boolean isSalted;

    public CredentialsDTO() {
    }

    public CredentialsDTO(Credentials c) {
        super();

        if (c != null) {
            this.id = c.getId();
            this.user = c.getUser();
            this.password = c.getPassword();
            this.password2 = c.getPassword();
            this.isSignatureRequired = c.getSignatureRequired();
            this.isPasswordEncrypted = c.getPasswordEncrypted();
            this.setSalted(c.getSalt());
        }
    }

    public Credentials toDomain(CredentialsType credentialsType) {
        Credentials c;
        switch (credentialsType) {
            case PROXY:
                c = new ProxyCredentials();
                break;
            case JMS:
                c = new JMSCredentials();
                break;
            case WS:
                c = new WSCredentials();
                break;
            case AMQP:
                c = new AMQPCredentials();
                break;
            default:
                c = new PartyCredentials();
        }

        c.setAccessInfo(new EntityAccessInfo());
        c.setId(this.id);
        c.setUser(this.user);
        c.setPassword(this.password);
        c.setSignatureRequired(this.isSignatureRequired);
        c.setPasswordEncrypted(this.isPasswordEncrypted);
        c.setSalt(this.getSalted());
        return c;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public Boolean getIsSignatureRequired() {
        return isSignatureRequired;
    }

    public void setIsSignatureRequired(Boolean isSignatureRequired) {
        this.isSignatureRequired = isSignatureRequired;
    }

    public Boolean getIsPasswordEncrypted() {
        return isPasswordEncrypted;
    }

    public void setIsPasswordEncrypted(Boolean isPasswordEncrypted) {
        this.isPasswordEncrypted = isPasswordEncrypted;
    }

    public Boolean getSalted() {
        return isSalted;
    }

    public void setSalted(Boolean salted) {
        isSalted = salted;
    }

    public enum CredentialsType {
        PARTY, PROXY, JMS, WS, AMQP
    }

}

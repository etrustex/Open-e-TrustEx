package eu.europa.ec.cipa.etrustex.domain;

import javax.persistence.*;

@Entity
@DiscriminatorValue("WS")
public class WSCredentials extends Credentials {
}

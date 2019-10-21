package eu.europa.ec.etrustex.domain;

import javax.persistence.*;

@Entity
@DiscriminatorValue("WS")
public class WSCredentials extends Credentials {
}

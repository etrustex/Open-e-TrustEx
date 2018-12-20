package eu.europa.ec.etrustex.domain;

import javax.persistence.*;

@Entity
@DiscriminatorValue("PROXY")
public class ProxyCredentials extends Credentials {
}

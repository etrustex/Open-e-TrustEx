package eu.europa.ec.cipa.etrustex.domain;

import javax.persistence.*;

@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "CRED_TYPE", discriminatorType = DiscriminatorType.STRING)
//@Table(name="ETR_TB_CREDENTIALS")
@DiscriminatorValue("PROXY")
public class ProxyCredentials extends Credentials {
}

package eu.europa.ec.etrustex.domain;

import javax.persistence.*;

@Entity
@DiscriminatorValue("JMS")
public class JMSCredentials extends Credentials {
}

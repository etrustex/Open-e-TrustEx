package eu.europa.ec.cipa.etrustex.domain;

import javax.persistence.*;

@Entity
@DiscriminatorValue("AMQP")
public class AMQPCredentials extends Credentials {
}

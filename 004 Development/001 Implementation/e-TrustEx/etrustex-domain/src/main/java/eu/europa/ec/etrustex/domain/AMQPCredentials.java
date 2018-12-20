package eu.europa.ec.etrustex.domain;

import javax.persistence.*;

@Entity
@DiscriminatorValue("AMQP")
public class AMQPCredentials extends Credentials {
}

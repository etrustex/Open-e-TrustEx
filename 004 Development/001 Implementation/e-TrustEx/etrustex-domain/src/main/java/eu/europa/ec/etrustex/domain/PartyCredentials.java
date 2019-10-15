package eu.europa.ec.etrustex.domain;

import javax.persistence.*;

@Entity
@DiscriminatorValue("PARTY")
public class PartyCredentials extends Credentials {
}

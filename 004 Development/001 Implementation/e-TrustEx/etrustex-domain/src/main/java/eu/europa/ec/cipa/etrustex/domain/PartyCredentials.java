package eu.europa.ec.cipa.etrustex.domain;

import javax.persistence.*;

@Entity
@DiscriminatorValue("PARTY")
public class PartyCredentials extends Credentials {
}

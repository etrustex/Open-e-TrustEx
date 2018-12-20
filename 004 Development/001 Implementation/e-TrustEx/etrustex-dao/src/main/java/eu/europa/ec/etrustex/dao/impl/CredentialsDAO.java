package eu.europa.ec.etrustex.dao.impl;

import eu.europa.ec.etrustex.dao.ICredentialsDAO;
import eu.europa.ec.etrustex.domain.Credentials;
import eu.europa.ec.etrustex.domain.PartyCredentials;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CredentialsDAO extends TrustExDAO<Credentials, Long> implements ICredentialsDAO {


    @Override
	public Boolean isSignatureRequired(String authenticatedUser) {
		Credentials result = entityManager
		.createQuery(
				"from Credentials cred where type(cred) =:type and cred.user =:authenticatedUser",
				Credentials.class).setParameter("type", PartyCredentials.class).
				setParameter("authenticatedUser", authenticatedUser).getSingleResult();
		return result.getSignatureRequired();
	}

	@Override
	public Boolean exists(Credentials credentials) {
        long count = entityManager
                .createQuery("SELECT count(cred.id) from Credentials cred where type(cred) =:type and UPPER(cred.user) = UPPER(:username)", Long.class)
                .setParameter("type", credentials.getClass())
                .setParameter("username", credentials.getUser().trim().toUpperCase())
                .getSingleResult();

        return count > 0;
	}

    /*
     * UC130_BR31	For proxy credentials, username must be unique per proxy host and proxy port.
     */
    @Override
    public Boolean isUniqueProxyCredentials(String username, String proxyHost, Integer proxyPort) {
        long count = entityManager
                .createQuery("SELECT count(e.id) from Endpoint as e join Credentials as c on e.proxyCredential.id = c.id where UPPER(c.user) = UPPER(:username) " +
                        "and UPPER(e.proxyHost) = UPPER(:proxyHost) " +
                        "and e.proxyPort = :proxyPort", Long.class)
                .setParameter("username", username.trim().toUpperCase())
                .setParameter("proxyHost", proxyHost.trim().toUpperCase())
                .setParameter("proxyPort", proxyPort)
                .getSingleResult();

        return count == 0;
    }

	@Override
    public List<Credentials> findByTypeAndUsername(Credentials credentials) {
        return entityManager
                .createQuery("from Credentials cred where type(cred) =:type and  UPPER(cred.user) = UPPER(:username)", Credentials.class)
                .setParameter("type", credentials.getClass())
                .setParameter("username", credentials.getUser().trim().toUpperCase())
                .getResultList();
    }
}

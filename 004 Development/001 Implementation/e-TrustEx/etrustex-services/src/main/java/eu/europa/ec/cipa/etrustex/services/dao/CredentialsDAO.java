package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.Credentials;
import eu.europa.ec.cipa.etrustex.domain.routing.Endpoint;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class CredentialsDAO extends TrustExDAO<Credentials, Long> implements ICredentialsDAO {

	@Override
	public Boolean isSignatureRequired(String authenticatedUser) {
		Credentials result = entityManager
		.createQuery(
				"from Credentials cred where cred.user =:authenticatedUser",
				Credentials.class).setParameter("authenticatedUser", authenticatedUser).getSingleResult();
		return result.getSignatureRequired();
	}

	@Override
	public Boolean exists(Credentials credentials) {

		List<Credentials> result = entityManager
				.createQuery("from Credentials cred where type(cred) =:type and UPPER(cred.user) = UPPER(:username)", Credentials.class)
                .setParameter("type", credentials.getClass())
                .setParameter("username", credentials.getUser().trim().toUpperCase())
                .getResultList();
		
		if (CollectionUtils.isNotEmpty(result)) {
			return true;
		}
		
		return false;
	}

    /*
     * UC130_BR31	For proxy credentials, username must be unique per proxy host and proxy port.
     */
    @Override
    public Boolean isUniqueProxyCredentials(String username, String proxyHost, Integer proxyPort) {
        boolean isUnique = false;
        List<Endpoint> result = entityManager
                .createQuery("from Endpoint e where UPPER(e.credentials.user) = UPPER(:username) " +
                        "and UPPER(e.proxyHost) = UPPER(:proxyHost) " +
                        "and e.proxyPort = :proxyPort", Endpoint.class)
                .setParameter("username", username.trim().toUpperCase())
                .setParameter("proxyHost", proxyHost.trim().toUpperCase())
                .setParameter("proxyPort", proxyPort)
                .getResultList();

        if (CollectionUtils.isEmpty(result)) {
            isUnique = true;
        }

        return isUnique;
    }

    /*
    NO LONGER NEEDED AFTER ETRUSTEX-1902  type PROVIDER --> WS, JMS, AMQP
     * UC130_BR32	For authentication credentials, username must be unique per
        •	Provider URL for JMS endpoints
        •	Webservice URL for WebService endpoints
        •	AMQP provider URL for AMQP endpoints
    */
//    @Override
//    public Boolean isUniqueProviderCredentials(Class endpointClass, String url, String username) {
//        boolean isUnique = false;
//        List<? extends Endpoint> result;
//        TypedQuery<? extends Endpoint> query;
//
//        if(endpointClass.equals(JMSEndpoint.class)) {
//            query = entityManager.createQuery("from JMSEndpoint e where UPPER(e.credentials.user) = UPPER(:username) " +
//                    "and UPPER(e.providerUrl) = UPPER(:url) ", endpointClass);
//        } else if (endpointClass.equals(WSEndpoint.class)) {
//            query = entityManager.createQuery("from WSEndpoint e where UPPER(e.credentials.user) = UPPER(:username) " +
//                    "and UPPER(e.wsEndpointURL) = UPPER(:url) ", endpointClass);
//        } else {
//            query = entityManager.createQuery("from AMQPEndpoint e where UPPER(e.credentials.user) = UPPER(:username) " +
//                    "and UPPER(e.providerUrl) = UPPER(:url) ", endpointClass);
//        }
//
//        result = query.setParameter("username", username.trim().toUpperCase()).setParameter("url", url.trim()).getResultList();
//
//        if (CollectionUtils.isEmpty(result)) {
//            isUnique =  true;
//        }
//
//        return isUnique;
//    }

	@Override
    public List<Credentials> findByTypeAndUsername(Credentials credentials) {
        return entityManager
                .createQuery("from Credentials cred where type(cred) =:type and  UPPER(cred.user) = UPPER(:username)", Credentials.class)
                .setParameter("type", credentials.getClass())
                .setParameter("username", credentials.getUser().trim().toUpperCase())
                .getResultList();
    }

    @Override
    public List<Credentials> findByTypeAndUsernameLike(Credentials credentials) {
        return entityManager
                .createQuery("from Credentials cred where type(cred) =:type and cred.user like  UPPER(:username)", Credentials.class)
                .setParameter("type", credentials.getClass())
                .setParameter("username", "%" + credentials.getUser().trim().toUpperCase() + "%")
                .getResultList();
    }
}

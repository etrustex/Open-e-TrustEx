/**
 * 
 */
package eu.europa.ec.etrustex.services.impl;

import eu.europa.ec.etrustex.dao.ICredentialsDAO;
import eu.europa.ec.etrustex.domain.Credentials;
import eu.europa.ec.etrustex.services.ICredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author batrian
 *
 */
@Service
public class CredentialsService implements ICredentialsService {

	@Autowired
	ICredentialsDAO credentialsDao;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Boolean exists(Credentials credentials) {
		return credentialsDao.exists(credentials);
	}

    @Override
    @Transactional(readOnly = true)
    public Boolean isUniqueProxyCredentials(String username, String proxyHost, Integer proxyPort) {
        return credentialsDao.isUniqueProxyCredentials(username, proxyHost, proxyPort);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Credentials> findByTypeAndUsername(Credentials credentials) {
        return credentialsDao.findByTypeAndUsername(credentials);
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Credentials get(Long id) {
		return credentialsDao.read(id);
	}
}

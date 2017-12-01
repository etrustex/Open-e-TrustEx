/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.Credentials;
import eu.europa.ec.cipa.etrustex.services.dao.ICredentialsDAO;

import java.util.List;

/**
 * @author batrian
 *
 */
@Service
public class CredentialsService implements ICredentialsService {

	@Autowired
	ICredentialsDAO credentiasDao;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Boolean exists(Credentials credentials) {
		return credentiasDao.exists(credentials);
	}

    @Override
    public Boolean isUniqueProxyCredentials(String username, String proxyHost, Integer proxyPort) {
        return credentiasDao.isUniqueProxyCredentials(username, proxyHost, proxyPort);
    }

    @Override
    public List<Credentials> findByTypeAndUsername(Credentials credentials) {
        return credentiasDao.findByTypeAndUsername(credentials);
    }

    @Override
    public List<Credentials> findByTypeAndUsernameLike(Credentials credentials) {
        return credentiasDao.findByTypeAndUsernameLike(credentials);
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Credentials get(Long id) {
		return credentiasDao.read(id);
	}
}

package eu.europa.ec.etrustex.dao.admin;


import eu.europa.ec.etrustex.domain.Credentials;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CredentialsRepository extends CrudRepository<Credentials,Long> {

}

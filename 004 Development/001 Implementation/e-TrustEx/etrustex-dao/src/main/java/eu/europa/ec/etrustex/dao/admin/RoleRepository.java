package eu.europa.ec.etrustex.dao.admin;

import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.stream.Stream;

@Transactional
public interface RoleRepository extends CrudRepository<Role, Long>{
    @Query("select r from Role r")
    Stream<Role> streamAllRoles();
}

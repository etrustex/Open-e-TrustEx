package eu.europa.ec.etrustex.dao.admin;

import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Transactional
public interface ProfileRepository extends CrudRepository<Profile,Long> {
    @Query("select p from Profile p")
    Stream<Profile> streamAllProfiles();
}

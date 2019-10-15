package eu.europa.ec.etrustex.dao.admin;

import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * Repository to manage {@link BusinessDomain} instances.
 *
 */
@Transactional
public interface BusinessDomainRepository extends CrudRepository<BusinessDomain, Long>, QueryDslPredicateExecutor<BusinessDomain> {
    /**
     * Special customization of {@link CrudRepository#findOne(java.io.Serializable)} to return a JDK 8 {@link Optional}.
     *
     * @param id
     * @return
     */
    Optional<BusinessDomain> findById(Long id);

    /**
     * Saves the given {@link BusinessDomain}.
     *
     * @param businessDomain
     * @return
     */
    <S extends BusinessDomain> S save(S businessDomain);

    /**
     * Finds {@link BusinessDomain} by name using JDK 8's {@link Optional} as return type.
     *
     * @param lastname
     * @return
     */
    Optional<BusinessDomain> findByName(String name);


    /**
     * Sample method to demonstrate support for {@link Stream} as a return type with a custom query. The query is executed
     * in a streaming fashion which means that the method returns as soon as the first results are ready.
     *
     * @return
     */
    @Query("select bd from BusinessDomain bd")
    Stream<BusinessDomain> streamAllBusinessDomains();

    /**
     * Sample method to demonstrate support for {@link Stream} as a return type with a derived query. The query is
     * executed in a streaming fashion which means that the method returns as soon as the first results are ready.
     *
     * @return
     */
    Stream<BusinessDomain> findAllByNameIgnoreCaseLike(String name);

}

package eu.europa.ec.etrustex.dao.admin;

import eu.europa.ec.etrustex.domain.routing.MessageRouting;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface MessageRoutingRepository extends CrudRepository<MessageRouting, Long>, QueryDslPredicateExecutor<MessageRouting> {

}

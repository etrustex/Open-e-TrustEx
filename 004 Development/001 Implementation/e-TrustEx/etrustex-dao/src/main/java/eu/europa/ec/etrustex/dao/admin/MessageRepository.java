package eu.europa.ec.etrustex.dao.admin;

import eu.europa.ec.etrustex.domain.Message;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MessageRepository extends CrudRepository<Message, Long>, QueryDslPredicateExecutor<Message> {
}

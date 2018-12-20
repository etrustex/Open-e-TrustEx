package eu.europa.ec.etrustex.dao.admin;

import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.QInterchangeAgreement;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

public interface InterchangeAgreementsRepository extends PagingAndSortingRepository<InterchangeAgreement, Long>, QueryDslPredicateExecutor<InterchangeAgreement>, QuerydslBinderCustomizer<QInterchangeAgreement> {
    @Override
    default void customize(QuerydslBindings bindings, QInterchangeAgreement interchangeAgreement) {
        bindings.bind(String.class).first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
        bindings.bind(interchangeAgreement.partyRoles.any().party.id).first((SimpleExpression::eq));
        bindings.bind(interchangeAgreement.profile.name).first(StringExpression::containsIgnoreCase);
        bindings.bind(interchangeAgreement.partyRoles.any().role.name).first(StringExpression::containsIgnoreCase);
        bindings.bind(interchangeAgreement.partyRoles.any().party.name).first(StringExpression::containsIgnoreCase);
        bindings.bind(interchangeAgreement.partyRoles.any().party.identifiers.any().value).first(StringExpression::containsIgnoreCase);
    }
}

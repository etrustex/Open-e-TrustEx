package eu.europa.ec.etrustex.dao.admin;

import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.QParty;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Set;


public interface PartyRepository extends PagingAndSortingRepository<Party, Long>, QueryDslPredicateExecutor<Party>, QuerydslBinderCustomizer<QParty> {
    @Override
    default void customize(QuerydslBindings bindings, QParty party) {
        bindings.bind(String.class).first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
        bindings.bind(party.identifiers.any().value).first(StringExpression::containsIgnoreCase);
        bindings.bind(party.delegatePartyAgreements.any().delegateParty.name).first(StringExpression::containsIgnoreCase);
//        bindings.excluding(party.accessInfo);
    }

    @Query("select case when count(mr) > 0 THEN true ELSE false end from MessageRouting mr join Endpoint ep on  mr.endpoint.id = ep.id where ep.party.id = ?1")
    boolean isInUseByMessageRouting(Long partyId);

    @Query("select case when count(m) > 0 THEN true ELSE false end from Message m where m.agreement.id in (select ica.id from InterchangeAgreement ica join ica.partyRoles pr join Party p on pr.party.id = p.id where p.id = ?1 )")
    boolean isInUseByMessage(Long partyId);

    @Modifying
    @Query("delete from PartyRole pr where pr.party.id = ?1")
    void deletePartyRoleByPartyId(Long partyId);

    @Modifying
    @Query("delete from EventNotification ev where ev.party.id = ?1")
    void deleteEventNotificationByPartyId(Long partyId);

    @Modifying
    @Query("delete from MetaDataItem mdi where mdi.sender.id = ?1")
    void deleteMetaDataItemByPartyId(Long partyId);

    @Modifying
    @Query("delete from Endpoint e where e.party.id = ?1")
    void deleteEndpointByPartyId(Long partyId);


    @Query("SELECT ica from InterchangeAgreement ica join ica.partyRoles pr join Party p on pr.party.id = p.id where p.id = ?1 ")
    Set<InterchangeAgreement> findInterchangeAgreementByPartyId(Long partyId);

    @Query("select case when count(mdi) > 0 THEN true ELSE false end from MetaDataItem as mdi where mdi.interchangeAgreement.id = ?1")
    Boolean isUsedByMetaDataItem(Long icaId);

    @Query("select case when count(m) > 0 THEN true ELSE false end from MessageResponseCode as m where m.interchangeAgreement.id = ?1")
    Boolean isUsedByMessageResponseCode(Long icaId);

    @Modifying
    @Query("delete from InterchangeAgreement ica where ica.id = ?1")
    void deleteInterchangeAgreement(Long icaId);
}

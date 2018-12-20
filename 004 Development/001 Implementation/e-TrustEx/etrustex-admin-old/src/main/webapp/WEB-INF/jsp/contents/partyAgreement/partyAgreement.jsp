<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<input type="hidden" id="pageMode" value="${pageMode}" />
<input type="hidden" id="refererPage" value="${refererPage.action}"/>
<input type="hidden" id="refererController" value="${refererPage.controller}"/>
<input type="hidden" id="accessRightsError" value="${accessRightsError}"/>

<c:choose>
	<c:when test="${pageMode eq 'view' }">
		<c:set var="url" value="/partyAgreement/${id}/view" />
	</c:when>
	<c:when test="${pageMode eq 'edit' }">
		<c:set var="url" value="/partyAgreement/${id}/edit" />
	</c:when>
	<c:otherwise>
		<c:set var="url" value="/partyAgreement/create" />
	</c:otherwise>
</c:choose>

<form:form modelAttribute="partyAgreementForm" id="partyAgreementSearchForm">
	<form:hidden path="authorizingParty.id"/>
	<form:hidden path="authorizingParty.name"/>
	<form:hidden path="delegateParty.id"/>
    <form:hidden path="delegateParty.name"/>
    <form:hidden path="transaction.id"/>
    <form:hidden path="transaction.name"/>
</form:form>

<form:form modelAttribute="partyAgreementForm" action="${url}" acceptCharset="UTF-8" id="partyAgreementForm" method="post">
	<j:box box_title_key="partyAgreement.${pageMode}.title">
		<div id="innerFragment"></div>
	</j:box>
</form:form>

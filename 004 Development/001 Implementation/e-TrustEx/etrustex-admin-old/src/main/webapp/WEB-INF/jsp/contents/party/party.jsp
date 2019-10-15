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
		<c:set var="url" value="/party/${partyId}/view" />
	</c:when>
	<c:when test="${pageMode eq 'edit' }">
		<c:set var="url" value="/party/${partyId}/edit" />
	</c:when>
	<c:otherwise>
		<c:set var="url" value="/party/create" />
	</c:otherwise>
</c:choose>

<form:form modelAttribute="partySearchForm" id="partySearchForm">
	<form:hidden path="name_search"/>
	<form:hidden path="businessDomainId"/>
	<form:hidden path="username_search"/>
	<form:hidden path="identifier_search"/>
</form:form>

<form:form modelAttribute="partyForm" action="${url}" acceptCharset="UTF-8" id="partyForm" method="post">
	<j:box box_title_key="party.${pageMode}.title">
		<div id="innerFragment"></div>
	</j:box>
</form:form>

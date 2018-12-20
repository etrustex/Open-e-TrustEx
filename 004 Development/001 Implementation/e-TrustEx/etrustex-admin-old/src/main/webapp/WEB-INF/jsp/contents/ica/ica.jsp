<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<input type="hidden" id="pageMode" value="${pageMode}" /> 
<input type="hidden" id="refererPage" value="${refererPage.action}"/>
<input type="hidden" id="isMultiple" value="${isMultiple}" />
<input type="hidden" id="accessRightsError" value="${accessRightsError}"/>

<c:choose>
	<c:when test="${isMultiple}">
		<c:set var="boxTitle" value="ica.multi.new.title" />
		<c:set var="url" value="/ica/multi/create" />
	</c:when>
	<c:otherwise>
		<form:form modelAttribute="interchangeAgreementSearchForm" id="interchangeAgreementSearchForm">
			<form:hidden path="party_search.partyName"/>
			<form:hidden path="partyRoleId"/>
			<form:hidden path="profileId"/>
		</form:form>
		<c:set var="boxTitle" value="ica.${pageMode}.title" />
		<c:choose>
			<c:when test="${pageMode eq 'view'}">
				<c:set var="url" value="/ica/${icaId}/view" />
			</c:when>
			<c:when test="${pageMode eq 'edit'}">
				<c:set var="url" value="/ica/${icaId}/edit" />
			</c:when>
			<c:otherwise>
				<c:set var="url" value="/ica/create" />
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>

<form:form modelAttribute="interchangeAgreement" action="${url}" acceptCharset="UTF-8" id="icaForm" method="post">
	<input type="hidden" name="db3_DRF:format" value="dd/MM/yyyy hh:mm">
	<j:box box_title_key="${boxTitle}">
		<div id="innerFragment"></div>
	</j:box>
</form:form>

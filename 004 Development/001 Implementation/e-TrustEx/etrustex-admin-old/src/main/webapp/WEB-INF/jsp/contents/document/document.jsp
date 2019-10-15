<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<input type="hidden" id="pageMode" value="${pageMode}" /> 
<input type="hidden" id="refererPage" value="${refererPage.action}"/>

<c:choose>
	<c:when test="${pageMode eq 'view' }">
		<c:set var="url" value="/document/${documentId}/view" />
	</c:when>
	<c:when test="${pageMode eq 'edit' }">
		<c:set var="url" value="/document/${documentId}/edit" />
	</c:when>
	<c:otherwise>
		<c:set var="url" value="/document/create" />
	</c:otherwise>
</c:choose>

<form:form modelAttribute="documentSearchForm" id="documentSearchForm">
	<form:hidden path="name_search"/>
	<form:hidden path="localName_search"/>
	<form:hidden path="typeCode_search"/>
</form:form>

<form:form modelAttribute="documentForm" action="${url}" acceptCharset="UTF-8" id="documentForm" method="post">
	<j:box box_title_key="document.${pageMode}.title">
		<div id="innerFragment"></div>
	</j:box>
</form:form>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<input type="hidden" id="pageMode" value="${pageMode}" />
<input type="hidden" id="refererPage" value="${refererPage.action}"/>

<c:choose>
	<c:when test="${not empty pageMode and pageMode eq 'view' }">
		<c:set var="url" value="/user/${userId}/view" />
	</c:when>
	<c:when test="${not empty pageMode and pageMode eq 'edit' }">
		<c:set var="url" value="/user/${userId}/edit" />
	</c:when>
	<c:when test="${not empty pageMode and pageMode eq 'changePwd' }">
		<c:set var="url" value="/user/changePwd" />
	</c:when>
	<c:otherwise>
		<c:set var="url" value="/user/create" />
	</c:otherwise>
</c:choose>

<form:form modelAttribute="userSearchForm" id="userSearchForm">
	<form:hidden path="usernameSearch"/>
	<form:hidden path="userRoleIdSearch"/>
	<form:hidden path="businessDomainIdSearch"/>
	<form:hidden path="partyIdSearch"/>
</form:form>
	
<form:form modelAttribute="userForm" action="${url}" acceptCharset="UTF-8" id="userForm" method="post">
	<j:box box_title_key="user.${pageMode}.title">
		<div id="innerFragment"></div>
	</j:box>
</form:form>

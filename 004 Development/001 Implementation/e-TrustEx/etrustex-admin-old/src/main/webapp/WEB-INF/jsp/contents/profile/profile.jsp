<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<input type="hidden" id="pageMode" value="${pageMode}" /> 
<input type="hidden" id="refererPage" value="${refererPage.action}"/>

<c:choose>
	<c:when test="${pageMode eq 'view' }">
		<c:set var="url" value="/profile/${profileId}/view" />
	</c:when>
	<c:when test="${pageMode eq 'edit' }">
		<c:set var="url" value="/profile/${profileId}/edit" />
	</c:when>
	<c:otherwise>
		<c:set var="url" value="/profile/create" />
	</c:otherwise>
</c:choose>

<form:form modelAttribute="profileForm" action="${url}" acceptCharset="UTF-8" id="profileForm" method="post">
	<j:box box_title_key="profile.${pageMode}.title">
		<div id="innerFragment"></div>
	</j:box>
</form:form>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:choose>
	<c:when test="${fn:toUpperCase(logsSearchForm.module_search) eq 'CIPADMIN'}">
		<c:set var="boxTitle" value="logs.search.title.cipadmin" />
	</c:when>
	<c:otherwise>
		<c:set var="boxTitle" value="logs.search.title.etrustex" />
	</c:otherwise>
</c:choose>

<form:form modelAttribute="logsSearchForm" action="/logs/search" acceptCharset="UTF-8" id="logsSearchForm" method="post">
	<form:hidden path="module_search"/>
	<j:box box_title_key="${boxTitle}">
		<div id="innerFragment"></div>
	</j:box>
</form:form>

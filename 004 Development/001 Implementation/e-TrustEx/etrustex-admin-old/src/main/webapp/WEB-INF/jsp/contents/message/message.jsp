<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<input type="hidden" id="pageMode" value="${pageMode}" /> 
<input type="hidden" id="refererPage" value="${refererPage.action}"/>

<c:set var="url" value="/message/${messageId}/view" />

<form:form modelAttribute="messageForm" action="${url}" acceptCharset="UTF-8" id="messageForm" method="post">
	<j:box box_title_key="message.${pageMode}.title">
		<div id="innerFragment"></div>
	</j:box>
</form:form>

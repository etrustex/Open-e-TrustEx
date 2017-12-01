<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<%-- 	
<c:choose>
	<c:when test="${not empty certificateMode and certificateMode eq 'view' }">
		<c:set var="url" value="/certificate/${certificateId}/view" />
	</c:when>
	<c:when test="${not empty certificateMode and certificateMode eq 'edit' }">
		<c:set var="url" value="/certificate/${certificateId}/edit" />
	</c:when>
	<c:otherwise>
		<c:set var="url" value="/certificate/new" />
	</c:otherwise>
</c:choose>
 --%>
<c:url var="url" value="/party/certificates/import.do" />

<form:form modelAttribute="certificatesForm" action="${url}" acceptCharset="UTF-8" id="certificatesForm" method="post" enctype="multipart/form-data">
	<div id="innerFragment"></div>
</form:form>

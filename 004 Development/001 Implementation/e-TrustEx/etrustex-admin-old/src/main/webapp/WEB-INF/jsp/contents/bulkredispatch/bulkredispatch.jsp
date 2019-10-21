<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<c:url var="url" value="/bulkredispatch/search" />

<form:form modelAttribute="bulkRedispatchForm" action="${url}" acceptCharset="UTF-8" id="bulkRedispatchForm" method="post" enctype="multipart/form-data"  target="upload_target">	
	<div id="innerFragment"></div>
</form:form>
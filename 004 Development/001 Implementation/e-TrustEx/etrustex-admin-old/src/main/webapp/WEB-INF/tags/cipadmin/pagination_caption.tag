<%@ tag language="java" isELIgnored="false" body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ attribute name="page" type="java.lang.Integer" required="true" rtexprvalue="true" description="The current page (not required, defaults to 1)" %>
<%@ attribute name="size" type="java.lang.Integer" required="true" rtexprvalue="true" description="The number of records per page" %>

<c:if test="${empty page || page lt 1}">
   <c:set var="page" value="1" />
</c:if>
<c:if test="${empty size || size lt 1}">
  <c:set var="size" value="50" />
</c:if>

<caption>
	<fmt:message key="records.found.pagination">
		<fmt:param value="${(page - 1) * size + 1}" />
		<fmt:param value="${page * size > total ? total : page * size}" />
		<fmt:param value="${total}" />
	</fmt:message>
</caption>
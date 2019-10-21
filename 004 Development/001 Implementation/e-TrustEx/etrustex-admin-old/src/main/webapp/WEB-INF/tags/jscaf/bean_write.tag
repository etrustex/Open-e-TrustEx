<%@ tag language="java" isELIgnored="false" body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ tag description="bean write replacement for abstraction" %>
<%@ attribute name="var" required="false" type="java.lang.String" description="" %>
<%@ attribute name="name" required="false" type="java.lang.String" description="" %>
<%@ attribute name="property" required="false" type="java.lang.String" description="" %>
<%@ attribute name="format" required="false" type="java.lang.String" description="" %>
<% if (null != format && !format.equals("")) {%>
	<fmt:formatDate value="${var}" pattern="${format}" var="newdatevar" />
	<c:out value="${newdatevar}" />
<%} else {%>
    <c:out value="${var}" />
<%}%>
<%@ tag language="java" isELIgnored="false" body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<%@ attribute name="item_id" required="true" type="java.lang.String" description="" %>
<%@ attribute name="item_name" required="true" type="java.lang.String" description="" %>
<%@ attribute name="item_value" required="false" type="java.lang.String" description="" %>
<%@ attribute name="group_value" required="false" type="java.lang.String" description="" %>
<%@ attribute name="label_key" required="false" type="java.lang.String" description="" %>
<%@ attribute name="label_text" required="false" type="java.lang.String" description="" %>

<c:choose>
    <c:when test="${group_value eq item_value}">
        <c:set var="checked" value="checked"/>
    </c:when>
    <c:otherwise>
        <c:set var="checked" value=""/>
    </c:otherwise>
</c:choose>
<input type="radio" id="${item_id}" name="${item_name}" value="${item_value}" ${checked} class="JS_radio">
<label for="${item_id}">
    <% if (null!=label_key && !label_key.equals("")) { %>
        <j:bean_message message_key="${label_key}"/>
    <% } else { %>
        ${label_text}
    <% } %>
</label>
<br>




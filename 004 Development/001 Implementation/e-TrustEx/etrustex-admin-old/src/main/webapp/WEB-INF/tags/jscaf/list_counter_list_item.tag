<%@ tag language="java" isELIgnored="false" body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<%@ attribute name="item_value" required="true" type="java.lang.String" description="" %>
<%@ attribute name="selected_item_value" required="true" type="java.lang.String" description="" %>
<%@ attribute name="list_action_class" required="true" type="java.lang.String" description="" %>
<%@ attribute name="list_title" required="true" type="java.lang.String" description="" %>
<%@ attribute name="counter_value" required="true" type="java.lang.String" description="" %>
<%@ attribute name="title_text" required="false" type="java.lang.String" description="" %>
<%@ attribute name="title_key" required="false" type="java.lang.String" description="" %>

<c:choose>
    <c:when test="${null != selected_item_value && item_value == selected_item_value}">
        <c:set var="activeClass" value="active" scope="page"/>
    </c:when>
    <c:otherwise>
        <c:set var="activeClass" value="" scope="page"/>
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${!empty title_key}">
        <li class="${list_action_class} ${activeClass}" listItem="${item_value}" title="<j:bean_message message_key="${title_key}"/>">
    </c:when>
    <c:when test="${!empty title_text}">
        <li class="${list_action_class} ${activeClass}" listItem="${item_value}" title="${title_text}">
    </c:when>
    <c:otherwise>
        <li class="${list_action_class} ${activeClass}" listItem="${item_value}">
    </c:otherwise>
</c:choose>
    ${list_title}
    <% if (counter_value.equals("--")) { %>
    <span class="tag empty">
    <% } else { %>
    <span class="tag">
    <% } %>
        ${counter_value}
    </span>
</li>
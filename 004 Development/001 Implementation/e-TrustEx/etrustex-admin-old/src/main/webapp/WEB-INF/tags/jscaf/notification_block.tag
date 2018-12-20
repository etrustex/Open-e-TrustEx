<%@ tag language="java" isELIgnored="false" body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<%@ attribute name="notif_text" required="false" type="java.lang.String" description="" %>
<%@ attribute name="notif_key" required="false" type="java.lang.String" description="" %>
<%@ attribute name="notif_type" required="false" type="java.lang.String" description="" %>
<%@ attribute name="optional_class" required="false" type="java.lang.String" description="" %>

<%
    if (null==notif_type || notif_type.equals("")) {
        notif_type = "note-info";
    }
    request.setAttribute("notif_type",notif_type);
%>

<div class="notification ${notif_type} small ${optional_class}">
    <p class="normal">
        <% if (null!=notif_key && !notif_key.equals("")) { %>
            <j:bean_message message_key="${notif_key}"/>
        <% } else { %>
            ${notif_text}
        <% } %>
    </p>
</div>





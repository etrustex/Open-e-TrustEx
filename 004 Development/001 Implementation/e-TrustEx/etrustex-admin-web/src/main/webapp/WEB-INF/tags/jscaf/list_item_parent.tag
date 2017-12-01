<%@ tag language="java" isELIgnored="false" body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<%@ attribute name="list_id" required="true" type="java.lang.String" description="" %>
<%@ attribute name="list_class" required="false" type="java.lang.String" description="" %>
<%@ attribute name="icon_class" required="false" type="java.lang.String" description="" %>
<%@ attribute name="title_key" required="false" type="java.lang.String" description="" %>
<%@ attribute name="title_text" required="false" type="java.lang.String" description="" %>
<%@ attribute name="counter" required="false" type="java.lang.String" description="" %>
<%@ attribute name="data_attr_map" required="false" type="java.lang.String" description="" %>

<%
    String dataAttributes = "";
    request.setAttribute("dataAttributes",dataAttributes);
%>    

<li id="${list_id}" class="parent ${list_class}" ${dataAttributes}>

    <% if (null != icon_class && !icon_class.equals("")) { %>
        <span class="icon ${icon_class}"></span>
    <% } %>

    <span class="title">
        <% if (null!=title_key && !title_key.equals("")) { %>
            <j:bean_message message_key="${title_key}"/>
        <% } else { %>
            ${title_text}
        <% } %>
    </span>
    <% if(counter!=null && !counter.equals("")){ %>
        <% if(counter.equals("0") || counter.equals("--")) { %>
            <span class="counter empty">${counter}</span>
        <% } else { %>
            <span class="counter">${counter}</span>
        <% } %>
    <% } %>
</li>
<span id="${list_id}_children" class="child-wrapper hidden">
    <jsp:doBody/>
</span>


<%@ tag language="java" isELIgnored="false" body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>


<%@ attribute name="list_id" required="false" type="java.lang.String" description="" %>
<%@ attribute name="list_class" required="false" type="java.lang.String" description="" %>
<%@ attribute name="title_key" required="false" type="java.lang.String" description="" %>
<%@ attribute name="title_text" required="false" type="java.lang.String" description="" %>
<%@ attribute name="counter" required="false" type="java.lang.String" description="" %>
<%@ attribute name="has_body" required="false" type="java.lang.String" description="" %>
<%@ attribute name="data_attr_map" required="false" type="java.lang.String" description="" %>

<%
    String dataAttributes = "";

    request.setAttribute("dataAttributes",dataAttributes);

    String counterClass="";
    if(counter!=null && !counter.equals("")){
        if(counter.equals("0") || counter.equals("--")) {
            counterClass="empty";
        }
    }
    request.setAttribute("counterClass",counterClass);
%>

<li ${list_id} class="child ${counterClass} ${list_class}" ${dataAttributes}>
    <span class="title">
        <% if (null!=title_key && !title_key.equals("")) { %>
            <j:bean_message message_key="${title_key}"/>
        <% } else { %>
            ${title_text}
        <% } %>
    </span>
    <span class="counter">${counter}</span>

    <% if (null!=has_body && has_body.equals("true")) { %>
        <jsp:doBody/>
    <% } %>
</li>




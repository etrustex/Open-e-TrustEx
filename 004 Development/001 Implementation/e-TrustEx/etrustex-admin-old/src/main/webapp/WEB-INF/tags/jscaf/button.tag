<%@ tag language="java" isELIgnored="false" body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<%@ tag description="Generate button" %>

<%@ attribute name="action_id" required="false" type="java.lang.String" description="" %>
<%@ attribute name="action_class" required="false" type="java.lang.String" description="" %>
<%@ attribute name="href" required="false" type="java.lang.String" description="" %>
<%@ attribute name="button_type_class" required="false" type="java.lang.String" description="" %>

<%@ attribute name="link_style" required="false" type="java.lang.String" description="" %>
<%@ attribute name="button_icon_class" required="false" type="java.lang.String" description="" %>
<%@ attribute name="button_optional_class" required="false" type="java.lang.String" description="" %>
<%@ attribute name="button_style" required="false" type="java.lang.String" description="" %>

<%@ attribute name="tooltip_key" required="false" type="java.lang.String" description="" %>
<%@ attribute name="tooltip_text" required="false" type="java.lang.String" description="" %>

<%@ attribute name="label_key" required="false" type="java.lang.String" description="" %>
<%@ attribute name="label_text" required="false" type="java.lang.String" description="" %>

<%--
<%@ attribute name="attr1" required="false" type="java.lang.String" description="" %>
<%@ attribute name="attr2" required="false" type="java.lang.String" description="" %>
<%@ attribute name="attr3" required="false" type="java.lang.String" description="" %>
--%>

<%@ attribute name="data" required="false" type="java.lang.String" description="" %>
<%@ attribute name="data_inner" required="false" type="java.lang.String" description="" %>


<c:if test="${button_type_class==null or button_type_class==''}">
    <c:set var="button_type_class" value="blue_button"/>
</c:if>
<% if (null!=action_class) {
    if (action_class.contains("fr")) { %>
        <c:set var="float_position" value="fr"/>        
    <% } else { %>
        <c:set var="float_position" value="fl"/>
    <% } %>
<% } else { %>
    <% if (null!=button_optional_class && button_optional_class.contains("full-width")) { %>
        <c:set var="float_position" value=""/>
    <% } else { %>
        <c:set var="float_position" value="fl"/>
    <% } %>
<% } %>
<c:if test="${(null!=tooltip_key and tooltip_key!='') or (null!=tooltip_text and tooltip_text!='')}">
    <c:set var="tooltip_class" value="JS_tooltip"/>
    <c:set var="tooltip_data_attr" value="data-content=\"html\""/>
</c:if>
<% if (null!=action_id && !action_id.equals("")) { %>
    <a id="${action_id}" class="${action_class} button-link cr-pointer ${float_position} ${tooltip_class}" ${data} ${tooltip_data_attr} style="${link_style}">
<% } else { %>
    <% if (null!=href && !href.equals("")) { %>
        <a class="${action_class} button-link cr-pointer ${float_position}" href="${href}" style="${link_style}" ${data}>
    <% } else { %>
        <a class="${action_class} button-link cr-pointer ${float_position}" style="${link_style}" ${data}>
    <% } %>
<% } %>
<c:if test="${(label_key==null or label_key=='') and  (label_text==null or label_text=='')}">
    <c:set var="button_no_label_class" value="no-label"/>
</c:if>
<c:if test="${(button_icon_class==null or button_icon_class=='')}">
    <c:set var="button_no_icon_class" value="no-icon"/>
</c:if>
    <span class="button ${button_type_class} ${button_optional_class} ${button_no_label_class} ${button_no_icon_class} ${data_inner} enabled" style="${button_style}">
        <span><span>
            <% if (null!=button_icon_class && !button_icon_class.equals("")) { %>
                <% if ((null!=button_optional_class && !button_optional_class.contains("no-icon")) || null==button_optional_class) { %>
                    <b class="icon-button ${button_icon_class} fl"></b>
                <% }%>
            <% } %>
            <% if (null!=label_key && !label_key.equals("")) { %> 
                <em><j:bean_message message_key="${label_key}"/></em>
            <% } else { %>
                <% if (null!=label_text && !label_text.equals("")) { %>
                    <em>${label_text}</em>
                <% } else { %>
                    <em>&nbsp;</em>
                <% } %>
            <% } %>
        </span></span>
    </span>
</a>
<% if ((null!=tooltip_key && !tooltip_key.equals("")) || (null!=tooltip_text && !tooltip_text.equals(""))) { %>
    <div id="${action_id}_content" class="hidden">
    <% if (null!=tooltip_key && !tooltip_key.equals("")) { %>
        <j:bean_message message_key="${tooltip_key}"/>
    <% } %>
    <% if (null!=tooltip_text && !tooltip_text.equals("")) { %>
        ${tooltip_text}   
    <% } %>
    </div>
<% } %>

        
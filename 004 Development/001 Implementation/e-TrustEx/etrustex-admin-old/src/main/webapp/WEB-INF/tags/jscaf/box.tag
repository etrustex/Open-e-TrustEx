
<%@ tag language="java" isELIgnored="false" body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>
<%@ tag description="Generate box" %>

<%@ attribute name="box_id"                 required="false" type="java.lang.String" description="" %>
<%@ attribute name="box_title_key"          required="false" type="java.lang.String" description="" %>
<%@ attribute name="box_title_text"         required="false" type="java.lang.String" description="" %>
<%@ attribute name="box_title_class"        required="false" type="java.lang.String" description="" %>
<%@ attribute name="box_icon_class"         required="false" type="java.lang.String" description="" %>
<%@ attribute name="box_optional_class"     required="false" type="java.lang.String" description="" %>
<%@ attribute name="content_optional_class" required="false" type="java.lang.String" description="" %>
<%@ attribute name="is_expandable_box"      required="false" type="java.lang.String" description="" %>
<%@ attribute name="is_expandable_box_default_hidden"      required="false" type="java.lang.String" description="" %>



<% if (null!=box_id && !box_id.equals("")) { %>
<div id="${box_id}" class="box-wrapper">
<% } else { %>
<div class="box-wrapper">
<% } %>
    <div class="box ${box_optional_class}">
        <div class="header">
            <span class="icon ${box_icon_class}"></span>
            <h2 class="${box_title_class}">
                <% if (null!=box_title_key && !box_title_key.equals("")) { %>
                    <j:bean_message message_key="${box_title_key}"/>
                <% } else { %>
                    ${box_title_text}
                <% } %>
            </h2>
            <div class="cl"></div>
        </div>

        <div class="content clear ${content_optional_class}">

            <jsp:doBody/>

        </div>
    </div>
</div>
<%@ tag language="java" isELIgnored="false" body-content="scriptless" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<%@ attribute name="field_id" required="false" type="java.lang.String" description="" %>
<%@ attribute name="field_class" required="false" type="java.lang.String" description="" %>
<%@ attribute name="field_style" required="false" type="java.lang.String" description="" %>
<%@ attribute name="label_key" required="false" type="java.lang.String" description="" %>
<%@ attribute name="label_text" required="false" type="java.lang.String" description="" %>
<%@ attribute name="label_width" required="false" type="java.lang.String" description="" %>
<%@ attribute name="label_class" required="false" type="java.lang.String" description="" %>
<%@ attribute name="hint" required="false" type="java.lang.String" description="" %>
<%@ attribute name="hint_key" required="false" type="java.lang.String" description="" %>
<%@ attribute name="heading_class" required="false" type="java.lang.String" description="" %>

<% if ((null!=hint && !hint.equals("")) || (null!=hint_key && !hint_key.equals(""))) { %>
<div id="${field_id}" class="field with-hint ${field_class}" style="${field_style}">
        <% } else { %>
    <div id="${field_id}" class="field ${field_class}" style="${field_style}">
        <% } %>

        <% if ((null!=label_text && !label_text.equals("")) || (null!=label_key && !label_key.equals(""))) { %>

            <div class="field-label ${label_class}"
                    <% if (null!=label_width && !label_width.equals("")) {%>
                 style="width:${label_width}px"
                    <% } %>
                    >
                <h6 class="${heading_class}">
                    <% if (null!=label_key && !label_key.equals("")) { %>
                        <j:bean_message message_key="${label_key}"/>
                    <% } else { %>
                        ${label_text}
                    <% } %>
                </h6>

                <% if ((null!=hint && !hint.equals("")) || (null!=hint_key && !hint_key.equals(""))) { %>
                <span class="hint"
                        <% if (null!=label_width && !label_width.equals("")) {%>
                      style="width:${label_width}px"
                        <% } %> >
                        <% if (null!=hint_key && !hint_key.equals("")) { %>
                                <j:bean_message message_key="${hint_key}"/>
                        <% } else { %>
                                ${hint}
                        <% } %>
                </span>
                <% } %>

            </div>

        <% } %>

        <jsp:doBody/>

    </div>
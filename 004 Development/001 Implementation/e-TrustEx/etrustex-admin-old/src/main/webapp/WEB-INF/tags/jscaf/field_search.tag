<%@ tag language="java" isELIgnored="false" body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<%@ attribute name="field_class" required="false" type="java.lang.String" description="" %>
<%@ attribute name="label_key" required="false" type="java.lang.String" description="" %>
<%@ attribute name="label_text" required="false" type="java.lang.String" description="" %>
<%@ attribute name="label_width" required="false" type="java.lang.String" description="" %>
<%@ attribute name="hint" required="false" type="java.lang.String" description="" %>
<%@ attribute name="hint_key" required="false" type="java.lang.String" description="" %>
<%@ attribute name="target_id" required="true" type="java.lang.String" description="" %>
<%@ attribute name="target_value" required="false" type="java.lang.String" description="" %>
<%@ attribute name="target_input_width" required="false" type="java.lang.String" description="" %>
<%@ attribute name="button_action_id" required="false" type="java.lang.String" description="" %>
<%@ attribute name="button_action_class" required="false" type="java.lang.String" description="" %>
<%@ attribute name="button_attr1" required="false" type="java.lang.String" description="" %>

<% if ((null!=hint && !hint.equals("")) || (null!=hint_key && !hint_key.equals(""))) { %>
    <div class="field with-hint ${field_class}">
<% } else { %>
    <div class="field ${field_class}">
<% } %>

    <div class="field-label"
    <% if (null!=label_width && !label_width.equals("")) {%>
        style="width:${label_width}px"
    <% } %>
    >
        <% if (null!=label_key) { %>
              <j:bean_message message_key="${label_key}"/>
        <% } else { %>
            <% if (null!=label_text) { %>
                <h6>${label_text}</h6>
            <% } else { %>
                <h6>&nbsp;</h6>
            <% } %>
        <% } %>

        <% if ((null!=hint && !hint.equals("")) || (null!=hint_key && !hint_key.equals(""))) { %>
            <span class="hint"
                <% if (null!=label_width && !label_width.equals("")) {%>
                    style="width:${label_width}px"
                <% } %>
            >
                    <% if (null!=hint_key && !hint_key.equals("")) { %>
                            <j:bean_message message_key="${hint_key}"/>
                    <% } else { %>
                            ${hint}
                    <% } %>
            </span>
        <% } %>

    </div>

    <span id="${target_id}" class="field-view fl text-align-left" style="width:${target_input_width}px;">
        ${target_value}
    </span>

    <% if (null!=button_action_id && !button_action_id.equals("")) { %>
        <a id="${button_action_id}" class="${button_action_class} search-button" ${button_attr1}>
    <% } else { %>
        <a class="${button_action_class} search-button" ${button_attr1}>
    <% } %>
    </a>

    <jsp:doBody/>


</div>

<%@ tag language="java" isELIgnored="false" body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>
<%@ tag description="Generate box" %>

<%@ attribute name="buttonset_id" required="true" type="java.lang.String" description="" %>
<%@ attribute name="var" required="false" type="java.lang.String" description="" %>

<%@ attribute name="content_display_value" required="false" type="java.lang.String" description="" %>
<%@ attribute name="content_display_value_2" required="false" type="java.lang.String" description="" %>
<%@ attribute name="buttonset_class" required="false" type="java.lang.String" description="" %>
<%@ attribute name="buttonset_value" required="false" type="java.lang.String" description="" %>

<%@ attribute name="button1_value" required="true" type="java.lang.String" description="" %>
<%@ attribute name="button1_text" required="false" type="java.lang.String" description="" %>
<%@ attribute name="button1_key" required="false" type="java.lang.String" description="" %>
<%@ attribute name="button1_checked" required="false" type="java.lang.String" description="" %>
<%@ attribute name="button2_value" required="true" type="java.lang.String" description="" %>
<%@ attribute name="button2_text" required="false" type="java.lang.String" description="" %>
<%@ attribute name="button2_key" required="false" type="java.lang.String" description="" %>
<%@ attribute name="button2_checked" required="false" type="java.lang.String" description="" %>
<%@ attribute name="button3_value" required="false" type="java.lang.String" description="" %>
<%@ attribute name="button3_text" required="false" type="java.lang.String" description="" %>
<%@ attribute name="button3_key" required="false" type="java.lang.String" description="" %>
<%@ attribute name="button3_checked" required="false" type="java.lang.String" description="" %>

<%
    String buttonCheckedIndex = "1";

    if (null != buttonset_value) {
        if (null != button1_value) {
            if (buttonset_value.equals(button1_value)) {
                buttonCheckedIndex = "1";
            }
        }
        if (null != button2_value) {
            if (buttonset_value.equals(button2_value)) {
                buttonCheckedIndex = "2";
            }
        }
        if (null != button3_value) {
            if (buttonset_value.equals(button3_value)) {
                buttonCheckedIndex = "3";
            }
        }
    }

    request.setAttribute("buttonCheckedIndex",buttonCheckedIndex);
%>


<input id="<%=buttonset_id+"_input"%>"
       name="${var}"
       value="<jscaf:bean_write var="${var}"/>"
       type="hidden"/>

<span class="field-other-input fl">
    <span id="${buttonset_id}" class="JS_buttonSet JS_live-validation ${buttonset_class}"
          associatedpropertyid="<%=buttonset_id+"_input"%>"
          associatedcontentdisplaybasedonvalue="${content_display_value}"
          associatedContentDisplayValue="${content_display_value_2}">

        <input type="radio" id="<%=buttonset_id+button1_value%>" name="${buttonset_id}" value="${button1_value}"
            <% if (null!=buttonCheckedIndex && buttonCheckedIndex.equals("1")) { %>
               checked="checked">
            <% } else { %>
                <% if ((null==button1_checked && null==button2_checked && null==button3_checked) || (null!=button1_checked && button1_checked.equals("Y"))) { %>
                    checked="checked">
                <% } else { %>
                    >
                <% } %>
            <% } %>
            <label for="<%=buttonset_id+button1_value%>">
                <% if (null!=button1_key && !button1_key.equals("")) { %>
                <j:bean_message message_key="${button1_key}"/>
                <% } else { %>
                ${button1_text}
                <% } %>
            </label>

        <input type="radio" id="<%=buttonset_id+button2_value%>" name="${buttonset_id}" value="${button2_value}"

            <% if (null!=buttonCheckedIndex && buttonCheckedIndex.equals("2")) { %>
               checked="checked">
            <% } else { %>
                <% if (null!=button2_checked && button2_checked.equals("Y")) { %>
                   checked="checked">
                <% } else { %>
                    >
                <% } %>
            <% } %>
            <label for="<%=buttonset_id+button2_value%>">
                <% if (null!=button2_key && !button2_key.equals("")) { %>
                <j:bean_message message_key="${button2_key}"/>
                <% } else { %>
                ${button2_text}
                <% } %>
            </label>

        <% if (((null!=button3_text || null!=button3_key) && null!=button3_value)) { %>

            <input type="radio" id="<%=buttonset_id+button3_value%>" name="${buttonset_id}" value="${button3_value}"
                <% if (null!=buttonCheckedIndex && buttonCheckedIndex.equals("3")) { %>
                   checked="checked">
                <% } else { %>
                    <% if (null!=button3_checked && button3_checked.equals("Y")) { %>
                       checked="checked">
                    <% } else { %>
                        >
                    <% } %>
                <% } %>
                <label for="<%=buttonset_id+button3_value%>">
                    <% if (null!=button3_key && !button3_key.equals("")) { %>
                    <j:bean_message message_key="${button3_key}"/>
                    <% } else { %>
                    ${button3_text}
                    <% } %>
                </label>

        <% } %>

    </span>
</span>



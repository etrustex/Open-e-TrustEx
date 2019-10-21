<%@ tag language="java" isELIgnored="false" body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<%@ attribute name="id" required="true" type="java.lang.String" description="" %>
<%@ attribute name="var" required="true" type="java.lang.String" description="" %>

<%@ attribute name="type" required="false" type="java.lang.String" description="" %>
<%@ attribute name="field_class" required="false" type="java.lang.String" description="" %>
<%@ attribute name="label_key" required="false" type="java.lang.String" description="" %>
<%@ attribute name="label_text" required="false" type="java.lang.String" description="" %>
<%@ attribute name="label_width" required="false" type="java.lang.String" description="" %>
<%@ attribute name="input_class" required="false" type="java.lang.String" description="" %>

<div class="field ${field_class}">
    <div class="field-label"
            <% if (null != label_width && !label_width.equals("")) {%>
         style="width:${label_width}px"
            <% } %>
            >
        <h6>
            <% if (null!=label_key && !label_key.equals("")) { %>
            <j:bean_message message_key="${label_key}"/>
            <% } else { %>
            ${label_text}
            <% } %>
        </h6>
    </div>
    <% if ("date".equals(type)) { %>
    <input type="text"
           id="${id}"
           name="${property_name}"
           value="<j:bean_write var="${var}" format="dd/MM/yyyy"/>"
           class="field-value JS_datetimepicker ${input_class} JS_dateonly JS_live-validation"/>
    <% } else { %>
    <input type="text"
           id="${id}"
           name="${property_name}"
           value="<j:bean_write var="${var}" format="dd/MM/yyyy HH:mm"/>"
           class="field-value JS_datetimepicker ${input_class} JS_live-validation"/>
    <% } %>
</div>
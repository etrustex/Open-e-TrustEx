<%@ tag language="java" isELIgnored="false" body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<%@ attribute name="item_id" required="false" type="java.lang.String" description="" %>
<%@ attribute name="icon_class" required="false" type="java.lang.String" description="" %>
<%@ attribute name="icon_font_class" required="false" type="java.lang.String" description="" %>
<%@ attribute name="optional_class" required="false" type="java.lang.String" description="" %>
<%@ attribute name="tooltip_text" required="false" type="java.lang.String" description="" %>
<%@ attribute name="tooltip_key" required="false" type="java.lang.String" description="" %>
<%@ attribute name="link_id" required="false" type="java.lang.String" description="" %>
<%@ attribute name="link_url" required="false" type="java.lang.String" description="" %>
<%@ attribute name="link_on_click" required="false" type="java.lang.String" description="" %>
<%@ attribute name="title_text" required="false" type="java.lang.String" description="" %>
<%@ attribute name="title_key" required="false" type="java.lang.String" description="" %>
<%@ attribute name="tag_count" required="false" type="java.lang.String" description="" %>
<%@ attribute name="tag_not_empty_color_class" required="false" type="java.lang.String" description="" %>


<%
String buttonNotification="";
String tagColorClass="";

if (null!=tag_count && !tag_count.equals("")) {
  if (!tag_count.equals("0")) {
     buttonNotification = "<span class=\"button-notification with-items\">" + tag_count + "</span>";
     if (null!=tag_not_empty_color_class && !tag_not_empty_color_class.equals("")) {
         if (!tag_not_empty_color_class.equals("none")) {
            tagColorClass = tag_not_empty_color_class;
         } else {
            buttonNotification = "<span class=\"button-notification without-items\">" + tag_count + "</span>";
         }
     } else {
        tagColorClass = "red";
     }
  } else {
    buttonNotification = "<span class=\"button-notification without-items\">" + tag_count + "</span>";
  }
}

request.setAttribute("tagColorClass",tagColorClass);
request.setAttribute("buttonNotification",buttonNotification);
%>

<li class="JS_tooltip ${optional_class} ${tagColorClass}"
    title="
        <% if (null!=tooltip_key && !tooltip_key.equals("")) { %>
            <j:bean_message message_key="${tooltip_key}"/>
        <% } else { %>
            ${tooltip_text}
        <% } %>
    ">
    <c:out value="${buttonNotification}" escapeXml="false"/>

    <% if (null!=link_url && !link_url.equals("")) { %>
        <a id="${link_id}" href="<c:url value="${link_url}"/>">
    <% } else if (null!=link_on_click && !link_on_click.equals("")) { %>
        <a id="${link_id}" onClick="${link_on_click}">
    <% } else { %>
        <a id="${link_id}">
    <% } %>
        <% if (null!=icon_font_class && !icon_font_class.equals("")) { %>
            <i class="icon ${icon_font_class}"></i><br>
        <% } else { %>
            <span class="icon-dashboard ${icon_class}"></span>
        <% } %>
        <% if (null!=title_key && !title_key.equals("")) { %>
            <j:bean_message message_key="${title_key}"/>
        <% } else { %>
            ${title_text}
        <% } %>
    </a>
</li>


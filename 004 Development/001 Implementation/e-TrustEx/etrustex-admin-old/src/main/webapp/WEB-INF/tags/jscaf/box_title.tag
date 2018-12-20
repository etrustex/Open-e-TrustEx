<%@ tag language="java" isELIgnored="false" body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ tag description="Generate box title" %>

<%@ attribute name="box_title_key"          required="false" type="java.lang.String" description="" %>
<%@ attribute name="box_title_text"         required="false" type="java.lang.String" description="" %>
<%@ attribute name="box_optional_class"     required="false" type="java.lang.String" description="" %>
<%@ attribute name="content_optional_class" required="false" type="java.lang.String" description="" %>

<div class="title-box ${box_optional_class}">
    <div class="header cl">
        <h6>
            <c:choose>
                <c:when test="${empty box_title_key}">
                    ${box_title_text}
                </c:when>
                <c:otherwise>
                    <j:bean_message message_key="${box_title_key}"/>
                </c:otherwise>
            </c:choose>
        </h6>
    </div>
    <div class="content clear ${content_optional_class}">
        <jsp:doBody/>
    </div>
</div>

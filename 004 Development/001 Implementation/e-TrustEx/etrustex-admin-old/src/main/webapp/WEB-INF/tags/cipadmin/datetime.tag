<%@ tag language="java" isELIgnored="false" body-content="scriptless" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ attribute name="fieldPath" type="java.lang.String" required="true" description="" %>

<div class="input-group date">
    <form:input path="${fieldPath}" class="field-value" type="text" />
    <span class="input-group-addon">
        <span><img src='<c:url value="/styles/images/common/datepicker-calendar.png"/>' class="ui-datepicker-trigger" data-time-icon="icon-time" data-date-icon="icon-calendar"></span>
    </span>
</div>


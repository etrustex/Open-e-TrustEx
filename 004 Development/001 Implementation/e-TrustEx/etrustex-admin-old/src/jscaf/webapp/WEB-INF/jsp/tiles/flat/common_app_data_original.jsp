<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://eu.europa.ec.digit.jscaf/tags" prefix="j" %>


<%-- ===================================================== --%>
<%-- APP MESSAGES for transfering MVC resources i18n keys  --%>
<%-- ===================================================== --%>

<div id="jscaf_app_i18n">

    <li data-key="common.OK" data-value="<j:message key="common.ok"/>"></li>
    <li data-key="common.cancel" data-value="<j:message key="common.cancel"/>"></li>
    
</div>



<%-- ===================================================== --%>
<%-- APPLICATION jSCAF SETTINGS                            --%>
<%-- ===================================================== --%>

<div id="jscaf_app_settings" 

	data-app-version="@app.version@"
	data-app-build-date="@build.date@"
	data-app-build-timestamp="@build.timestamp@"
	data-app-development-build="@app.development.build@"
	data-context-path="<%=request.getContextPath()%>"
	data-language="<j:message key="selected.language"/>"
	data-has-page-inner-fragment=true
	data-is-flat-theme-active=true
	data-is-bootstrap-active=true

></div>


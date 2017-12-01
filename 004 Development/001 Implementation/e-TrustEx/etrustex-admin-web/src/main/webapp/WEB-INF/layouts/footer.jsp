<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@envProperties.getProperty('etrustex.version')" var="version"/>
<spring:eval expression="@envProperties.getProperty('etrustex.environment')" var="environment"/>
<spring:eval expression="@envProperties.getProperty('timestamp')" var="timestamp"/>

<div id="footer" style="text-align:center;">
    <span class="version">version: ${version}</span> - build: ${timestamp} - environment: ${environment}
</div>

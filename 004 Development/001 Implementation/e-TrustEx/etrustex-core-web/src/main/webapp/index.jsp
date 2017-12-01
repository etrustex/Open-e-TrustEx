<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="eu.europa.ec.cipa.etrustex.services.IMonitoringService"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EtrustEx Monitoring Page</title>
    </head>
    <body>
    	<h1>Version: ${etrustex.version}</h1>
        <h1>Build date: ${timestamp}</h1>
        <br/>

<%
ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(application); 
IMonitoringService monitorService = (IMonitoringService)context.getBean("monitoringService");
monitorService.monitor();
%>
		
        
    </body>
</html>

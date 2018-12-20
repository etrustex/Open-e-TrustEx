<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="true" import="java.io.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--@elvariable id="contextPath" type="java.lang.String"--%>

<!doctype html>

<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <!--[if IE]>
    <meta http-equiv="imagetoolbar" content="no">
    <![endif]-->
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width">
    <title>REDIRECT</title>
</head>

<body>
			<c:redirect url="/home.do"/>
			<%-- <a href="<c:url value="/home.do"/>">redirect</a> --%>
</body>
</html>





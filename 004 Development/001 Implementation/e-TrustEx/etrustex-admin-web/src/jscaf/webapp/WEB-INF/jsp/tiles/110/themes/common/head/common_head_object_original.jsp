<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://eu.europa.ec.digit.jscaf/tags" prefix="j" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%--
    <!--[if IE]>
    <script src="<c:url value="/scripts/dev/firebug-lite/build/firebug-lite.js"/>" type="text/javascript"></script>
    <![endif]-->
    --%>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <!--[if IE]>
    <meta http-equiv="imagetoolbar" content="no">
    <![endif]-->
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1">
    <title>MyApp - 1.0.0</title>
    <link rel="SHORTCUT ICON" href="<c:url value="/styles/images/app.ico"/>"/>

    <%-- jSCAF MAIN CSS --%>
    <link rel="stylesheet" href="<c:url value="/styles/reset.css?v=@build.timestamp@"/>" type="text/css" media="screen"/>
    <link rel="stylesheet" href="<c:url value="/styles/fonts.css?v=@build.timestamp@"/>" type="text/css" media="screen"/>
    <link rel="stylesheet" href="<c:url value="/styles/jquery.ui.css?v=@build.timestamp@"/>" type="text/css" media="screen"/>
    <link rel="stylesheet" href="<c:url value="/styles/bootstrap/bootstrap.css?v=@build.timestamp@"/>" type="text/css" media="screen"/>
    <link rel="stylesheet" href="<c:url value="/styles/font_default.css?v=@build.timestamp@"/>" type="text/css" media="screen"/>
    <link rel="stylesheet" href="<c:url value="/styles/icon-fonts.css?v=@build.timestamp@"/>" type="text/css" media="screen"/>
    <link rel="stylesheet" href="<c:url value="/styles/common16.css?v=@build.timestamp@"/>" type="text/css" media="screen"/>

    <link rel="stylesheet" href="<c:url value="/styles/common.app.css?v=@build.timestamp@"/>" type="text/css" media="screen"/>


    <!--jSCAF externals + modules definitions-->
    <script src="<c:url value="/scripts/jquery.js?v=@build.timestamp@"/>"></script>
    <script src="<c:url value="/scripts/bootstrap.light.js?v=@build.timestamp@"/>"></script>
    <script src="<c:url value="/scripts/core.js?v=@build.timestamp@"/>"></script>

    <!--jSCAF CORE + modules definitions-->
    <script src="<c:url value="/scripts/jscaf/modules/ajax.js?v=@build.timestamp@"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/validation.js?v=@build.timestamp@"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/components.js?v=@build.timestamp@"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/core.js?v=@build.timestamp@"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/utils.js?v=@build.timestamp@"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/resources.js?v=@build.timestamp@"/>"></script>

    <%--DEFAULT USAGE--%>
    <script src="<c:url value="/scripts/jscaf/modules/init.js?v=@build.timestamp@"/>"></script>

    <!--IMPORTANT ALWAYS PUT THE MAIN in last position-->
    <script src="<c:url value="/scripts/jscaf/modules/main.js?v=@build.timestamp@"/>"></script>

    <!--APP CONTROLLERS-->
    <script src="<c:url value="/scripts/common.app.js?v=@build.timestamp@"/>" type="text/javascript"></script>

</head>




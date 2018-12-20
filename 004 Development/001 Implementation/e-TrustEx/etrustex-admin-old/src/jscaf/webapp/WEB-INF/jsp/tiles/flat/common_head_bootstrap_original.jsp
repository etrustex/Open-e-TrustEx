<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://eu.europa.ec.digit.jscaf/tags" prefix="j" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
   
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <!--[if IE]>
    <meta http-equiv="imagetoolbar" content="no">
    <![endif]-->
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1">
    <title>@app.name.display@ - @app.version@</title>
    <link rel="SHORTCUT ICON" href="<c:url value="/styles/images/app.ico"/>"/>

    <%-- jSCAF MAIN CSS --%>

    <%-- DEFAULT USAGE --%>
    <link rel="stylesheet" href="<c:url value="/styles/@main.flat.bootstrap.css@"/>" type="text/css" media="screen"/>

    <%-- SPA USAGE 
    <link rel="stylesheet" href="<c:url value="/styles/@main.spa.css@"/>" type="text/css" media="screen"/>
    --%>
    
    <%-- jSCAF MAIN PRINT CSS --%>
    <link rel="stylesheet" href="<c:url value="/styles/@main.print.bootstrap.css@"/>" type="text/css" media="print"/>



    <%-- USE EITHER THE FULL jSCAF BUNDLE : externals + all modules --%>

    <!--jSCAF externals + modules definitions + app controller bundle-->

    <%-- DEFAULT USAGE --%>
    <script src="<c:url value="/scripts/@main.bootstrap.full.js@"/>"></script>

    
    <%-- SPA USAGE 
    <script src="<c:url value="/scripts/@main.spa.full.js@"/>"></script>
    --%>

    <%-- 

    OR USE THIS MODULES-DRIVEN DECLARATION : externals + needed modules

    <!--jSCAF externals-->
    <script src="<c:url value="/scripts/@main.js@"/>"></script>

    <!--jSCAF modules definitions-->
    <script src="<c:url value="/scripts/jscaf/modules/@mod.ajax.js@"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/@mod.browser.js@"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/@mod.components.js@"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/@mod.display.js@"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/@mod.ui-dialog.js@"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/@mod.validation.js@"/>"></script>

    <script src="<c:url value="/scripts/jscaf/modules/@mod.init.js@"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/@mod.core.js@"/>"></script>

    <!--IMPORTANT ALWAYS PUT THE MAIN in last position-->
    <script src="<c:url value="/scripts/jscaf/modules/@mod.main.js@"/>"></script>

    --%>


    <%-- 

    OR USE THIS MODULES-DRIVEN DECLARATION : externals + needed modules  => FOR SPA

    <!--jSCAF externals-->
    <script src="<c:url value="/scripts/@main.js@"/>"></script>

    <!--jSCAF modules definitions-->
    <script src="<c:url value="/scripts/jscaf/modules/@mod.ajax.js@"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/@mod.browser.js@"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/@mod.components.js@"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/@mod.display.js@"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/@mod.ui-dialog.js@"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/@mod.validation.js@"/>"></script>

    <!--SPA modules-->
    <script src="<c:url value="/scripts/jscaf/modules/@mod.spa.js@"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/@mod.spa-init.js@"/>"></script>

    <script src="<c:url value="/scripts/jscaf/modules/@mod.core.js@"/>"></script>

    <!--IMPORTANT ALWAYS PUT THE MAIN in last position-->
    <script src="<c:url value="/scripts/jscaf/modules/@mod.main.js@"/>"></script>

    --%>


</head>




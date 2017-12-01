<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1">
    <title>etrustex-admin-web</title>
    <link rel="SHORTCUT ICON" href="/etrustex-admin-web//styles/images/app.ico"/>

    <%-- jSCAF MAIN CSS --%>
    <link rel="stylesheet" href="/etrustex-admin-web//styles/fonts.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="/etrustex-admin-web//styles/font_default.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="/etrustex-admin-web//styles/icon-fonts.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="/etrustex-admin-web//styles/common16.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="/etrustex-admin-web//styles/cipadmin.css" type="text/css" media="screen"/>
</head>

<body>
    <div id="main">

        <div id="header">
            <div class="logo"></div>
            <div class="logo-text">CIPAdmin</div>
        </div>

        <div id="page-title-wrapper" class="clear">
            <div class="left-content">
                <h1 id="page-title">
                    <fmt:message key="logout.title"/>
                </h1>
            </div>
            <div class="middle-content"> </div>
            <div class="right-content"> </div>
        </div>
        <div class="cl"></div>

        <div id="main-content">
            <div class="columns clear">
                <div class="col-100 clear">
                    <div class="inner-box">
                        <fmt:message key="logout.message"/>
                    </div>
                </div>

            <div class="cl"></div>
        </div>
        <div class="cl"></div>


        <div id="footer" style="text-align:center;">
            <span class="version"></span>
        </div>

        <div class="cl"></div>
    </div>
    <div class="cl"></div>
</body>

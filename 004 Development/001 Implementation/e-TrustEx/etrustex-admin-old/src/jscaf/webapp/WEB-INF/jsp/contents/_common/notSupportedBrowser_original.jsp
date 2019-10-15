<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="@tiles.uri@" prefix="tiles" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

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

    <title>NOT SUPPORTED BROWSER</title>
    <link rel="SHORTCUT ICON" href="<c:url value="/styles/images/app.ico"/>"/>

    <link rel="stylesheet" href="<c:url value="/styles/@main.css@"/>" type="text/css"/>

</head>

<body>

<div id="main">

    <div id="main-content">

        <br><br><br>

        <div id="login-logo"></div>

        <br><br>

        <div class="inner-box" style="margin-left:35%; margin-bottom: 5px; width:350px">
            <br>

            <div class="center">
                <h3 class="text-color-red">Your browser is not supported</h3>
            </div>
            <br>

            <div class="text-color-green center">
                Please upgrade it to be able to use the application again.
            </div>
            <br>
            List of supported browsers :
            <ul class="standard">
                <li>IE8+</li>
                <li>Chrome 3+</li>
                <li>Safari 3+</li>
                <li>Firefox 3+</li>
                <li>Opera 8+</li>
            </ul>
            <br>
        </div>

        <div id="back2homePageButton" style="margin: 30px 0 30px 20px;">
            <a id="app_action_back_home" class="button-link cr-pointer fl" style="margin-left:43%;">
                <span class="button green_button ">
                    <span><span><b class="icon-button icon-login"></b>
                        <em>BACK TO HOME PAGE </em>
                    </span></span>
                </span>
            </a>
        </div>
    


        <br><br><br><br><br><br>


    </div>
</div>

<div class="cl"></div>


<div id="footer">

</div>

</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="@tiles.uri@" prefix="tiles" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>
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

    <title>404</title>
    <link rel="SHORTCUT ICON" href="<c:url value="/styles/images/app.ico"/>"/>

    <link rel="stylesheet" href="<c:url value="/styles/@main.css@"/>" type="text/css"/>

</head>

<body>

<div id="main">

    <div id="main-content">

        <div id="error-content">

            <br><br><br>

            <div id="login-logo"></div>

            <br><br>

            <div class="inner-box" style="margin-left:40%; margin-bottom: 5px; width:250px">
                <br>

                <div class="center">
                    <h3 class="text-color-red">PAGE NOT FOUND</h3>
                </div>
                <br>

                <div class="text-color-red center">
                    Please go back to the Home page
                </div>
                <br>
            </div>

            <div id="back2homePageButton" style="margin: 30px 0 30px 20px;">
                <j:button action_id="app_action_back_home"
                              link_style="margin-left:43%;"
                              button_icon_class="icon-login"
                              label_text="BACK TO HOME PAGE"/>
            </div>

            <br><br><br><br>

        </div>

    </div>
</div>

<div class="cl"></div>


<div id="footer">

</div>

</body>
</html>
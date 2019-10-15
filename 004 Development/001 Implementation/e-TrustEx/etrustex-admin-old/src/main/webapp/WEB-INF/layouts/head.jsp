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
    <title>etrustex-admin-web - 1.0.0</title>
    <%-- <link rel="SHORTCUT ICON" href="<c:url value="/styles/images/app.ico"/>"/> --%>

    <%-- TODO jSCAF MAIN CSS version--%>
    <link rel="stylesheet" href="<c:url value="/styles/reset.css"/>" type="text/css" media="screen"/>
    <link rel="stylesheet" href="<c:url value="/styles/fonts.css"/>" type="text/css" media="screen"/>
    <link rel="stylesheet" href="<c:url value="/styles/jquery.ui.css"/>" type="text/css" media="screen"/>
    <link rel="stylesheet" href="<c:url value="/styles/bootstrap/bootstrap.css"/>" type="text/css" media="screen"/>
    <link rel="stylesheet" href="<c:url value="/styles/font_default.css"/>" type="text/css" media="screen"/>
    <link rel="stylesheet" href="<c:url value="/styles/font_min.css"/>" type="text/css" media="screen"/>
    <link rel="stylesheet" href="<c:url value="/styles/font_max.css"/>" type="text/css" media="screen"/>
    <link rel="stylesheet" href="<c:url value="/styles/icon-fonts.css"/>" type="text/css" media="screen"/>

    <link rel="stylesheet" href="<c:url value="/styles/common16.css"/>" type="text/css" media="screen"/>
    <link rel="stylesheet" href="<c:url value="/styles/cipadmin.css"/>" type="text/css" media="screen"/>

    <%-- jSCAF MAIN PRINT CSS --%>
    <link rel="stylesheet" href="<c:url value="/styles/reset.css"/>" type="text/css" media="print"/>
    <link rel="stylesheet" href="<c:url value="/styles/jquery.ui.css"/>" type="text/css" media="print"/>
    <link rel="stylesheet" href="<c:url value="/styles/font_default.css"/>" type="text/css" media="print"/>
    <link rel="stylesheet" href="<c:url value="/styles/print.css"/>" type="text/css" media="print"/>
    <link rel="stylesheet" href="<c:url value="/styles/bootstrap-datetimepicker.css"/>" type="text/css"/>
    <link rel="stylesheet" href="<c:url value="/styles/bootstrap-datetimepicker-standalone.css"/>" type="text/css"/>
    <link rel="stylesheet" href="<c:url value="/styles/font-awesome.min.css"/>" type="text/css"/>


    <!--jSCAF externals + modules definitions-->
    <script src="<c:url value="/scripts/jquery.js"/>"></script>
    <script src="<c:url value="/scripts/bootstrap.full.js"/>"></script>
    <script src="<c:url value="/scripts/core.js"/>"></script>

    <!--jSCAF CORE + modules definitions-->
    <script src="<c:url value="/scripts/jscaf/modules/display.js"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/ajax.js"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/validation.js"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/ui-dialog.js"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/components.js"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/browser.js"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/core.js"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/utils.js"/>"></script>
    <script src="<c:url value="/scripts/jscaf/modules/resources.js"/>"></script>
    <script src="<c:url value="/scripts/bootstrap-datetimepicker.min.js"/>"></script>

    <%--DEFAULT USAGE --%>
    <script src="<c:url value="/scripts/jscaf/modules/init.js"/>"></script>

    <%-- ============jSCAF APP SETTINGS============= --%>
    <jsp:include page="common_app_settings.jsp"/>
    <%-- =========================================== --%>

    <%-- ============jSCAF APP MESSAGES ============= --%>
    <jsp:include page="common_app_messages.jsp"/>
    <%-- =========================================== --%>


    <!--IMPORTANT ALWAYS PUT THE MAIN in last position-->
    <script src="<c:url value="/scripts/jscaf/modules/main.js"/>"></script>


    <!-- APP CONTROLLERS -->
    <script src="<c:url value="/scripts/common.cipadmin.js"/>" type="text/javascript"></script>

</head>




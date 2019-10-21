<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="@tiles.uri@" prefix="tiles" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<!--[if IE 7]><html class="ie ie7" lang="en"> <![endif]-->
<!--[if IE 8]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if IE 9]><html class="ie ie9" lang="en"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--><html lang="en"> <!--<![endif]-->

<%-- ==========HEAD============= --%>
<tiles:@tiles.insert.attribute@="head"/>
<%-- =========================== --%>

<body>
<div id="main" class="hidden">
    <%-- ==========HEADER================ --%>
    <tiles:@tiles.insert.attribute@="header"/>
    <%-- ================================ --%>
    <br>
    <div id="main-content">
        <%-- ===========BODY================= --%>
        <tiles:@tiles.insert.attribute@="pageContent"/>
        <tiles:@tiles.insert.attribute@="pagei18n" ignore="true"/>
        <%-- ================================ --%>
        <div class="cl"></div>
    </div>
    <div class="cl"></div>
    <%-- ============FOOTER============== --%>
    <tiles:@tiles.insert.attribute@="footer"/>
    <%-- ================================ --%>
    <div class="cl"></div>
</div>
<div class="cl"></div>
</body>
<%-- ============SCRIPTS============= --%>
<jsp:include page="/WEB-INF/jsp/tiles/flat/common_scripts.jsp"/>
<%-- ================================ --%>

<%-- ===== jSCAF APP SETTINGS and i18n RESOURCES ====== --%>
<jsp:include page="/WEB-INF/jsp/tiles/flat/common_app_data.jsp"/>   
<%-- ================================================== --%>


</html>

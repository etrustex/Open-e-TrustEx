<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>
<%@ taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>

<tilesx:useAttribute name="pageTitle" classname="java.lang.String" scope="request" ignore="true"/>
<tilesx:useAttribute name="pageIconClass" classname="java.lang.String" scope="request" ignore="true"/>

<tilesx:useAttribute name="pageHelpTitle" classname="java.lang.String" scope="request" ignore="true"/>
<tilesx:useAttribute name="pageHelpPdfFilename" classname="java.lang.String" scope="request" ignore="true"/>

<!--[if IE 7]><html class="ie ie7" lang="en"> <![endif]-->
<!--[if IE 8]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if IE 9]><html class="ie ie9" lang="en"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--><html lang="en"> <!--<![endif]-->


<%-- TODO Remove this global var. Temporary fix while $._getContextPath() returns '..'--%>
<script type="text/javascript">
    var contextPath = '<%=request.getContextPath()%>';
</script>

<%-- ==========HEAD============= --%>
<tiles:insertAttribute name="head"/>
<%-- =========================== --%>

<body>
<div id="main">
    <%-- ==========HEADER================ --%>
    <tiles:insertAttribute name="header"/>
    <%-- ================================ --%>
    <div id="page-title-wrapper" class="clear">
        <div class="left-content">
            <h1 id="page-title">
                <j:bean_message message_key="${pageTitle}"/>
            </h1>
            <tiles:insertAttribute name="pageHelpButton" ignore="true"/>
        </div>
        <div class="middle-content">
            <tiles:insertAttribute name="pageMiddleContentTop" ignore="true"/>
        </div>
        <div class="right-content">
            <tiles:insertAttribute name="pageRightContentTop" ignore="true"/>
        </div>
    </div>
    <div class="cl"></div>
    <div id="main-content">
	    <%-- ===========BODY================= --%>
        <tiles:insertAttribute name="pageContent"/>
        <%-- ================================ --%>
        <div class="cl"></div>
    </div>
    <div class="cl"></div>
    <%-- ============FOOTER============== --%>
    <tiles:insertAttribute name="footer"/>
    <%-- ================================ --%>
    <div class="cl"></div>
</div>
<div class="cl"></div>
</body>
<%-- ============SCRIPTS============= --%>
<jsp:include page="/WEB-INF/layouts/common_scripts.jsp"/>

</html>

<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="@tiles.uri@" prefix="tiles" %>
<%@ taglib uri="http://eu.europa.ec.digit.jscaf/tags" prefix="j" %>

<tiles:useAttribute name="pageDefaultHiddenClass" classname="java.lang.String" scope="request" ignore="true"/>

<body>
<div id="main">
    <%-- ==========HEADER================ --%>
    <tiles:@tiles.insert.attribute@="header"/>
    <%-- ================================ --%>
    <br><br>

    <div id="main-content" class="<%=pageDefaultHiddenClass%>">
        <%-- ===========BODY================= --%>
        <tiles:@tiles.insert.attribute@="pageContent"/>
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
<jsp:include page="common_scripts.jsp"/>
<%-- ================================ --%>

<%-- ===== jSCAF APP SETTINGS and i18n RESOURCES ====== --%>
<jsp:include page="common_app_data.jsp"/>   
<%-- ================================================== --%>



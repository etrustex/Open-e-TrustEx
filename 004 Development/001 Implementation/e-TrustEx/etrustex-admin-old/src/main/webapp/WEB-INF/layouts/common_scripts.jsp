<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>


<%-- ================== --%>
<%-- PAGE CONTROLLER JS --%>
<%-- ================== --%>
<tilesx:useAttribute name="pageScript" classname="java.lang.String" scope="request" ignore="true"/>

<%
    if (null==pageScript || pageScript.equals("")) {
        pageScript = "/scripts/jscaf/_templates/genericPageController.js";
    }
%>
<script id="page_script" src="<c:url value="<%=pageScript%>"/>?v=20170906162853762" type="text/javascript"></script>


<%-- ================================ --%>
<%-- OPTIONAL JS / CSS                --%>
<%-- ================================ --%>
<tilesx:useAttribute name="use_datatables" classname="java.lang.String" scope="request" ignore="true"/>
<c:if test="${not empty use_datatables}">
    <link rel="stylesheet" href="<c:url value="/plugins/datatables/datatables.css?v=20170906162853762"/>" type="text/css"/>
    <link rel="stylesheet" href="<c:url value="/plugins/datatables/datatables.custom.css?v=20170906162853762"/>" type="text/css"/>
    <script src="<c:url value="/plugins/datatables/dataTables-tableTools.min.js?v=20170906162853762"/>" type="text/javascript"></script>
    <script src="<c:url value="/plugins/datatables/dataTables.init?v=20170906162853762.js"/>" type="text/javascript"></script>
</c:if>


<tilesx:useAttribute name="use_highcharts" classname="java.lang.String" scope="request" ignore="true"/>
<c:if test="${not empty use_highcharts}">
    <script src="<c:url value="/plugins/highcharts/highcharts.js?v=20170906162853762"/>" type="text/javascript"></script>
    <script src="<c:url value="/plugins/highcharts/highcharts.init.js?v=20170906162853762"/>" type="text/javascript"></script>
</c:if>


<tilesx:useAttribute name="use_jstree" classname="java.lang.String" scope="request" ignore="true"/>
<c:if test="${not empty use_jstree}">
    <script src="<c:url value="/plugins/jsTree/jquery.jstree.js?v=20170906162853762"/>" type="text/javascript"></script>
</c:if>


<tilesx:useAttribute name="use_select2" classname="java.lang.String" scope="request" ignore="true"/>
<c:if test="${not empty use_select2}">
    <link rel="stylesheet" href="<c:url value="/plugins/select2/select2.css?v=20170906162853762"/>" type="text/css"/>
    <script src="<c:url value="/plugins/select2/select2.min.js?v=20170906162853762"/>" type="text/javascript"></script>
</c:if>


<%-- JSCAF PLUGINS --%>
<%-- --%>
<script src="<c:url value="/scripts/jscaf/plugins/JSinlineEdit.js?v=20170906162853762"/>"></script>


<%-- ========== --%>
<%-- REQUIRE JS  --%>
<%-- ========== --%>
<%--
RequireJS MUST be kept after datatables declaration !!! conflict when declared before !!!
[base] module should be handled and configured inside the linked page JS
EVEN IF THE IDE COMPLAINS : DON'T PUT ANY TYPE DEFINITION FOR THIS SCRIPT !!!
--%>
<script src="<c:url value="/plugins/require.js?v=20170906162853762"/>"></script>
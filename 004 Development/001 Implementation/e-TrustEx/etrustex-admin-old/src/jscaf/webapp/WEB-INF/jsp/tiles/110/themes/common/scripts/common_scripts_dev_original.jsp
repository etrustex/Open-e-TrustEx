<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="@tiles.uri@" prefix="tiles" %>
<%@ taglib uri="http://eu.europa.ec.digit.jscaf/tags" prefix="j" %>

<%-- jSCAF 1.10+ --%>

<%-- ================== --%>
<%-- PAGE CONTROLLER JS --%>
<%-- ================== --%>
<tiles:useAttribute name="pageScript" classname="java.lang.String" scope="request" ignore="true"/>
<tiles:useAttribute name="defaultPagePath" classname="java.lang.String" scope="request" ignore="true"/>
<tiles:useAttribute name="module" classname="java.lang.String" scope="request" ignore="true"/>
<tiles:useAttribute name="pageName" classname="java.lang.String" scope="request" ignore="true"/>
<tiles:useAttribute name="isSpaContent" classname="java.lang.String" scope="request" ignore="true"/>

<% if (null == isSpaContent) { %>

	<%
	    String s = module;
	    String moduleOut = s.replaceAll("\\.", "/");
	    String pageScriptUrl;
		if (null==pageScript || pageScript.equals("")) {
		    pageScriptUrl = "/pagescripts/" + moduleOut + "/" + pageName + ".js?v=@build.timestamp@";
		} else {
			pageScriptUrl = pageScript + "?v=@build.timestamp@";
		}

	%>
	
	<script id="page_script" src="<c:url value="<%=pageScriptUrl%>"/>" type="text/javascript"></script>
		
<% } %>




<%-- ================================ --%>
<%-- OPTIONAL JS / CSS                --%>
<%-- ================================ --%>
<tiles:useAttribute name="use_datatables" classname="java.lang.String" scope="request" ignore="true"/>
<c:if test="${not empty use_datatables}">
	<link rel="stylesheet" href="<c:url value="/plugins/datatables/datatables.css?v=@build.timestamp@"/>" type="text/css"/>
	<link rel="stylesheet" href="<c:url value="/plugins/datatables/datatables.custom.css?v=@build.timestamp@"/>" type="text/css"/>
	<script src="<c:url value="/plugins/datatables/dataTables-tableTools.min.js?v=@build.timestamp@"/>" type="text/javascript"></script>
	<script src="<c:url value="/plugins/datatables/dataTables.init?v=@build.timestamp@.js"/>" type="text/javascript"></script>
</c:if>


<tiles:useAttribute name="use_highcharts" classname="java.lang.String" scope="request" ignore="true"/>
<c:if test="${not empty use_highcharts}">
	<script src="<c:url value="/plugins/highcharts/highcharts.js?v=@build.timestamp@"/>" type="text/javascript"></script>
	<script src="<c:url value="/plugins/highcharts/highcharts.init.js?v=@build.timestamp@"/>" type="text/javascript"></script>
</c:if>


<tiles:useAttribute name="use_jstree" classname="java.lang.String" scope="request" ignore="true"/>
<c:if test="${not empty use_jstree}">
	<script src="<c:url value="/plugins/jsTree/jquery.jstree.js?v=@build.timestamp@"/>" type="text/javascript"></script>
</c:if>


<tiles:useAttribute name="use_select2" classname="java.lang.String" scope="request" ignore="true"/>
<c:if test="${not empty use_select2}">
	<link rel="stylesheet" href="<c:url value="/plugins/select2/select2.css?v=@build.timestamp@"/>" type="text/css"/>
	<script src="<c:url value="/plugins/select2/select2.min.js?v=@build.timestamp@"/>" type="text/javascript"></script>
</c:if>


<%-- ============APP SCRIPTS================ --%>
<jsp:include page="common_app_scripts.jsp"/>
<%-- ======================================= --%>


<%-- JSCAF PLUGINS --%>
<%-- --%>
<script src="<c:url value="/scripts/jscaf/plugins/JSinlineEdit.js?v=@build.timestamp@"/>"></script>


<%-- ========== --%>
<%-- REQUIRE JS  --%>
<%-- ========== --%>
<%--
RequireJS MUST be kept after datatables declaration !!! conflict when declared before !!!
[base] module should be handled and configured inside the linked page JS
EVEN IF THE IDE COMPLAINS : DON'T PUT ANY TYPE DEFINITION FOR THIS SCRIPT !!!
--%>
<script src="<c:url value="/plugins/require.js?v=@build.timestamp@"/>"></script>
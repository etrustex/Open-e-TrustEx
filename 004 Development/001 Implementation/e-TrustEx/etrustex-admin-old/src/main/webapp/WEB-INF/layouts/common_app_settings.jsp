<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<%-- ===================================================== --%>
<%-- APPLICATION jSCAF SETTINGS                            --%>
<%-- ===================================================== --%>

<script type="text/javascript">

	
    $._settings = $.extend( $._settings, {

       	appName: 'CIPADMIN',
        appVersion: '@app.version@',
        appBuildDate: '@build.date@',
		appBuildTimeStamp: '@build.timestamp@',
       	contextPath: '<%=request.getContextPath()%>',
        //language: '<j:bean_message message_key="selected.language"/>',
        language: '<%=request.getLocale()%>', //$("#language").val(),
        hasPageInnerFragment: true,
        componentsBaseUrl: '/pagescripts',
        ecasRedirectFailedUrl: '/outOfSession.do'
    });    

</script>


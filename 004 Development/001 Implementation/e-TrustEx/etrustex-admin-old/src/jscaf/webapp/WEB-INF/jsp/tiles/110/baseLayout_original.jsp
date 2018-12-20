<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="@tiles.uri@" prefix="tiles" %>
<%@ taglib uri="http://eu.europa.ec.digit.jscaf/tags" prefix="j" %>

<%-- jSCAF 1.10+ --%>

<tiles:useAttribute name="isPageTitleHidden" classname="java.lang.String" scope="request" ignore="true"/>
<tiles:useAttribute name="isCloseButtonDisplayed" classname="java.lang.String" scope="request" ignore="true"/>  
<tiles:useAttribute name="pageTitle" classname="java.lang.String" scope="request" ignore="true"/>
<tiles:useAttribute name="pageIconClass" classname="java.lang.String" scope="request" ignore="true"/>
<tiles:useAttribute name="pageHelpTitle" classname="java.lang.String" scope="request" ignore="true"/>
<tiles:useAttribute name="pageHelpPdfFilename" classname="java.lang.String" scope="request" ignore="true"/>
<tiles:useAttribute name="themeCssClass" classname="java.lang.String" scope="request" ignore="true"/>

<tiles:useAttribute name="defaultPagePath" classname="java.lang.String" scope="request" ignore="true"/>
<tiles:useAttribute name="module" classname="java.lang.String" scope="request" ignore="true"/>
<tiles:useAttribute name="pageName" classname="java.lang.String" scope="request" ignore="true"/>

<tiles:useAttribute name="isSpaContent" classname="java.lang.String" scope="request" ignore="true"/>
<tiles:useAttribute name="spaRootUrl" classname="java.lang.String" scope="request" ignore="true"/>


<!--[if IE 7]><html class="ie ie7"> <![endif]-->
<!--[if IE 8]><html class="ie ie8"> <![endif]-->
<!--[if IE 9]><html class="ie ie9"> <![endif]-->
<!--[if IE 10]><html class="ie ie10"> <![endif]-->
<!--[if IE 11]><html class="ie ie11"> <![endif]-->
<!--[if (gt IE 11)|!(IE)]><!--><html> <!--<![endif]-->

<%-- ==========HEAD============= --%>
<tiles:@tiles.insert.attribute@="head"/>
<%-- =========================== --%>

<body class="<%=themeCssClass%>">


<div id="main" class="clear hidden">
    <%-- ==========HEADER================ --%>
    <tiles:@tiles.insert.attribute@="header"/>
    <%-- ================================ --%>


    <% if (null != isSpaContent && isSpaContent.equals("true")) { %>


        <div id="main-content">
           <%
               String spaContentUrl = spaRootUrl + "/app.jsp";
            %>
           <jsp:include page="<%=spaContentUrl%>"/>
        </div>



    <% } else { %>

        <%
            String moduleOut = module;
            String pageNameOut = pageName;
            
            moduleOut = moduleOut.replaceAll("\\.", "/");
            pageNameOut = pageNameOut.replaceAll("\\.", "/");
            
            String pageUrl = defaultPagePath + "/" + moduleOut + "/" + pageNameOut + ".jsp";
            String pageI18nUrl = defaultPagePath + "/" + moduleOut + "/" + pageNameOut + "-i18n.jsp";

            if (null==pageTitle || pageTitle.equals("")) {
                pageTitle = "page.title." + module + "." + pageName;
            }

            if (null==pageIconClass || pageIconClass.equals("")) {
                pageIconClass = "icon-page-" + module + "-" + pageName;
            }
        %>


        <% if (null==isPageTitleHidden || isPageTitleHidden.equals("")) { %>

            <div id="page-title-wrapper" class="clear">
                <div class="left-content">
                    <span class="icon-page-title <%=pageIconClass%>"></span>

                    <h1 id="page-title">
                        <j:message key="<%=pageTitle%>"/>
                    </h1>
                    <tiles:insertAttribute name="pageHelpButton" ignore="true"/>
                </div>
                <div class="middle-content">
                    <tiles:insertAttribute name="pageMiddleContentTop" ignore="true"/>
                </div>
                <div class="right-content">
                    <% if (null != isCloseButtonDisplayed && isCloseButtonDisplayed.equals("true")) { %> 
                        <j:button action_class="action_close"
                                  link_style="padding-top:8px"
                                  button_type_class="gray_button"
                                  button_icon_class="icon-close"
                                  button_optional_class="alternate last-right"
                                  tooltip_key="common.close.tooltip"
                                  label_key="common.close.UPPER"/>
                    <% } %>

                    <tiles:insertAttribute name="pageRightContentTop" ignore="true"/>
                </div>
            </div>

        <% } %>



        <%-- ============PAGE CONTENT============== --%>
        <div id="main-content" class="clear">
            <jsp:include page="<%=pageUrl%>"/>

            <%-- optional i18n page linked to the current page --%>
            <% try { %>
                <jsp:include page="<%=pageI18nUrl%>"/>
            <% } catch(Exception e) {} %>         
        </div>
        <%-- ====================================== --%>

    <% } %>


    <%-- ============FOOTER============== --%>
    <tiles:@tiles.insert.attribute@="footer"/>
    <%-- ================================ --%>
</div>

</body>



<%-- ============SCRIPTS============= --%>
<tiles:@tiles.insert.attribute@="scripts"/>
<%-- ================================ --%>

<%-- ===== jSCAF APP SETTINGS and i18n RESOURCES ====== --%>
<jsp:include page="common_app_data.jsp"/>   
<%-- ================================================== --%>

</html>

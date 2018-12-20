<%@ tag language="java" isELIgnored="false" body-content="scriptless" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<%@ tag description="Generate box" %>

<%@ attribute name="box_id"                 required="true"  type="java.lang.String" description="" %>
<%@ attribute name="is_expanded"            required="false" type="java.lang.String" description="" %>
<%@ attribute name="is_auto_triggered"      required="false" type="java.lang.String" description="" %>
<%@ attribute name="box_title_key"          required="false" type="java.lang.String" description="" %>
<%@ attribute name="box_title_text"         required="false" type="java.lang.String" description="" %>
<%@ attribute name="box_title_class"        required="false" type="java.lang.String" description="" %>
<%@ attribute name="box_icon_class"         required="false" type="java.lang.String" description="" %>
<%@ attribute name="box_icon_action_class"  required="false" type="java.lang.String" description="" %>
<%@ attribute name="box_optional_class"     required="false" type="java.lang.String" description="" %>
<%@ attribute name="header_optional_class"  required="false" type="java.lang.String" description="" %>
<%@ attribute name="content_optional_class" required="false" type="java.lang.String" description="" %>
<%@ attribute name="data_attr_map"          required="false" type="java.lang.String" description="" %>

<%
    String dataAttributes = "";
    request.setAttribute("dataAttributes",dataAttributes);


    String state = "collapsed";
    if (null != is_expanded && is_expanded.equals("true")) {
        state = "expanded";
    }
    request.setAttribute("state",state);

    String trigger = "";
    if (null != is_auto_triggered && is_auto_triggered.equals("true")) {
        trigger = "JS_accordion_box_auto_triggered";
    }
    request.setAttribute("trigger",trigger);

%>

<div id="${box_id}" class="box-wrapper accordion-box ${state} ${box_optional_class}" ${dataAttributes}>
    <div class="box">
        <div class="header ${header_optional_class} ${trigger} clear">
            <h2 class="${box_title_class}">
                <% if (null != box_title_key && !box_title_key.equals("")) { %>
                    <j:bean_message message_key="${box_title_key}"/>
                <% } else { %>
                    ${box_title_text}
                <% } %>
            </h2>
            <% if (null != box_icon_action_class && !box_icon_action_class.equals("")) { %>
                <div class="header-right">
                    <a class="${box_icon_action_class}">
                        <% if (null!=box_icon_action_class && !box_icon_action_class.equals("")) { %>
                            <i class="icon${box_icon_class} size24 white"></i>
                        <% } else { %>
                            <i class="icon icon-ft-help size24 white"></i>
                        <% } %>
                    </a>
                </div>
            <% } %>
        </div>
        <div id="<%=box_id + "_content"%>" class="content clear ${content_optional_class}">
            <jsp:doBody/>
        </div>
    </div>
</div>
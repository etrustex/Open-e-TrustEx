
<%@ tag language="java" isELIgnored="false" body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<%@ attribute name="search_action_id"     required="true" type="java.lang.String" description="" %>
<%@ attribute name="search_action_key"    required="false" type="java.lang.String" description="" %>
<%@ attribute name="search_action_text"   required="false" type="java.lang.String" description="" %>
<%@ attribute name="search_action_reset_id"   required="false" type="java.lang.String" description="" %>
<%@ attribute name="search_action_reset_key"   required="false" type="java.lang.String" description="" %>
<%@ attribute name="search_action_reset_text"   required="false" type="java.lang.String" description="" %>


<div class="inner-box blue clear">

    <jsp:doBody/>

</div>


<% if (null!=search_action_key && !search_action_key.equals("")) { %>
    <j:button action_id="${search_action_id}"
          button_icon_class="icon-search"
          label_key="${search_action_key}"/>
<% } else { %>
    <j:button action_id="${search_action_id}"
          button_icon_class="icon-search"
          label_text="${search_action_text}"/>
<% } %>

<a id="${search_action_reset_id}" class="cr-pointer fl button-secondary-link">
    <% if (null!=search_action_reset_key && !search_action_reset_key.equals("")) { %>
        <j:bean_message message_key="${search_action_reset_key}"/>
    <% } else { %>
        ${search_action_reset_text}
    <% } %>
</a>


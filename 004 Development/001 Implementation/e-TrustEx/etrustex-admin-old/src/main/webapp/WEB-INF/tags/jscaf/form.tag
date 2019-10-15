<%@ tag language="java" isELIgnored="false" body-content="scriptless" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<%@ attribute name="id" required="true" type="java.lang.String" description="" %>
<%@ attribute name="baseUrl" required="true" type="java.lang.String" description="" %>
<%@ attribute name="hasInnerFragment" required="false" type="java.lang.String" description="" %>


<form modelAttribute="${id}" id="${id}" action="${baseUrl}" accept-charset="UTF-8">
    <% if (null!=hasInnerFragment && !hasInnerFragment.equals("")) { %>
        <div id="innerFragment"></div>
    <% } else { %>
        <jsp:doBody/>
    <% } %>
</form>

        

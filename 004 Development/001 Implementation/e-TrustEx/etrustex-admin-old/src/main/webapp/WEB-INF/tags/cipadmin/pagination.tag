<%@ tag language="java" isELIgnored="false" body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib tagdir="/WEB-INF/tags/jscaf" prefix="j" %>

<%@ attribute name="maxPages" required="true" type="java.lang.Integer" description="The maximum number of pages available (ie tableRecordCount / size)" %>
<%@ attribute name="functionName" type="java.lang.String" required="true"  rtexprvalue="true" description="the name of the js search function" %>
<%@ attribute name="page" type="java.lang.Integer" required="true" rtexprvalue="true" description="The current page (not required, defaults to 1)" %>
<%@ attribute name="size" type="java.lang.Integer" required="true" rtexprvalue="true" description="The number of records per page (not required, defaults to 10)" %>


<c:if test="${empty page || page lt 1}">
  <c:set var="page" value="1" />
</c:if>

<c:if test="${empty size || size lt 1}">
  <c:set var="size" value="50" />
</c:if>

   
<div class="pagination-footer">
    <spring:message code="list_page" arguments="${page},${maxPages}" argumentSeparator="," />
    <c:out value=".	"/>
    <spring:message code="list_size" var="list_size" htmlEscape="false" />
    <c:out value="${list_size}"/>
  	
    <c:forEach var="i" items="10,25,50">
	      <c:choose>
	        <c:when test="${size == i}">
	          <c:out value="${i}" />
	        </c:when>
	        <c:otherwise>
	          <a class="list_size_url">${i}</a>
	        </c:otherwise>
	      </c:choose>
	      <c:out value=" " />
    </c:forEach>
	<div class="pagination pagination-right">
	   	<ul>
		    <c:if test="${page ne 1}">
		      <li><a class="select-log" id="first_page_url">&lt; &lt;</a></li>
		    </c:if>
		    <c:if test="${page gt 1}">
		      <li><a class="select-log" id="previous_page_url">&lt;</a></li>
		    </c:if>
		    <c:if test="${page lt maxPages}">
		      <li><a class="select-log" id="next_page_url">&gt;</a></li>
		    </c:if>
		    <c:if test="${page ne maxPages}">
		      <li><a class="select-log" id="last_page_url">&gt; &gt;</a></li>
		    </c:if>
	   	</ul>
	</div>
	
</div>

<script>
	var page = ${page};
	var size = ${size};
	var maxPages = ${maxPages};
	var fnName = '${functionName}';
	
	$._AE('click','#first_page_url', function() {
		CIPADMIN.executeFnByName(fnName, window, 1, size);
	});
	
	$._AE('click','#previous_page_url', function() {
		CIPADMIN.executeFnByName(fnName, window, page - 1, size);
	});
	
	$._AE('click','#next_page_url', function() {
		CIPADMIN.executeFnByName(fnName, window, page + 1, size);
	});
	
	$._AE('click','#last_page_url', function() {
		CIPADMIN.executeFnByName(fnName, window, maxPages, size);
	});
	
	$._AE('click','.list_size_url', function() {
		CIPADMIN.executeFnByName(fnName, window, 1, $(this).text());
	});
</script>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/jscaf" prefix="j"%>

 <j:box box_title_key="common.system.error.title">
	<h6>${message}</h6>
    <div class="hidden">
      Failed URL: ${url}
      <br/>
      <c:out value="${exception.class.name}"/>: <c:out value="${exception.message}"/>
      <br/>
      <c:forEach items="${exception.stackTrace}" var="ste">
          <c:out value="${ste}" />
      </c:forEach>
    </div>
</j:box>
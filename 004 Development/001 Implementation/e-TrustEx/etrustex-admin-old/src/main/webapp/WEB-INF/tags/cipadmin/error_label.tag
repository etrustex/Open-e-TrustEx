<%@ tag language="java" isELIgnored="false" body-content="scriptless" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ attribute name="fieldPath" type="java.lang.String" required="true" description="" %>

<spring:bind path = "${fieldPath}">
    <c:if test="${status.error}"> 
        <div class="fl" style="margin-top:0;">
			<div class="validation box">
				<p>${status.errorMessage}</p>
			</div>
		</div>
    </c:if> 
</spring:bind> 


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<input type="hidden" id="error" value="${error}" />
<input type="hidden" id="isLogged" value="${isLogged}" />
<input type="hidden" id="isAuthorized" value="${isAuthorized}" />
<form method="post" action="j_security_check">
	<div id="usernameDiv" class="field required">
		<div class="field-label">
			<h6>
				<fmt:message key="login.username" />:
			</h6>
		</div>
		<input type="text" name="j_username" class="field-value JS_live-validation JS_show_validation_error "/>
		<span class="required-toggle required-required"></span>
	</div>
	<br/>
	
	<div id="passwordDiv" class="field required">
		<div class="field-label">
			<h6>
				<fmt:message key="login.password" />:
			</h6>
		</div>
		<input type="password" name="j_password" class="field-value JS_live-validation JS_show_validation_error" />
		<span class="required-toggle required-required"></span>
	</div>

	<div class="cl"></div>
    <input type="submit" value="Login" class="alternate blue_button"/>
    <%--<div id="innerFragment"></div>
    <div id="businessDomainInnerFragment"></div>--%>
</form>


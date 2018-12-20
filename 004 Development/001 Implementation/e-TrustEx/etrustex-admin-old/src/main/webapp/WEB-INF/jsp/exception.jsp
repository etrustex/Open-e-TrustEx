<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib tagdir="/WEB-INF/tags/jscaf" prefix="j"%>

<j:box box_title_key="common.system.error">
	<div class="text-color-red left clear">
		${exceptionMessage}
	</div>
</j:box>



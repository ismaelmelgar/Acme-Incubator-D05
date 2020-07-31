<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:list>
	<acme:list-column code="administrator.bookkeeper.list.label.username" path="authenticated.userAccount.username" width="40%" />
	<acme:list-column code="administrator.bookkeeper.list.label.firm" path="firm" width="20%" />
	<acme:list-column code="administrator.bookkeeper.list.label.responsibilityStatement" path="responsibilityStatement" width="80%" />

</acme:list>
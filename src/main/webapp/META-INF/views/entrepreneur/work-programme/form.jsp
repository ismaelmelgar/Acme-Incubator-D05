
<%--
- form.jsp
-
- Copyright (c) 2019 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>




<acme:form>

	<acme:form-hidden path="investmentRoundId"/>
	<acme:form-hidden path="status"/>
	
	<acme:form-textbox code="entrepreneur.workProgramme.label.title" path="title"/>
	<jstl:if test="${command != 'create'}">
		<acme:form-moment code="entrepreneur.workProgramme.label.start" path="start" readonly="true" />
	</jstl:if>
	<acme:form-moment code="entrepreneur.workProgramme.label.end" path="end"/>
	<acme:form-money code="entrepreneur.workProgramme.label.budget" path="budget"/>
	

	<acme:form-submit test="${command == 'show' && irStatus == false}"
		code="entrepreneur.workProgramme.button.update" 
		action="/entrepreneur/work-programme/update"/>
		
	<acme:form-submit test="${command == 'show' && irStatus == false}"
		code="entrepreneur.workProgramme.button.delete" 
		action="/entrepreneur/work-programme/delete"/>

	<acme:form-submit test="${command == 'update' && irStatus == false}"
		code="entrepreneur.workProgramme.button.update" 
		action="/entrepreneur/work-programme/update"/>
	
	<acme:form-submit test="${command == 'create' }"
		code="entrepreneur.workProgramme.button.create" 
		action="/entrepreneur/work-programme/create"/>
		
	<acme:form-submit test="${command == 'delete' && irStatus == false}"
		code="entrepreneur.workProgramme.button.delete" 
		action="/entrepreneur/work-programme/delete"/>

	<acme:form-return code="entrepreneur.workProgramme.button.return"/>
</acme:form>
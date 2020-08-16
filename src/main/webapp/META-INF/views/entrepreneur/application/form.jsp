
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
	<jstl:if test="${command!='update'}">
		<acme:form-textbox code="entrepreneur.application.label.ticker" path="ticker" readonly="true" />
		<acme:form-textbox code="entrepreneur.application.label.creationMoment" path="creationMoment" readonly="true" />
		<acme:form-textbox code="entrepreneur.application.label.statement" path="statement" readonly="true" />
		<acme:form-money code="entrepreneur.application.label.moneyOffer" path="moneyOffer" readonly="true" />
		<acme:form-textbox code="entrepreneur.application.label.tickerInvestmentRound" path="investmentRound.ticker" readonly="true" />
		<acme:form-textbox code="entrepreneur.application.label.investorName" path="investor.identity.fullName" readonly="true" />
	</jstl:if>

	<acme:form-select code="entrepreneur.application.form.label.status" path="status">
		<jstl:if test="${command!='update'}">
			<option value="Pending" <jstl:if test="${status =='Pending'}">selected</jstl:if>>Pending</option>
		</jstl:if>
		<option value="Accepted" <jstl:if test="${status =='Accepted'}">selected</jstl:if>>Accepted</option>
		<option value="Rejected" <jstl:if test="${status =='Rejected'}">selected</jstl:if>>Rejected</option>
	</acme:form-select>
	<jstl:if test="${status=='Rejected' || command=='update'}">
		<acme:form-textarea code="entrepreneur.application.form.label.reason" path="reason" />
	</jstl:if>
	
	<acme:form-submit test="${command == 'show' && status=='Pending'}" code="entrepreneur.application.form.button.publish"
		action="/entrepreneur/application/update?id=${id}" method="get" />
	<acme:form-submit test="${command == 'update'}" code="entrepreneur.application.form.button.update"
		action="/entrepreneur/application/update" />

	<acme:form-return code="entrepreneur.application.button.return" />
</acme:form>
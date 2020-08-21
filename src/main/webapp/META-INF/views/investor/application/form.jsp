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
	<acme:form-hidden path="investmentRoundId" />
	<acme:form-textbox code="investor.application.label.ticker" placeholder="SSS-YY-NNNNNN" path="ticker" />
	<acme:message code="entrepreneur.investmentRound.activitySectors" />
	<acme:message code="${activitySectors}" />
	<br />
	<acme:message code="entrepreneur.investmentRound.YY" />
	<br />
	<br />
	<jstl:if test="${command != 'create'}">
		<acme:form-moment code="investor.application.label.creationMoment" path="creationMoment" readonly="true" />
	</jstl:if>
	<acme:form-textbox code="investor.application.label.statement" path="statement" />
	<acme:form-textbox code="investor.application.label.moneyOffer" path="moneyOffer" />
	<jstl:if test="${command != 'create'}">
		<acme:form-textbox code="investor.application.label.tickerInvestmentRound" path="investmentRound.ticker" readonly="true" />
		<acme:form-textbox code="investor.application.label.investorName" path="investor.identity.fullName" readonly="true" />
	</jstl:if>

	<acme:form-submit test="${command == 'create'}" code="investor.application.form.button.create"
		action="/investor/application/create" />

	<acme:form-return code="investor.application.button.return" />

</acme:form>
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
	<jstl:if test="${command == 'create'}">
		<acme:form-textbox code="entrepreneur.investmentRound.label.ticker" placeholder="SSS-YY-NNNNNN" path="ticker" />
		<acme:message code="entrepreneur.investmentRound.activitySectors" />
		<acme:message code="${activitySectors}" />
		<br />
		<acme:message code="entrepreneur.investmentRound.YY" />
		<br />
		<br />
		<acme:form-textbox code="entrepreneur.investmentRound.label.round" path="round"
			placeholder="SEED, ANGEL, SERIES_A, SERIES_B, SERIES_C, BRIDGE" />
		<acme:form-textbox code="entrepreneur.investmentRound.label.title" path="title" />
		<acme:form-textarea code="entrepreneur.investmentRound.label.description" path="description" />
		<acme:form-money code="entrepreneur.investmentRound.label.amountMoney" path="amountMoney" />
		<acme:form-url code="entrepreneur.investmentRound.label.moreInfo" path="moreInfo" />
	</jstl:if>
	<jstl:if test="${command != 'create'}">
		<acme:form-textbox code="entrepreneur.investmentRound.label.ticker" placeholder="SSS-YY-NNNNNN" path="ticker" readonly="true" />
		<acme:form-moment code="entrepreneur.investmentRound.label.creation" path="creationMoment" readonly="true" />
		<jstl:if test="${status == false}">
			<acme:form-textbox code="entrepreneur.investmentRound.label.round" path="round"
				placeholder="SEED, ANGEL, SERIES_A, SERIES_B, SERIES_C, BRIDGE" />
			<acme:form-textbox code="entrepreneur.investmentRound.label.title" path="title" />
			<acme:form-textarea code="entrepreneur.investmentRound.label.description" path="description" />
			<acme:form-money code="entrepreneur.investmentRound.label.amountMoney" path="amountMoney" />
			<acme:form-url code="entrepreneur.investmentRound.label.moreInfo" path="moreInfo" />
		</jstl:if>

		<jstl:if test="${status == true}">
			<acme:form-textbox code="entrepreneur.investmentRound.label.round" path="round" readonly="true" />
			<acme:form-textbox code="entrepreneur.investmentRound.label.title" path="title" readonly="true" />
			<acme:form-textarea code="entrepreneur.investmentRound.label.description" path="description" readonly="true" />
			<acme:form-money code="entrepreneur.investmentRound.label.amountMoney" path="amountMoney" readonly="true" />
			<acme:form-url code="entrepreneur.investmentRound.label.moreInfo" path="moreInfo" readonly="true" />
			<acme:form-textbox code="entrepreneur.investmentRound.label.entrepreneur" path="entrepreneur.identity.fullName" readonly="true" />
		</jstl:if>
	</jstl:if>

	<jstl:if test="${command != 'create'}">
		<acme:form-textbox code="entrepreneur.investmentRound.label.entrepreneur" path="entrepreneur.identity.fullName" readonly="true" />
	</jstl:if>

	<jstl:if test="${command != 'create' && status == false}">
		<acme:form-checkbox code="entrepreneur.investmentRound.label.notPublished" path="status" />
	</jstl:if>

	<jstl:if test="${status == true}">
		<acme:form-checkbox code="entrepreneur.investmentRound.label.published" path="status" readonly="true" />
	</jstl:if>

	<acme:form-submit test="${command == 'create'}" code="entrepreneur.investmentRound.form.button.create"
		action="/entrepreneur/investment-round/create" />
	<acme:form-submit test="${(command == 'show' || command == 'update') && status == false}"
		code="entrepreneur.investmentRound.form.button.update" action="/entrepreneur/investment-round/update" />
	<acme:form-submit test="${command == 'show' || command == 'delete'}" code="entrepreneur.investmentRound.form.button.delete"
		action="/entrepreneur/investment-round/delete" />

	<acme:form-submit test="${command != 'create'}" code="entrepreneur.investmentRound.form.button.workProgramme"
		action="/entrepreneur/work-programme/list?investmentRoundId=${id}" method="get" />
	<acme:form-submit test="${(command != 'create') && status == true && numAR > 0}"
		code="entrepreneur.investmentRound.form.button.accountingRecord"
		action="/entrepreneur/accounting-record/list-mine?investmentRoundid=${id}" method="get" />
	<acme:form-submit test="${command != 'create' && status == false }"
		code="entrepreneur.investmentRound.form.button.createWorkProgramme"
		action="/entrepreneur/work-programme/create?investmentRoundId=${id}&status=${status}" method="get" />

	<acme:form-return code="entrepreneur.investmentRound.button.return" />
</acme:form>


package acme.features.entrepreneur.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.applications.Application;
import acme.entities.roles.Entrepreneur;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractUpdateService;

@Service
public class EntrepreneurApplicationUpdateService implements AbstractUpdateService<Entrepreneur, Application> {

	// Internal state ------------------------------------------------------------------
	@Autowired
	EntrepreneurApplicationRepository repository;


	// AbstractListService<Entrepreneur, Application> interface ------------------------------
	@Override
	public boolean authorise(final Request<Application> request) {
		assert request != null;

		boolean result;
		int applicationId;
		Application application;
		Entrepreneur entrepreneur;
		Principal principal;

		applicationId = request.getModel().getInteger("id");
		application = this.repository.findOneById(applicationId);
		entrepreneur = application.getInvestmentRound().getEntrepreneur();
		principal = request.getPrincipal();
		result = entrepreneur.getUserAccount().getId() == principal.getAccountId();

		return result;
	}

	@Override
	public void bind(final Request<Application> request, final Application entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);

	}

	@Override
	public void unbind(final Request<Application> request, final Application entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "ticker", "creationMoment", "statement", "moneyOffer", "investor.identity.fullName", "investmentRound.ticker", "status", "reason");
	}

	@Override
	public Application findOne(final Request<Application> request) {
		assert request != null;

		Application result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findOneById(id);

		return result;
	}

	@Override
	public void validate(final Request<Application> request, final Application entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		if (!errors.hasErrors("reason")) {
			errors.state(request, entity.getReason() != null && entity.getReason() != "" || !entity.getStatus().equals("rejected"), "reason", "entrepreneur.application.error.reason");

		}

	}

	@Override
	public void update(final Request<Application> request, final Application entity) {
		assert request != null;
		assert entity != null;

		this.repository.save(entity);

	}

}

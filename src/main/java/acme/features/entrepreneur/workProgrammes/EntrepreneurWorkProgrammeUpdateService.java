
package acme.features.entrepreneur.workProgrammes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.roles.Entrepreneur;
import acme.entities.workProgrammes.WorkProgramme;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractUpdateService;

@Service
public class EntrepreneurWorkProgrammeUpdateService implements AbstractUpdateService<Entrepreneur, WorkProgramme> {

	@Autowired
	EntrepreneurWorkProgrammeRepository repository;


	@Override
	public boolean authorise(final Request<WorkProgramme> request) {
		assert request != null;

		boolean result;
		int workProgrammeId;
		WorkProgramme workProgramme;
		Entrepreneur entrepreneur;
		Principal principal;

		workProgrammeId = request.getModel().getInteger("id");
		workProgramme = this.repository.findOneById(workProgrammeId);
		System.out.println(workProgramme);
		entrepreneur = workProgramme.getInvestmentRound().getEntrepreneur();
		principal = request.getPrincipal();
		result = entrepreneur.getUserAccount().getId() == principal.getAccountId();

		return result;
	}

	@Override
	public void bind(final Request<WorkProgramme> request, final WorkProgramme entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);

	}

	@Override
	public void unbind(final Request<WorkProgramme> request, final WorkProgramme entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "start", "end", "budget");

	}

	@Override
	public WorkProgramme findOne(final Request<WorkProgramme> request) {
		assert request != null;

		WorkProgramme result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findOneById(id);

		return result;
	}

	@Override
	public void validate(final Request<WorkProgramme> request, final WorkProgramme entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
	}

	@Override
	public void update(final Request<WorkProgramme> request, final WorkProgramme entity) {
		assert request != null;
		assert entity != null;

		this.repository.save(entity);
	}
}

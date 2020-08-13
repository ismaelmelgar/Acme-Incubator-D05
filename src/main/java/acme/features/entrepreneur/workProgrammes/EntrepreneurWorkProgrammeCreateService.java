
package acme.features.entrepreneur.workProgrammes;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.investmentRounds.InvestmentRound;
import acme.entities.roles.Entrepreneur;
import acme.entities.workProgrammes.WorkProgramme;
import acme.features.entrepreneur.investmentRounds.EntrepreneurInvestmentRoundRepository;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractCreateService;

@Service
public class EntrepreneurWorkProgrammeCreateService implements AbstractCreateService<Entrepreneur, WorkProgramme> {

	@Autowired
	EntrepreneurWorkProgrammeRepository		repository;

	@Autowired
	EntrepreneurInvestmentRoundRepository	investmentRoundRepository;


	@Override
	public boolean authorise(final Request<WorkProgramme> request) {
		assert request != null;

		boolean result;
		int investmentRoundId;
		InvestmentRound investmentRound;
		Entrepreneur entrepreneur;
		Principal principal;

		investmentRoundId = request.getModel().getInteger("investmentRoundId");
		investmentRound = this.repository.findOneByIdII(investmentRoundId);
		entrepreneur = investmentRound.getEntrepreneur();
		principal = request.getPrincipal();

		result = entrepreneur.getUserAccount().getId() == principal.getAccountId();

		System.out.println("AUTORISE");
		return result;
	}

	@Override
	public void bind(final Request<WorkProgramme> request, final WorkProgramme entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
		System.out.println("BIND");

	}

	@Override
	public void unbind(final Request<WorkProgramme> request, final WorkProgramme entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		model.setAttribute("investmentRoundId", entity.getInvestmentRound().getId());
		request.unbind(entity, model, "title", "start", "end", "budget");

		System.out.println("UNBIND");
	}

	@Override
	public WorkProgramme instantiate(final Request<WorkProgramme> request) {
		WorkProgramme result;

		result = new WorkProgramme();
		InvestmentRound investmentRound;
		int investmentRoundId = request.getModel().getInteger("investmentRoundId");
		Date moment;

		moment = new Date(System.currentTimeMillis() - 1);
		result.setStart(moment);

		investmentRound = this.investmentRoundRepository.findOneById(investmentRoundId);
		result.setInvestmentRound(investmentRound);
		System.out.println("INSTANTIATE");

		return result;
	}

	@Override
	public void validate(final Request<WorkProgramme> request, final WorkProgramme entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		System.out.println("VALIDATE");
	}

	@Override
	public void create(final Request<WorkProgramme> request, final WorkProgramme entity) {
		assert request != null;
		assert entity != null;

		Date moment;

		moment = new Date(System.currentTimeMillis() - 1);
		entity.setStart(moment);
		this.repository.save(entity);
		System.out.println("CREATE");

	}

}

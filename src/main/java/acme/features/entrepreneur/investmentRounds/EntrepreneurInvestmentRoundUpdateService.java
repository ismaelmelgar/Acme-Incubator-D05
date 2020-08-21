
package acme.features.entrepreneur.investmentRounds;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.configuration.Customisation;
import acme.entities.investmentRounds.InvestmentRound;
import acme.entities.roles.Entrepreneur;
import acme.features.spamFilter.SpamFilter;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractUpdateService;

@Service
public class EntrepreneurInvestmentRoundUpdateService implements AbstractUpdateService<Entrepreneur, InvestmentRound> {

	@Autowired
	EntrepreneurInvestmentRoundRepository repository;


	@Override
	public boolean authorise(final Request<InvestmentRound> request) {
		assert request != null;

		boolean result;
		int investmentRoundId;
		InvestmentRound investmentRound;
		Entrepreneur entrepreneur;
		Principal principal;

		investmentRoundId = request.getModel().getInteger("id");
		investmentRound = this.repository.findOneById(investmentRoundId);
		entrepreneur = investmentRound.getEntrepreneur();
		principal = request.getPrincipal();
		result = entrepreneur.getUserAccount().getId() == principal.getAccountId();
		return result;
	}

	@Override
	public void bind(final Request<InvestmentRound> request, final InvestmentRound entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);

	}

	@Override
	public void unbind(final Request<InvestmentRound> request, final InvestmentRound entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		int id = request.getModel().getInteger("id");
		int numAccountingRecord = this.repository.findAccountingRecordByInvestmentRoundId(id);
		int numApplication = this.repository.findApplicationByInvestmentRoundId(id);
		model.setAttribute("numAccountingRecord", numAccountingRecord);
		model.setAttribute("numApplication", numApplication);
		request.unbind(entity, model, "title", "description", "amountMoney", "creationMoment", "round", "ticker", "moreInfo", "entrepreneur.identity.fullName", "status");

	}

	@Override
	public InvestmentRound findOne(final Request<InvestmentRound> request) {
		assert request != null;

		InvestmentRound result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findOneById(id);

		return result;
	}

	@Override
	public void validate(final Request<InvestmentRound> request, final InvestmentRound entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		//Filtro Anti-Spam

		SpamFilter sf = new SpamFilter();
		Customisation customisation = this.repository.findCustomisation();
		String spam = customisation.getSpamWords();
		Double threshold = customisation.getThreshold();
		String[] spamWordPieces = sf.spamWordPieces(spam);

		if (!errors.hasErrors("title")) {
			Boolean isSpam = sf.isFreeSpam(spamWordPieces, entity.getTitle(), threshold);
			errors.state(request, isSpam, "title", "entrepreneur.investmentRound.error.spam");
		}

		if (!errors.hasErrors("description")) {
			Boolean isSpam = sf.isFreeSpam(spamWordPieces, entity.getDescription(), threshold);
			errors.state(request, isSpam, "description", "entrepreneur.investmentRound.error.spam");
		}

		if (!errors.hasErrors("status")) {
			Integer id = request.getModel().getInteger("id");
			Double sumBudget = this.repository.sumBudgetWorkProgramme(id);
			if (sumBudget == null) {
				sumBudget = 0.;
			}
			errors.state(request, sumBudget.equals(entity.getAmountMoney().getAmount()) || !request.getModel().getBoolean("status"), "status", "entrepreneur.investmentRound.error.sumBudget");
		}

		if (!errors.hasErrors("amountMoney")) {
			errors.state(request, entity.getAmountMoney().getCurrency().equals("EUR") || entity.getAmountMoney().getCurrency().equals("€"), "amountMoney", "entrepreneur.investmentRound.form.error.zoneEur");
		}

		if (errors.hasErrors() == true) {
			request.getModel().setAttribute("status", false);

		}

	}

	@Override
	public void update(final Request<InvestmentRound> request, final InvestmentRound entity) {
		assert request != null;
		assert entity != null;

		this.repository.save(entity);
	}

}


package acme.features.bookkeeper.accountingRecord;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.accountingRecords.AccountingRecord;
import acme.entities.configuration.Customisation;
import acme.entities.investmentRounds.InvestmentRound;
import acme.entities.roles.Bookkeeper;
import acme.features.authenticated.investmentRounds.AuthenticatedInvestmentRoundRepository;
import acme.features.spamFilter.SpamFilter;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractCreateService;

@Service
public class BookkeeperAccountingRecordCreateService implements AbstractCreateService<Bookkeeper, AccountingRecord> {

	@Autowired
	BookkeeperAccountingRecordRepository	repository;

	@Autowired
	AuthenticatedInvestmentRoundRepository	investmentRoundRepository;


	@Override
	public boolean authorise(final Request<AccountingRecord> request) {
		assert request != null;

		boolean result = true;
		Integer investmentRoundId;
		InvestmentRound investmentRound;
		investmentRoundId = request.getModel().getInteger("investmentRoundId");
		investmentRound = this.investmentRoundRepository.findOneById(investmentRoundId);

		// If the investment round is not already published you cannot create a accounting record
		if (investmentRound.getStatus() == false) {
			result = false;
		}

		return result;
	}

	@Override
	public void bind(final Request<AccountingRecord> request, final AccountingRecord entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors, "investmentRound");

	}

	@Override
	public void unbind(final Request<AccountingRecord> request, final AccountingRecord entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		model.setAttribute("investmentRoundId", entity.getInvestmentRound().getId());
		request.unbind(entity, model, "title", "body", "status", "creation", "bookkeeper.identity.fullName");

	}

	@Override
	public AccountingRecord instantiate(final Request<AccountingRecord> request) {
		assert request != null;

		AccountingRecord result;
		InvestmentRound investmentRound;
		int investmentRoundId;

		result = new AccountingRecord();
		investmentRoundId = request.getModel().getInteger("investmentRoundId");
		investmentRound = this.investmentRoundRepository.findOneById(investmentRoundId);
		result.setInvestmentRound(investmentRound);

		Date moment;

		moment = new Date(System.currentTimeMillis() - 1);
		result.setCreation(moment);

		Principal principal;
		Bookkeeper bookkeeper;
		int userAccountId;

		principal = request.getPrincipal();
		userAccountId = principal.getActiveRoleId();
		bookkeeper = this.repository.findBookkeeperById(userAccountId);
		result.setBookkeeper(bookkeeper);
		result.setStatus(false);

		return result;
	}

	@Override
	public void validate(final Request<AccountingRecord> request, final AccountingRecord entity, final Errors errors) {
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
			errors.state(request, isSpam, "title", "bookkeeper.accountingRecord.error.spam");
		}

		if (!errors.hasErrors("body")) {
			Boolean isSpam = sf.isFreeSpam(spamWordPieces, entity.getBody(), threshold);
			errors.state(request, isSpam, "body", "bookkeeper.accountingRecord.error.spam");
		}

	}

	@Override
	public void create(final Request<AccountingRecord> request, final AccountingRecord entity) {
		assert request != null;
		assert entity != null;

		this.repository.save(entity);
	}
}

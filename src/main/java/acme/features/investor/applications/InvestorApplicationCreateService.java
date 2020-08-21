
package acme.features.investor.applications;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.applications.Application;
import acme.entities.configuration.Customisation;
import acme.entities.investmentRounds.InvestmentRound;
import acme.entities.roles.Investor;
import acme.features.spamFilter.SpamFilter;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractCreateService;

@Service
public class InvestorApplicationCreateService implements AbstractCreateService<Investor, Application> {

	@Autowired
	InvestorApplicationRepository repository;


	@Override
	public boolean authorise(final Request<Application> request) {
		assert request != null;

		return true;
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

		model.setAttribute("investmentRoundId", entity.getInvestmentRound().getId());
		request.unbind(entity, model, "ticker", "creationMoment", "statement", "moneyOffer", "investor.identity.fullName", "investmentRound.ticker");

	}

	@Override
	public Application instantiate(final Request<Application> request) {
		Application result;

		result = new Application();
		Date moment;
		Principal principal;
		Investor investor;
		int userAccountId;
		int investmentRoundId;
		InvestmentRound investmentRound;

		principal = request.getPrincipal();
		userAccountId = principal.getActiveRoleId();
		investor = this.repository.findInvestorById(userAccountId);
		result.setInvestor(investor);

		moment = new Date(System.currentTimeMillis() - 1);
		result.setCreationMoment(moment);
		result.setStatus("Pending");

		investmentRoundId = request.getModel().getInteger("investmentRoundId");
		investmentRound = this.repository.findInvestmentRoundById(investmentRoundId);
		result.setInvestmentRound(investmentRound);

		return result;
	}

	@Override
	public void validate(final Request<Application> request, final Application entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		Collection<String> tickers = this.repository.getTickers();
		String[] activitySector = this.repository.findCustomisation().getActivitySectors().split(",");

		if (!errors.hasErrors("moneyOffer")) {
			errors.state(request, entity.getMoneyOffer().getCurrency().equals("EUR") || entity.getMoneyOffer().getCurrency().equals("€"), "moneyOffer", "investor.application.form.error.zoneEur");
		}

		//Filtro Anti-Spam

		SpamFilter sf = new SpamFilter();
		Customisation customisation = this.repository.findCustomisation();
		String spam = customisation.getSpamWords();
		Double threshold = customisation.getThreshold();
		String[] spamWordPieces = sf.spamWordPieces(spam);

		if (!errors.hasErrors("statement")) {
			Boolean isSpam = sf.isFreeSpam(spamWordPieces, entity.getStatement(), threshold);
			errors.state(request, isSpam, "statement", "investor.application.error.spam");
		}

		// TICKER

		if (!errors.hasErrors("ticker")) {
			errors.state(request, !tickers.contains(entity.getTicker()), "ticker", "investor.application.error.ticker.repeated");
		}

		if (!errors.hasErrors("ticker")) {
			String texto = entity.getTicker();

			Integer lonTicker = 0;
			for (int j = 0; j <= texto.length() - 1; j++) {
				if (Character.isLetter(texto.charAt(j))) {
					lonTicker = lonTicker + 1;
				}
			}

			String subS = texto.substring(0, 3);
			Boolean res = false;

			for (int i = 0; i <= activitySector.length - 1; i++) {
				activitySector[i] = activitySector[i].trim();
				if (activitySector[i].length() == 1) {
					activitySector[i] = activitySector[i] + "XX";
				}
				if (activitySector[i].length() == 2) {
					activitySector[i] = activitySector[i] + "X";
				}
				String SSS = activitySector[i].trim().substring(0, 3).toUpperCase();
				if (subS.equals(SSS)) {
					res = true;
					break;
				}
			}
			errors.state(request, res, "ticker", "investor.application.error.activitySector");
		}

		if (!errors.hasErrors("ticker")) {
			String texto = entity.getTicker();
			String subY = texto.substring(4, 6);
			Calendar cal = Calendar.getInstance();
			Integer anyo = cal.get(Calendar.YEAR);
			String anyoSub = anyo.toString().substring(2, 4);
			Boolean put = subY.equals(anyoSub);
			errors.state(request, put, "ticker", "investor.application.error.anyo");

		}

	}

	@Override
	public void create(final Request<Application> request, final Application entity) {
		assert request != null;
		assert entity != null;

		Date moment;

		moment = new Date(System.currentTimeMillis() - 1);
		entity.setCreationMoment(moment);
		this.repository.save(entity);
	}

}

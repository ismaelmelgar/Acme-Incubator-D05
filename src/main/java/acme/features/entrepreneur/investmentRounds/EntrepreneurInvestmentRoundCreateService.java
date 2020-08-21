
package acme.features.entrepreneur.investmentRounds;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

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
import acme.framework.services.AbstractCreateService;

@Service
public class EntrepreneurInvestmentRoundCreateService implements AbstractCreateService<Entrepreneur, InvestmentRound> {

	@Autowired
	EntrepreneurInvestmentRoundRepository repository;


	@Override
	public boolean authorise(final Request<InvestmentRound> request) {
		assert request != null;

		return true;
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

		Customisation customisation = this.repository.findCustomisation();
		String activitySectors = customisation.getActivitySectors();
		model.setAttribute("activitySectors", activitySectors);

		request.unbind(entity, model, "ticker", "creationMoment", "round", "title", "description", "status", "amountMoney", "moreInfo", "entrepreneur.identity.fullName");

	}

	@Override
	public InvestmentRound instantiate(final Request<InvestmentRound> request) {
		InvestmentRound result;

		result = new InvestmentRound();
		Date moment;
		Principal principal;
		Entrepreneur entrepreneur;
		int userAccountId;

		principal = request.getPrincipal();
		userAccountId = principal.getActiveRoleId();
		entrepreneur = this.repository.findEntrepreneurById(userAccountId);
		result.setEntrepreneur(entrepreneur);

		moment = new Date(System.currentTimeMillis() - 1);
		result.setCreationMoment(moment);
		result.setStatus(false);

		return result;
	}

	@Override
	public void validate(final Request<InvestmentRound> request, final InvestmentRound entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		Collection<String> tickers = this.repository.getTickers();
		String[] activitySector = this.repository.findCustomisation().getActivitySectors().split(",");

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

		if (!errors.hasErrors("amountMoney")) {
			errors.state(request, entity.getAmountMoney().getCurrency().equals("EUR") || entity.getAmountMoney().getCurrency().equals("€"), "amountMoney", "entrepreneur.investmentRound.form.error.zoneEur");
		}

		// TICKER

		if (!errors.hasErrors("ticker")) {
			errors.state(request, !tickers.contains(entity.getTicker()), "ticker", "entrepreneur.investmentRound.error.ticker.repeated");
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
			errors.state(request, res, "ticker", "entrepreneur.investmentRound.error.activitySector");
		}

		if (!errors.hasErrors("ticker")) {
			String texto = entity.getTicker();
			String subY = texto.substring(4, 6);
			Calendar cal = Calendar.getInstance();
			Integer anyo = cal.get(Calendar.YEAR);
			String anyoSub = anyo.toString().substring(2, 4);
			Boolean put = subY.equals(anyoSub);
			errors.state(request, put, "ticker", "entrepreneur.investmentRound.error.anyo");

		}

	}

	@Override
	public void create(final Request<InvestmentRound> request, final InvestmentRound entity) {
		assert request != null;
		assert entity != null;

		Date moment;

		moment = new Date(System.currentTimeMillis() - 1);
		entity.setCreationMoment(moment);
		entity.setStatus(false);
		this.repository.save(entity);

	}

}

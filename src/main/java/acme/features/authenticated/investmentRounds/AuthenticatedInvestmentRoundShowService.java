
package acme.features.authenticated.investmentRounds;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.investmentRounds.InvestmentRound;
import acme.entities.roles.Investor;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractShowService;

@Service
public class AuthenticatedInvestmentRoundShowService implements AbstractShowService<Authenticated, InvestmentRound> {

	// Internal state ------------------------------------------------------------------
	@Autowired
	AuthenticatedInvestmentRoundRepository repository;

	// AbstractListService<Authenticated, InvestmentRound> interface ------------------------------


	@Override
	public boolean authorise(final Request<InvestmentRound> request) {
		assert request != null;

		boolean result = true;
		int investmentRoundId;

		investmentRoundId = request.getModel().getInteger("id");
		InvestmentRound investmentRound = this.repository.findOneByIdII(investmentRoundId);

		// If the investment round is already published you can not see it
		if (investmentRound.getStatus() == false) {
			result = false;
		}

		return result;
	}

	@Override
	public void unbind(final Request<InvestmentRound> request, final InvestmentRound entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		int id = request.getModel().getInteger("id");
		int numAR = this.repository.findAccountingRecordByInvestmentRoundId(id);
		model.setAttribute("numAR", numAR);
		Principal principal = request.getPrincipal();
		model.setAttribute("isInvestor", principal.hasRole(Investor.class));

		request.unbind(entity, model, "ticker", "creationMoment", "round", "title", "description", "amountMoney", "moreInfo", "entrepreneur.identity.fullName", "status");

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

}

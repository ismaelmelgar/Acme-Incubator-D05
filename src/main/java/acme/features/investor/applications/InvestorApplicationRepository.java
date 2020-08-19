
package acme.features.investor.applications;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.applications.Application;
import acme.entities.configuration.Customisation;
import acme.entities.investmentRounds.InvestmentRound;
import acme.entities.roles.Investor;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface InvestorApplicationRepository extends AbstractRepository {

	@Query("select a from Application a where a.investor.id = ?1")
	Collection<Application> findManyByInvestorId(int id);

	@Query("select a from Application a where a.id = ?1")
	Application findOneById(int id);

	@Query("select i from Investor i where i.id = ?1")
	Investor findInvestorById(int investorId);

	@Query("select i from InvestmentRound i where i.id = ?1")
	InvestmentRound findInvestmentRoundById(int id);

	@Query("select a.ticker from Application a")
	Collection<String> getTickers();

	@Query("select c from Customisation c")
	Customisation findCustomisation();

}

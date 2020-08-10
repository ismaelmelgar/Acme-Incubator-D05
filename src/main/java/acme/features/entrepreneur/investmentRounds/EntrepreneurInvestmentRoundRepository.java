
package acme.features.entrepreneur.investmentRounds;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.forums.Forum;
import acme.entities.investmentRounds.InvestmentRound;
import acme.entities.roles.Entrepreneur;
import acme.entities.workProgrammes.WorkProgramme;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface EntrepreneurInvestmentRoundRepository extends AbstractRepository {

	@Query("select ir from InvestmentRound ir where ir.entrepreneur.id = ?1")
	Collection<InvestmentRound> findManyByEntrepreneurId(int id);

	@Query("select ir from InvestmentRound ir where ir.id = ?1")
	InvestmentRound findOneById(int id);

	@Query("select ir from InvestmentRound ir where ir.id = ?1")
	InvestmentRound findOneInvestmentRoundById(int id);

	@Query("select wp from WorkProgramme wp join wp.investmentRound ir where ir.id = ?1")
	Collection<WorkProgramme> findWorkProgrammeByInvestmentRoundId(int id);

	@Query("select count(ar) from AccountingRecord ar where ar.investmentRound.id = ?1")
	int findAccountingRecordByInvestmentRoundId(int investmentRoundId);

	@Query("select e from Entrepreneur e where e.id = ?1")
	Entrepreneur findEntrepreneurById(int entrepreneurId);

	@Query("select count(a) from Application a where a.investmentRound.id = ?1")
	int findApplicationByInvestmentRoundId(int investmentRoundId);

	@Query("select wp from WorkProgramme wp where wp.investmentRound.id = ?1")
	Collection<WorkProgramme> findAllWorkProgrammeByInvestmentRoundId(int investmentRoundId);

	@Query("select f from Forum f where f.investmentRound.id = ?1")
	Forum findForumByInvestmentRoundId(int investmentRoundId);

}

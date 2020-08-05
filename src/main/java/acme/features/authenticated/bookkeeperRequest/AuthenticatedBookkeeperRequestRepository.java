/*
 * EntrepreneurProviderRepository.java
 *
 * Copyright (c) 2019 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.bookkeeperRequest;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.roles.Bookkeeper;
import acme.framework.entities.Authenticated;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedBookkeeperRequestRepository extends AbstractRepository {

	@Query("select a from Bookkeeper a where  a.userAccount.id = ?1")
	Bookkeeper findOneBookkeeperByUserAccountId(int id);

	@Query("select au from Authenticated au where au.userAccount.id =?1")
	Authenticated findOneAuthenticatedByUserAccountId(int userAccountId);

	@Query("select count(a) from BookkeeperRequest a where a.authenticated.userAccount.id = ?1")
	int findTotalBookkeeperRequestByUserAccountId(int userAccountId);
}


package acme.entities.bookkeeperRequests;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import acme.framework.entities.Authenticated;
import acme.framework.entities.DomainEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class BookkeeperRequest extends DomainEntity {

	//Serialisation identifier ---------------------------------------

	private static final long	serialVersionUID	= 1L;

	//Attributes -----------------------------------------------------

	@NotBlank
	@Column(length = 100)
	private String				firm;

	@NotBlank
	@Column(length = 3000)
	private String				responsibilityStatement;

	// Derived attributes -----------------------------------------------------

	@NotNull
	@Valid
	@OneToOne(optional = false)
	private Authenticated		authenticated;
}

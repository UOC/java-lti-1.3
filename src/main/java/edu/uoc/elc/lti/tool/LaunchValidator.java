package edu.uoc.elc.lti.tool;

import edu.uoc.elc.lti.tool.validator.LaunchValidatable;
import edu.uoc.elc.lti.tool.validator.MessageTypesValidatorEnum;
import edu.uoc.lti.claims.ClaimAccessor;
import edu.uoc.lti.claims.ClaimsEnum;
import edu.uoc.lti.oidc.OIDCLaunchSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
public class LaunchValidator {
	private final Registration registration;
	private final ClaimAccessor claimAccessor;
	private final OIDCLaunchSession oidcLaunchSession;

	@Getter
	private String reason;

	/**
	 * Validates a LTI 1.3 launch
	 * @param token JWT token to validate
	 * @param state saved state, if present
	 * @return true if token is a valid LTI 1.3 launch, false otherwise
	 */
	public boolean validate(String token, String state) {
		AuthenticationResponseValidator authenticationResponseValidator = new AuthenticationResponseValidator(registration, claimAccessor);

		// IMS Security validations
		if (!authenticationResponseValidator.validate(token)) {
			this.reason = authenticationResponseValidator.getReason();
			return false;
		}

		final String messageTypeClaim = this.claimAccessor.get(ClaimsEnum.MESSAGE_TYPE);
		final LaunchValidatable validator = MessageTypesValidatorEnum.getValidator(messageTypeClaim);
		if (validator == null) {
			setReasonToInvalidClaim(ClaimsEnum.MESSAGE_TYPE);
			return false;
		}

		if (!validator.validate(state, registration, claimAccessor, oidcLaunchSession)) {
			this.reason = validator.getReason();
			return false;
		}

		return true;
	}

	private void setReasonToInvalidClaim(ClaimsEnum claim) {
		reason = "Claim " + claim.getName() + " is invalid";
	}

}

package edu.uoc.elc.lti.tool;

import edu.uoc.lti.MessageTypesEnum;
import edu.uoc.lti.claims.ClaimAccessor;
import edu.uoc.lti.claims.ClaimsEnum;
import edu.uoc.lti.deeplink.content.DocumentTargetEnum;
import edu.uoc.lti.deeplink.content.Presentation;
import edu.uoc.lti.oidc.OIDCLaunchSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
public class LaunchValidator {
	private final static String VERSION = "1.3.0";
	private static final int ID_MAX_LENGTH = 255;

	private final ToolDefinition toolDefinition;
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
		AuthenticationResponseValidator authenticationResponseValidator = new AuthenticationResponseValidator(toolDefinition, claimAccessor);

		// IMS Security validations
		if (!authenticationResponseValidator.validate(token)) {
			this.reason = authenticationResponseValidator.getReason();
			return false;
		}

		// LTI required claims
		if (!validateRequiredClaims(state)) {
			return false;
		}

		// LTI optional claims
		if (!validateOptionalClaims()) {
			return false;
		}

		// state
		if (state != null) {
			if (!state.equals(this.oidcLaunchSession.getState())) {
				reason = "Invalid state";
				return false;
			}
			if (claimAccessor.get(ClaimsEnum.NONCE) != null) {
				if (!claimAccessor.get(ClaimsEnum.NONCE).equals(this.oidcLaunchSession.getNonce())) {
					setReasonToInvalidClaim(ClaimsEnum.NONCE);
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Validates the required claims of the LTI launch following https://www.imsglobal.org/spec/lti/v1p3/#required-message-claims
	 * @param state saved state, if present
	 * @return true if the required claims of the LTI launch are valid, false otherwise
	 */
	private boolean validateRequiredClaims(String state) {
		// 5.3.1 message type claim
		final String messageTypeClaim = this.claimAccessor.get(ClaimsEnum.MESSAGE_TYPE);
		if (messageTypeClaim == null) {
			setReasonToMissingRequiredClaim(ClaimsEnum.MESSAGE_TYPE);
			return false;
		}

		try {
			MessageTypesEnum.valueOf(messageTypeClaim);
		} catch (IllegalArgumentException e) {
			setReasonToInvalidClaim(ClaimsEnum.MESSAGE_TYPE);
			return false;
		}

		// 5.3.2 version
		final String versionClaim = this.claimAccessor.get(ClaimsEnum.VERSION);
		if (versionClaim == null || !VERSION.equals(versionClaim)) {
			setReasonToInvalidClaim(ClaimsEnum.VERSION);
			return false;
		}

		// 5.3.3 LTI Deployment ID claim
		final String deploymentId = this.claimAccessor.get(ClaimsEnum.DEPLOYMENT_ID);
		if (isEmpty(deploymentId)) {
			setReasonToMissingRequiredClaim(ClaimsEnum.DEPLOYMENT_ID);
			return false;
		}
		if (deploymentId.trim().length() > ID_MAX_LENGTH) {
			setReasonToInvalidClaim(ClaimsEnum.DEPLOYMENT_ID);
			return false;
		}
		if (!this.toolDefinition.getDeploymentId().equals(deploymentId)) {
			setReasonToInvalidClaim(ClaimsEnum.DEPLOYMENT_ID);
			return false;
		}

		// 5.3.4 Target Link URI
		if (state != null) {
			final String targetLinkUri = this.claimAccessor.get(ClaimsEnum.TARGET_LINK_URI);
			if (isEmpty(targetLinkUri)) {
				setReasonToMissingRequiredClaim(ClaimsEnum.TARGET_LINK_URI);
				return false;
			}
			final String targetLinkUriFromOidcSession = this.oidcLaunchSession.getTargetLinkUri();
			if (!targetLinkUri.equals(targetLinkUriFromOidcSession)) {
				setReasonToInvalidClaim(ClaimsEnum.TARGET_LINK_URI);
				return false;
			}
		}

		// 5.3.5 Resource link claim
		final ResourceLink resourceLink = this.claimAccessor.get(ClaimsEnum.RESOURCE_LINK, ResourceLink.class);
		if (resourceLink == null) {
			setReasonToMissingRequiredClaim(ClaimsEnum.RESOURCE_LINK);
			return false;
		}

		if (!isIdStringValid(resourceLink.getId())) {
			setReasonToInvalidClaim(ClaimsEnum.RESOURCE_LINK);
			return false;
		}

		// 5.3.6 User Identity claims
		final String subject = this.claimAccessor.getSubject();
		if (isEmpty(subject)) {
			// check other user identity claims are empty as well
			List<ClaimsEnum> identityClaims = Arrays.asList(ClaimsEnum.GIVEN_NAME, ClaimsEnum.FAMILY_NAME, ClaimsEnum.NAME, ClaimsEnum.EMAIL);
			if (identityClaims.stream().anyMatch(claim -> !isEmpty(this.claimAccessor.get(claim)))) {
				this.reason = "Subject is required";
				return false;
			}
		} else {
			if (!isIdStringValid(subject)) {
				this.reason = "Subject is invalid";
				return false;
			}
		}


		// 5.3.7 Roles claim
		Class<List<String>> rolesClass = (Class) List.class;
		final List<String> roles = this.claimAccessor.get(ClaimsEnum.ROLES, rolesClass);
		if (roles == null) {
			setReasonToMissingRequiredClaim(ClaimsEnum.ROLES);
			return false;
		}
		// check it contains, at least one valid role
		final List<String> filteredRoles = roles.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
		if (filteredRoles.size() > 0 && !filteredRoles.stream().anyMatch(role -> RolesEnum.from(role) != null)) {
			setReasonToInvalidClaim(ClaimsEnum.ROLES);
			return false;
		}

		return true;
	}

	/**
	 * Validates the optional claims of the LTI launch following https://www.imsglobal.org/spec/lti/v1p3/#optional-message-claims
	 * @return true if the optional claims of the LTI launch are valid, false otherwise
	 */
	private boolean validateOptionalClaims() {
		// 5.4.1 Context claim
		final Context contextClaim = this.claimAccessor.get(ClaimsEnum.CONTEXT, Context.class);
		if (contextClaim != null) {
			if (!isIdStringValid(contextClaim.getId())) {
				setReasonToInvalidClaim(ClaimsEnum.CONTEXT);
				return false;
			}
		}
		// 5.4.2 Platform instance claim
		final Platform platform = this.claimAccessor.get(ClaimsEnum.TOOL_PLATFORM, Platform.class);
		if (platform != null) {
			if (!isIdStringValid(platform.getGuid())) {
				setReasonToInvalidClaim(ClaimsEnum.TOOL_PLATFORM);
				return false;
			}
		}

		// 5.4.3 Role-scope mentor claims
		Class<List<String>> mentorRolesClass = (Class) List.class;
		final List<String> mentorRoles = this.claimAccessor.get(ClaimsEnum.ROLE_SCOPE_MENTOR, mentorRolesClass);
		if (mentorRoles != null && mentorRoles.size() > 0) {
			Class<List<String>> rolesClass = (Class) List.class;
			final List<String> roles = this.claimAccessor.get(ClaimsEnum.ROLES, rolesClass);
			if (!roles.contains(RolesEnum.MENTOR.getName())) {
				setReasonToInvalidClaim(ClaimsEnum.ROLE_SCOPE_MENTOR);
				return false;
			}
		}

		// 5.4.4 Launch presentation claim
		final Presentation presentation = this.claimAccessor.get(ClaimsEnum.PRESENTATION, Presentation.class);
		if (presentation != null) {
			if (!isEmpty(presentation.getDocumentTarget())) {
				try {
					DocumentTargetEnum.valueOf(presentation.getDocumentTarget());
				} catch (IllegalArgumentException e) {
					setReasonToInvalidClaim(ClaimsEnum.PRESENTATION);
					return false;
				}
			}
		}

		// 5.4.5 Learning Information Services LIS claim: Nothing to do here
		// 5.4.6 Custom properties and variable substitution: Nothing to do here (values are gotten as string in Tool)
		// 5.4.7 Vendor-specific extension claims: Nothing to do here
		return true;
	}

	private boolean isEmpty(String value) {
		return value == null || "".equals(value.trim());
	}

	private boolean isIdStringValid(String value) {
		return !isEmpty(value) && value.trim().length() <= ID_MAX_LENGTH;
	}

	private void setReasonToMissingRequiredClaim(ClaimsEnum claim) {
		reason = "Required claim " + claim.getName() + " not found";
	}

	private void setReasonToInvalidClaim(ClaimsEnum claim) {
		reason = "Claim " + claim.getName() + " is invalid";
	}
}

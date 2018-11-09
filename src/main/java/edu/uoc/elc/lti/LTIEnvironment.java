package edu.uoc.elc.lti;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class LTIEnvironment {

	private final static List<String> ALLOWED_MESSAGE_TYPES = Arrays.asList("LtiResourceLinkRequest");
	private final static String VERSION = "1.3.0";

	Map<String, Claim> claims;

	@Getter
	String issuer;
	@Getter
	List<String> audience;
	@Getter
	Date issuedAt;
	@Getter
	Date expiresAt;

	@Getter
	private User user;

	private ConsumerService consumerService;

	@Getter
	private String locale;

	@Getter
	private boolean valid;
	@Getter
	private String reason;

	public boolean validate(String token) {
		valid = false;
		try {
			decode(token);

			// validate lti headers
			// message type
			final Claim messageTypeClaim = getClaim(ClaimsEnum.MESSAGE_TYPE.getName());
			if (messageTypeClaim == null || !ALLOWED_MESSAGE_TYPES.contains(messageTypeClaim.asString())) {
				reason = "Unknown Message Type";
				valid = false;
			}

			// version
			final Claim versionClaim = getClaim(ClaimsEnum.VERSION.getName());
			if (versionClaim == null || !VERSION.equals(versionClaim.asString())) {
				reason = "Invalid Version";
				valid = false;
			}
			valid = true;
		} catch (Throwable t) {
			reason = t.getMessage();
		}

		return valid;
	}

	public void decode(String token) {
		try {

			// validate and decode token
			DecodedJWT jwt = JWT.decode(token);

			// get the standard JWT payload claims
			this.issuer = jwt.getIssuer();
			this.audience = jwt.getAudience();
			this.issuedAt = jwt.getIssuedAt();
			this.expiresAt = jwt.getExpiresAt();

			// get the private claims (contains all LTI claims)
			this.claims = jwt.getClaims();

			// create the user attribute
			createUser(jwt.getSubject());

			// update locale attribute
			this.locale = getClaimAsString(ClaimsEnum.LOCALE.getName());

		} catch (JWTDecodeException exception){
			//Invalid token
			throw new InvalidTokenException("JWT is invalid");
		}
	}

	private void createUser(String subject) {
		String name = getClaimAsString(ClaimsEnum.NAME.getName());
		String givenName = getClaimAsString(ClaimsEnum.GIVEN_NAME.getName());
		String familyName = getClaimAsString(ClaimsEnum.FAMILY_NAME.getName());
		String middleName = getClaimAsString(ClaimsEnum.MIDDLE_NAME.getName());
		String picture = getClaimAsString(ClaimsEnum.PICTURE.getName());
		String email = getClaimAsString(ClaimsEnum.EMAIL.getName());

		this.user = User.builder()
						.id(subject)
						.givenName(givenName)
						.familyName(familyName)
						.middleName(middleName)
						.picture(picture)
						.email(email)
						.name(name)
						.build();
	}


	public Claim getClaim(String name) {
		return claims != null ? claims.get(name) : null;
	}

	public String getClaimAsString(String name) {
		final Claim claim = getClaim(name);
		return claim != null ? claim.asString() : null;
	}

	// general claims getters
	public Platform getPlatform() {
		final Claim claim = getClaim(ClaimsEnum.TOOL_PLATFORM.getName());
		return claim != null ? claim.as(Platform.class) : null;
	}

	public Context getContext() {
		final Claim claim = getClaim(ClaimsEnum.CONTEXT.getName());
		return claim != null ? claim.as(Context.class) : null;
	}

	public ResourceLink getResourceLink() {
		final Claim claim = getClaim(ClaimsEnum.RESOURCE_LINK.getName());
		return claim != null ? claim.as(ResourceLink.class) : null;
	}

	public NamesRoleService getNameRoleService() {
		final Claim claim = getClaim(ClaimsEnum.RESOURCE_LINK.getName());
		return claim != null ? claim.as(NamesRoleService.class) : null;
	}

	public List<String> getRoles() {
		final Claim claim = getClaim(ClaimsEnum.ROLES.getName());
		return claim != null ? claim.asList(String.class) : null;
	}

	public Object getCustomParameter(String name) {
		final Claim claim = getClaim(ClaimsEnum.CUSTOM.getName());
		if (claim != null) {
			return claim.asMap().get(name);
		}
		return null;
	}

	// roles commodity methods
	public boolean isLearner() {
		return getRoles() != null && getRoles().contains(RolesEnum.LEARNER.getName());
	}

	public boolean isInstructor() {
		return getRoles() != null && getRoles().contains(RolesEnum.INSTRUCTOR.getName());
	}
}

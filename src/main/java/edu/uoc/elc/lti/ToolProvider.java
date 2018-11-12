package edu.uoc.elc.lti;

import com.auth0.jwk.InvalidPublicKeyException;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import edu.uoc.elc.lti.jwt.AlgorithmFactory;
import lombok.Getter;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class ToolProvider {

	private final static List<String> ALLOWED_MESSAGE_TYPES = Arrays.asList("LtiResourceLinkRequest");
	private final static String VERSION = "1.3.0";
	private final static long _5_MINUTES = 5 * 60;
	private final static long _1_YEAR = 365 * 24 * 60 * 60;

	Map<String, Claim> claims;

	@Getter
	String issuer;
	@Getter
	List<String> audience;

	String kid;
	@Getter
	Date issuedAt;
	@Getter
	Date expiresAt;

	@Getter
	private User user;

	private ToolDefinition toolDefinition;

	@Getter
	private String locale;

	@Getter
	private boolean valid;
	@Getter
	private String reason;

	public ToolProvider(String clientId, String keySetUrl, String accessTokenUrl, String privateKey, String publicKey) {
		this.toolDefinition = ToolDefinition.builder()
						.clientId(clientId)
						.keySetUrl(keySetUrl)
						.accessTokenUrl(accessTokenUrl)
						.privateKey(privateKey)
						.publicKey(publicKey)
						.build();
	}

	public boolean validate(String token) {
	 return validate(token, true);
	}

	public boolean validate(String token, boolean checkDelay) {
		valid = false;
		try {
			decode(token);

			// validate lti headers
			JwkProvider provider = new UrlJwkProvider(new URL(toolDefinition.getKeySetUrl()));
			Jwk jwk = provider.get(this.kid);

			verify(token, jwk, checkDelay);

			// message type
			final Claim messageTypeClaim = getClaim(ClaimsEnum.MESSAGE_TYPE.getName());
			if (messageTypeClaim == null || !ALLOWED_MESSAGE_TYPES.contains(messageTypeClaim.asString())) {
				reason = "Unknown Message Type";
				valid = false;
				return valid;
			}

			// version
			final Claim versionClaim = getClaim(ClaimsEnum.VERSION.getName());
			if (versionClaim == null || !VERSION.equals(versionClaim.asString())) {
				reason = "Invalid Version";
				valid = false;
				return valid;
			}
			valid = true;
		} catch (Throwable t) {
			reason = t.getMessage();
		}

		return valid;
	}

	private void verify(String token, Jwk jwk, boolean checkDelay) throws InvalidPublicKeyException {
		Algorithm algorithm = AlgorithmFactory.createAlgorithm(jwk);

		final Verification verifierBuilder = JWT.require(algorithm);

		if (checkDelay) {
			verifierBuilder.acceptIssuedAt(_5_MINUTES);
		} else {
			verifierBuilder.acceptLeeway(_1_YEAR); // only for test!!
		}

		JWTVerifier verifier = verifierBuilder.build();
		verifier.verify(token);
	}

	public void decode(String token) {
		try {

			// validate and decode token
			DecodedJWT jwt = JWT.decode(token);

			final Claim kidClaim = jwt.getHeaderClaim(ClaimsEnum.KID.getName());
			if (kidClaim == null) {
				throw new InvalidLTICallException("kid header not found");
			}
			this.kid = kidClaim.asString();

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

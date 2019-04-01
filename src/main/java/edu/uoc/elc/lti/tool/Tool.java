package edu.uoc.elc.lti.tool;

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
import edu.uoc.elc.lti.exception.BadToolProviderConfigurationException;
import edu.uoc.elc.lti.exception.InvalidLTICallException;
import edu.uoc.elc.lti.exception.InvalidTokenException;
import edu.uoc.elc.lti.jwt.AlgorithmFactory;
import edu.uoc.elc.lti.platform.AccessTokenResponse;
import edu.uoc.elc.lti.platform.RequestHandler;
import edu.uoc.elc.lti.tool.deeplinking.Settings;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class Tool {

	private final static String VERSION = "1.3.0";
	private final static long _5_MINUTES = 5 * 60;
	private final static long _1_YEAR = 365 * 24 * 60 * 60;

	Map<String, Claim> claims;

	@Getter
	String issuer;
	@Getter
	List<String> audience;

	@Getter
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

	private AccessTokenResponse accessTokenResponse;

	public Tool(String name, String clientId, String keySetUrl, String accessTokenUrl, String privateKey, String publicKey) {
		this.toolDefinition = ToolDefinition.builder()
						.clientId(clientId)
						.name(name)
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
			final Claim messageTypeClaim = getClaim(ClaimsEnum.MESSAGE_TYPE);
			if (messageTypeClaim == null) {
				reason = "Unknown Message Type";
				valid = false;
				return false;
			}

			try {
				MessageTypesEnum.valueOf(messageTypeClaim.asString());
			} catch (IllegalArgumentException e) {
				reason = "Unknown Message Type";
				valid = false;
				return false;
			}

			// version
			final Claim versionClaim = getClaim(ClaimsEnum.VERSION);
			if (versionClaim == null || !VERSION.equals(versionClaim.asString())) {
				reason = "Invalid Version";
				valid = false;
				return false;
			}
			valid = true;
		} catch (Throwable t) {
			reason = t.getMessage();
		}

		return valid;
	}

	private void verify(String token, Jwk jwk, boolean checkDelay) throws InvalidPublicKeyException {
		Algorithm algorithm = AlgorithmFactory.createAlgorithm(jwk);

		assert algorithm != null;
		final Verification verifierBuilder = JWT.require(algorithm);

		if (checkDelay) {
			verifierBuilder.acceptIssuedAt(_5_MINUTES);
		} else {
			verifierBuilder.acceptLeeway(_1_YEAR); // only for test!!
		}

		JWTVerifier verifier = verifierBuilder.build();
		verifier.verify(token);
	}

	void decode(String token) {
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
			this.locale = getClaimAsString(ClaimsEnum.LOCALE);

		} catch (JWTDecodeException exception){
			//Invalid token
			throw new InvalidTokenException("JWT is invalid");
		}
	}

	public AccessTokenResponse getAccessToken() throws IOException, BadToolProviderConfigurationException {
		if (!this.isValid()) {
			return null;
		}

		if (accessTokenResponse == null) {
			RequestHandler requestHandler = new RequestHandler(kid, toolDefinition);
			accessTokenResponse = requestHandler.getAccessToken();
		}

		return accessTokenResponse;
	}

	private void createUser(String subject) {
		this.user = User.builder()
						.id(subject)
						.givenName(getClaimAsString(ClaimsEnum.GIVEN_NAME))
						.familyName(getClaimAsString(ClaimsEnum.FAMILY_NAME))
						.middleName(getClaimAsString(ClaimsEnum.MIDDLE_NAME))
						.picture(getClaimAsString(ClaimsEnum.PICTURE))
						.email(getClaimAsString(ClaimsEnum.EMAIL))
						.name(getClaimAsString(ClaimsEnum.NAME))
						.build();
	}


	private Claim getClaim(ClaimsEnum name) {
		return claims != null ? claims.get(name.getName()) : null;
	}

	private String getClaimAsString(ClaimsEnum name) {
		final Claim claim = getClaim(name);
		return claim != null ? claim.asString() : null;
	}

	// general claims getters
	public Platform getPlatform() {
		final Claim claim = getClaim(ClaimsEnum.TOOL_PLATFORM);
		return claim != null ? claim.as(Platform.class) : null;
	}

	public Context getContext() {
		final Claim claim = getClaim(ClaimsEnum.CONTEXT);
		return claim != null ? claim.as(Context.class) : null;
	}

	public ResourceLink getResourceLink() {
		final Claim claim = getClaim(ClaimsEnum.RESOURCE_LINK);
		return claim != null ? claim.as(ResourceLink.class) : null;
	}

	public NamesRoleService getNameRoleService() {
		final Claim claim = getClaim(ClaimsEnum.NAMES_ROLE_SERVICE);
		return claim != null ? claim.as(NamesRoleService.class) : null;
	}

	public AssignmentGradeService getAssignmentGradeService() {
		final Claim claim = getClaim(ClaimsEnum.ASSIGNMENT_GRADE_SERVICE);
		return claim != null ? claim.as(AssignmentGradeService.class) : null;

	}

	public Settings getDeepLinkingSettings() {
		if (!isDeepLinkingRequest()) {
			return null;
		}
		final Claim claim = getClaim(ClaimsEnum.DEEP_LINKING_SETTINGS);
		return claim != null ? claim.as(Settings.class) : null;
	}

	public List<String> getRoles() {
		final Claim claim = getClaim(ClaimsEnum.ROLES);
		return claim != null ? claim.asList(String.class) : null;
	}

	public Object getCustomParameter(String name) {
		final Claim claim = getClaim(ClaimsEnum.CUSTOM);
		if (claim != null) {
			return claim.asMap().get(name);
		}
		return null;
	}

	public MessageTypesEnum getMessageType() {
		final Claim messageTypeClaim = getClaim(ClaimsEnum.MESSAGE_TYPE);
		return MessageTypesEnum.valueOf(messageTypeClaim.asString());
	}

	public boolean isDeepLinkingRequest() {
		return MessageTypesEnum.LtiDeepLinkingRequest == getMessageType();
	}

	public boolean isResourceLinkLaunch() {
		return MessageTypesEnum.LtiResourceLinkRequest == getMessageType();
	}

	// roles commodity methods
	public boolean isLearner() {
		return getRoles() != null && getRoles().contains(RolesEnum.LEARNER.getName());
	}

	public boolean isInstructor() {
		return getRoles() != null && getRoles().contains(RolesEnum.INSTRUCTOR.getName());
	}
}

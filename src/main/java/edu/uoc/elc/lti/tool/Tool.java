package edu.uoc.elc.lti.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uoc.elc.lti.exception.BadToolProviderConfigurationException;
import edu.uoc.elc.lti.exception.InvalidLTICallException;
import edu.uoc.elc.lti.exception.InvalidTokenException;
import edu.uoc.elc.lti.jwt.LtiSigningKeyResolver;
import edu.uoc.elc.lti.platform.AccessTokenResponse;
import edu.uoc.elc.lti.platform.RequestHandler;
import edu.uoc.elc.lti.tool.deeplinking.Settings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Getter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class Tool {

	private final static String VERSION = "1.3.0";
	private final static long _5_MINUTES = 5 * 60;
	private final static long _1_YEAR = 365 * 24 * 60 * 60;

	Claims claims;

	@Getter
	String issuer;
	@Getter
	String audience;

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

	private ObjectMapper objectMapper = new ObjectMapper();

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

			decodeAndVerify(token, checkDelay);

			// message type
			final String messageTypeClaim = getClaimAsString(ClaimsEnum.MESSAGE_TYPE);
			if (messageTypeClaim == null) {
				reason = "Unknown Message Type";
				valid = false;
				return false;
			}

			try {
				MessageTypesEnum.valueOf(messageTypeClaim);
			} catch (IllegalArgumentException e) {
				reason = "Unknown Message Type";
				valid = false;
				return false;
			}

			// version
			final String versionClaim = getClaimAsString(ClaimsEnum.VERSION);
			if (versionClaim == null || !VERSION.equals(versionClaim)) {
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

	void decodeAndVerify(String token, boolean checkDelay) {
		LtiSigningKeyResolver ltiSigningKeyResolver = new LtiSigningKeyResolver(toolDefinition.getKeySetUrl());

		try {
			Jws<Claims> jws = Jwts.parser()
							.setSigningKeyResolver(ltiSigningKeyResolver)
							.setAllowedClockSkewSeconds(checkDelay ? _5_MINUTES : _1_YEAR)
							.parseClaimsJws(token);

			this.kid = jws.getHeader().getKeyId();
			if (this.kid == null) {
				throw new InvalidLTICallException("kid header not found");
			}

			// get the standard JWT payload claims
			this.issuer = jws.getBody().getIssuer();
			this.audience = jws.getBody().getAudience();
			this.issuedAt = jws.getBody().getIssuedAt();
			this.expiresAt = jws.getBody().getExpiration();

			// get the private claims (contains all LTI claims)
			this.claims = jws.getBody();

			// create the user attribute
			createUser(jws.getBody().getSubject());

			// update locale attribute
			this.locale = getClaimAsString(ClaimsEnum.LOCALE);

		} catch (JwtException ex) {
			//Invalid token
			throw new InvalidTokenException("JWT is invalid");
		}

	}

/*
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
*/

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


	private <T> T getClaim(ClaimsEnum claim, Class<T> returnClass) {
		if (claims == null || !claims.containsKey(claim.getName())) {
			return null;
		}
		final Object o = claims.get(claim.getName());
		// doing this way because Jwts deserialize json classes as LinkedHashMap
		return objectMapper.convertValue(o, returnClass);
	}

	private String getClaimAsString(ClaimsEnum name) {
		return claims.get(name.getName(), String.class);
	}

	// general claims getters
	public Platform getPlatform() {
		return getClaim(ClaimsEnum.TOOL_PLATFORM, Platform.class);
	}

	public Context getContext() {
		return getClaim(ClaimsEnum.CONTEXT, Context.class);
	}

	public ResourceLink getResourceLink() {
		return getClaim(ClaimsEnum.RESOURCE_LINK, ResourceLink.class);
	}

	public NamesRoleService getNameRoleService() {
		return getClaim(ClaimsEnum.NAMES_ROLE_SERVICE, NamesRoleService.class);
	}

	public AssignmentGradeService getAssignmentGradeService() {
		return getClaim(ClaimsEnum.ASSIGNMENT_GRADE_SERVICE, AssignmentGradeService.class);
	}

	public String getAuthorizedPart() {
		return getClaimAsString(ClaimsEnum.AUTHORIZED_PART);
	}
	public String getDeploymentId() {
		if (!isDeepLinkingRequest()) {
			return null;
		}
		return getClaimAsString(ClaimsEnum.DEPLOYMENT_ID);
	}

	public Settings getDeepLinkingSettings() {
		if (!isDeepLinkingRequest()) {
			return null;
		}
		return getClaim(ClaimsEnum.DEEP_LINKING_SETTINGS, Settings.class);
	}

	public List<String> getRoles() {
		Class<List<String>> rolesClass = (Class) List.class;
		return getClaim(ClaimsEnum.ROLES, rolesClass);
	}

	public Object getCustomParameter(String name) {
		Class<Map<String, Object>> customClass = (Class) Map.class;
		final Map<String, Object> claim = getClaim(ClaimsEnum.CUSTOM, customClass);
		if (claim != null) {
			return claim.get(name);
		}
		return null;
	}

	public MessageTypesEnum getMessageType() {
		try {
			return MessageTypesEnum.valueOf(getClaimAsString(ClaimsEnum.MESSAGE_TYPE));
		} catch (IllegalArgumentException ignored) {
			return null;
		}
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

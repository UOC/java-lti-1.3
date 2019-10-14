package edu.uoc.elc.lti.tool;

import edu.uoc.elc.lti.exception.BadToolProviderConfigurationException;
import edu.uoc.elc.lti.exception.InvalidLTICallException;
import edu.uoc.elc.lti.exception.InvalidTokenException;
import edu.uoc.elc.lti.platform.AccessTokenResponse;
import edu.uoc.elc.lti.platform.RequestHandler;
import edu.uoc.elc.lti.platform.deeplinking.DeepLinkingClient;
import edu.uoc.elc.lti.tool.claims.ClaimAccessor;
import edu.uoc.elc.lti.tool.claims.ClaimsEnum;
import edu.uoc.elc.lti.tool.claims.JWSClaimAccessor;
import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.elc.lti.tool.oidc.AuthRequestUrlBuilder;
import edu.uoc.elc.lti.tool.oidc.LoginRequest;
import edu.uoc.elc.lti.tool.oidc.LoginResponse;
import io.jsonwebtoken.JwtException;
import lombok.Getter;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class Tool {

	private final static String VERSION = "1.3.0";

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

	private final ToolDefinition toolDefinition;
	private final ClaimAccessor claimAccessor;

	@Getter
	private String locale;

	@Getter
	private boolean valid;

	@Getter
	private String reason;

	private AccessTokenResponse accessTokenResponse;

	Tool(String name, String clientId, String keySetUrl, String accessTokenUrl, String oidcAuthUrl, String privateKey, String publicKey, ClaimAccessor claimAccessor) {
		this.toolDefinition = ToolDefinition.builder()
						.clientId(clientId)
						.name(name)
						.keySetUrl(keySetUrl)
						.accessTokenUrl(accessTokenUrl)
						.oidcAuthUrl(oidcAuthUrl)
						.privateKey(privateKey)
						.publicKey(publicKey)
						.build();
		this.claimAccessor = claimAccessor;
	}

	public boolean validate(String token) {
		valid = false;
		try {

			decodeAndVerify(token);

			// message type
			final String messageTypeClaim = this.claimAccessor.get(ClaimsEnum.MESSAGE_TYPE);
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
			final String versionClaim = this.claimAccessor.get(ClaimsEnum.VERSION);
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

	void decodeAndVerify(String token) {

		try {
			this.claimAccessor.decode(token);

			this.kid = this.claimAccessor.getKId();
			if (this.kid == null) {
				throw new InvalidLTICallException("kid header not found");
			}

			// TODO: validate id_token, if present, using rules from https://www.imsglobal.org/spec/security/v1p0/#authentication-response-validation

			// get the standard JWT payload claims
			this.issuer = this.claimAccessor.getIssuer();
			this.audience = this.claimAccessor.getAudience();
			this.issuedAt = this.claimAccessor.getIssuedAt();
			this.expiresAt = this.claimAccessor.getExpiration();

			/**
			 * 3. The Tool MUST validate that the aud (audience) Claim contains its client_id value registered as an
			 * audience with the Issuer identified by the iss (Issuer) Claim. The aud (audience) Claim MAY contain an array
			 * with more than one element. The Tool MUST reject the ID Token if it does not list the client_id as a valid
			 * audience, or if it contains additional audiences not trusted by the Tool. The request message will be
			 * rejected with a HTTP code of 401;
			 */
			if (!this.toolDefinition.getClientId().equals(this.audience)) {
				throw new InvalidLTICallException("Audience invalid");
			}

			// create the user attribute
			createUser(this.claimAccessor.getSubject());

			// update locale attribute
			this.locale = this.claimAccessor.get(ClaimsEnum.LOCALE);

		} catch (JwtException ex) {
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
						.givenName(this.claimAccessor.get(ClaimsEnum.GIVEN_NAME))
						.familyName(this.claimAccessor.get(ClaimsEnum.FAMILY_NAME))
						.middleName(this.claimAccessor.get(ClaimsEnum.MIDDLE_NAME))
						.picture(this.claimAccessor.get(ClaimsEnum.PICTURE))
						.email(this.claimAccessor.get(ClaimsEnum.EMAIL))
						.name(this.claimAccessor.get(ClaimsEnum.NAME))
						.build();
	}


	// general claims getters
	public Platform getPlatform() {
		return this.claimAccessor.get(ClaimsEnum.TOOL_PLATFORM, Platform.class);
	}

	public Context getContext() {
		return this.claimAccessor.get(ClaimsEnum.CONTEXT, Context.class);
	}

	public ResourceLink getResourceLink() {
		return this.claimAccessor.get(ClaimsEnum.RESOURCE_LINK, ResourceLink.class);
	}

	public NamesRoleService getNameRoleService() {
		return this.claimAccessor.get(ClaimsEnum.NAMES_ROLE_SERVICE, NamesRoleService.class);
	}

	public AssignmentGradeService getAssignmentGradeService() {
		return this.claimAccessor.get(ClaimsEnum.ASSIGNMENT_GRADE_SERVICE, AssignmentGradeService.class);
	}

	public String getDeploymentId() {
		if (!isDeepLinkingRequest()) {
			return null;
		}
		return this.claimAccessor.get(ClaimsEnum.DEPLOYMENT_ID);
	}

	public Settings getDeepLinkingSettings() {
		if (!isDeepLinkingRequest()) {
			return null;
		}
		return this.claimAccessor.get(ClaimsEnum.DEEP_LINKING_SETTINGS, Settings.class);
	}


	public List<String> getRoles() {
		Class<List<String>> rolesClass = (Class) List.class;
		return this.claimAccessor.get(ClaimsEnum.ROLES, rolesClass);
	}

	public Object getCustomParameter(String name) {
		Class<Map<String, Object>> customClass = (Class) Map.class;
		final Map<String, Object> claim = this.claimAccessor.get(ClaimsEnum.CUSTOM, customClass);
		if (claim != null) {
			return claim.get(name);
		}
		return null;
	}

	public MessageTypesEnum getMessageType() {
		try {
			return MessageTypesEnum.valueOf(this.claimAccessor.get(ClaimsEnum.MESSAGE_TYPE));
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

	public DeepLinkingClient getDeepLinkingClient() {
		if (!isDeepLinkingRequest()) {
			return null;
		}
		return new DeepLinkingClient(getIssuer(),
						getAudience(),
						this.claimAccessor.getAzp(),
						this.kid,
						toolDefinition.getPublicKey(),
						toolDefinition.getPrivateKey(),
						getDeploymentId(),
						getDeepLinkingSettings());
	}

	// roles commodity methods
	public boolean isLearner() {
		return getRoles() != null && getRoles().contains(RolesEnum.LEARNER.getName());
	}

	public boolean isInstructor() {
		return getRoles() != null && getRoles().contains(RolesEnum.INSTRUCTOR.getName());
	}

	// openid methods
	public LoginResponse getOidcAuthParams(LoginRequest loginRequest) {
		return LoginResponse.builder()
						.client_id(loginRequest.getClient_id() != null ? loginRequest.getClient_id() : toolDefinition.getClientId())
						.redirect_uri(loginRequest.getTarget_link_uri())
						.login_hint(loginRequest.getLogin_hint())
						.state(new BigInteger(50, new SecureRandom()).toString(16))
						.nonce(new BigInteger(50, new SecureRandom()).toString(16))
						.lti_message_hint(loginRequest.getLti_message_hint())
						.build();
	}

	public String getOidcAuthUrl(LoginResponse loginResponse) {
		return AuthRequestUrlBuilder.build(toolDefinition.getOidcAuthUrl(), loginResponse);
	}
}

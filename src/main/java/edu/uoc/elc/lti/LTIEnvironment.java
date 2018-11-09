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
	String subject;
	@Getter
	Date issuedAt;
	@Getter
	Date expiresAt;

	@Getter
	private String reason;

	public boolean validate(String token) {
		try {
			decode(token);

			// validate lti headers
			// message type
			final Claim messageTypeClaim = getClaim(ClaimsEnum.MESSAGE_TYPE.getName());
			if (messageTypeClaim == null || !ALLOWED_MESSAGE_TYPES.contains(messageTypeClaim.asString())) {
				reason = "Unknown Message Type";
				return false;
			}

			// version
			final Claim versionClaim = getClaim(ClaimsEnum.VERSION.getName());
			if (versionClaim == null || !VERSION.equals(versionClaim.asString())) {
				reason = "Invalid Version";
				return false;
			}
			return true;
		} catch (Throwable t) {
			reason = t.getMessage();
		}

		return false;
	}

	public void decode(String token) {
		try {

			// validate and decode token
			DecodedJWT jwt = JWT.decode(token);

			// get the standard JWT payload claims
			this.issuer = jwt.getIssuer();
			this.audience = jwt.getAudience();
			this.subject = jwt.getSubject();
			this.issuedAt = jwt.getIssuedAt();
			this.expiresAt = jwt.getExpiresAt();

			// get the private claims (contains all LTI claims)
			this.claims = jwt.getClaims();

		} catch (JWTDecodeException exception){
			//Invalid token
			throw new InvalidTokenException("JWT is invalid");
		}
	}


	public Claim getClaim(String name) {
		return claims != null ? claims.get(name) : null;
	}


}

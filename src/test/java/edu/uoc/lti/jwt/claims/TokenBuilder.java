package edu.uoc.lti.jwt.claims;

import edu.uoc.lti.claims.ClaimsEnum;
import edu.uoc.lti.jwt.AlgorithmFactory;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
public class TokenBuilder {
	private final static long _5_MINUTES = 5 * 30 * 1000;
	private final static String KID = "kid";
	private final static String ALG = "ALG";
	private final static String ISSUER = "iss";
	private final static String SUBJECT = "sub";
	private final static String AUDIENCE = "aud";
	private final static String EXPIRATION = "exp";
	private final static String ISSUED_AT = "iat";
	private final static String DEPLOYMENT_ID = ClaimsEnum.DEPLOYMENT_ID.getName();
	private final static List<String> PAYLOAD_SPECIAL_KEYS = Arrays.asList(ISSUER, SUBJECT, AUDIENCE, EXPIRATION, ISSUED_AT, DEPLOYMENT_ID);

	private final String kid;
	private final String issuer;
	private final String audience;
	private final String deploymentId;
	private final String publicKey;
	private final String privateKey;

	public String build(TestLaunch testLaunch) {
		AlgorithmFactory algorithmFactory = new AlgorithmFactory(publicKey, privateKey);

		final JwtBuilder builder = Jwts.builder()
						.signWith(algorithmFactory.getPrivateKey());

		if (testLaunch.getHeader() != null) {
			for (Map.Entry<String, Object> entry : testLaunch.getHeader().entrySet()) {
				if (!ALG.equals(entry.getKey())) {
					builder.setHeaderParam(entry.getKey(), entry.getValue());
				}
			}
		} else {
			builder.setHeaderParam(KID, this.kid);
		}

		if (testLaunch.getPayload() != null) {
			for (Map.Entry<String, Object> entry : testLaunch.getPayload().entrySet()) {
				if (isSpecial(entry.getKey())) {
					setSpecial(entry.getKey(), entry.getValue(), builder, testLaunch.getSpecialFieldsToKeepFromPayload());
				} else {
					// is a normal claim
					builder.claim(entry.getKey(), entry.getValue());
				}
			}
		}
		return builder.compact();
	}

	private void setSpecial(String key, Object value, JwtBuilder builder, List<String> excludedKeys) {
		if (ISSUER.equals(key)) {
			builder.setIssuer(isExcluded(key, excludedKeys) ? value.toString() : this.issuer);
		}
		if (SUBJECT.equals(key)) {
			builder.setSubject(value.toString());
		}
		if (AUDIENCE.equals(key)) {
			builder.setAudience(isExcluded(key, excludedKeys) ? value.toString() : this.audience);
		}
		if (EXPIRATION.equals(key)) {
			builder.setExpiration(isExcluded(key, excludedKeys) ? new Date((Integer) value) : new Date(System.currentTimeMillis() + _5_MINUTES));
		}
		if (ISSUED_AT.equals(key)) {
			builder.setIssuedAt(isExcluded(key, excludedKeys) ? new Date((Integer) value) : new Date());
		}
		if (DEPLOYMENT_ID.equals(key)) {
			builder.claim(DEPLOYMENT_ID, isExcluded(key, excludedKeys) ? value.toString() : this.deploymentId);
		}
	}

	private boolean isExcluded(String key, List<String> excludedKeys) {
		return (excludedKeys == null || excludedKeys.size() == 0) ? false : excludedKeys.contains(key);
	}

	private boolean isSpecial(String key) {
		return PAYLOAD_SPECIAL_KEYS.contains(key);
	}
}

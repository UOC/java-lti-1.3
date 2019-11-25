package edu.uoc.lti.jwt.claims;

import edu.uoc.lti.jwt.AlgorithmFactory;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Map;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
public class TokenBuilder {
	private final static long _5_MINUTES = 5 * 30 * 1000;

	private final String kid;
	private final String issuer;
	private final String audience;
	private final String publicKey;
	private final String privateKey;

	public String build(Token token) {
		AlgorithmFactory algorithmFactory = new AlgorithmFactory(publicKey, privateKey);
		final JwtBuilder builder = Jwts.builder()
						.setHeaderParam("kid", kid)
						.setIssuer(issuer)
						.setAudience(audience)
						.setIssuedAt(new Date())
						.setExpiration(new Date(System.currentTimeMillis() + _5_MINUTES))
						.signWith(algorithmFactory.getPrivateKey());

		// add claims
		if (token.getClaims() != null) {
			builder.addClaims(token.getClaims());
		}
		return builder.compact();
	}
}

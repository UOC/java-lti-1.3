package edu.uoc.elc.lti.platform;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import java.security.SecureRandom;
import java.util.Date;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
class ClientCredentialsJWT {

	private final static long _5_MINUTES = 5 * 30 * 1000;
	private final String publicKey;
	private final String privateKey;
	private final String kid;
	private final String toolName;
	private final String clientId;
	private final String oauth2Url;

	private SecureRandom secureRandom = new SecureRandom();

	String build() {
		AlgorithmFactory algorithmFactory = new AlgorithmFactory(publicKey, privateKey);
		byte bytes[] = new byte[10];
		secureRandom.nextBytes(bytes);
		return Jwts.builder()
						.setHeaderParam("kid", kid)
						.setIssuer(toolName)
						.setSubject(clientId)
						.setAudience(oauth2Url)
						.setIssuedAt(new Date())
						.setExpiration(new Date(System.currentTimeMillis() + _5_MINUTES))
						.signWith(algorithmFactory.getPrivateKey())
						.setId(new String(bytes))
						.compact();
	}
}

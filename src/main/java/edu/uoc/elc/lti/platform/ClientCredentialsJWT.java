package edu.uoc.elc.lti.platform;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import edu.uoc.elc.lti.exception.BadToolProviderConfigurationException;
import edu.uoc.elc.lti.exception.LTISignatureException;
import lombok.RequiredArgsConstructor;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
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
		try {
			AlgorithmFactory algorithmFactory = new AlgorithmFactory(publicKey, privateKey);
			Algorithm algorithm = algorithmFactory.getAlgorithm();
			byte bytes[] = new byte[10];
			secureRandom.nextBytes(bytes);
			return JWT.create()
							.withKeyId(kid)
							.withIssuer(toolName)
							.withSubject(clientId)
							.withAudience(oauth2Url)
							.withIssuedAt(new Date())
							.withExpiresAt(new Date(System.currentTimeMillis() + _5_MINUTES))
							.withJWTId(new String(bytes))
							.sign(algorithm);
		} catch (JWTCreationException exception){
			//Invalid Signing configuration / Couldn't convert Claims.
			throw new LTISignatureException(exception);
		}
	}
}

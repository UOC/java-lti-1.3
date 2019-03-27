package edu.uoc.elc.lti.platform;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import edu.uoc.elc.lti.exception.BadToolProviderConfigurationException;
import edu.uoc.elc.lti.exception.LTISignatureException;
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
class ClientCredentialsJWT {

	private final static long _5_MINUTES = 5 * 30 * 1000;
	private RSAPublicKey publicKey;
	private RSAPrivateKey privateKey;
	private String kid;
	private String toolName;
	private String clientId;
	private String oauth2Url;

	private SecureRandom secureRandom;

	ClientCredentialsJWT(String publicKey, String privateKey, String kid, String toolName, String clientId, String oauth2Url) {
		this.kid = kid;
		this.toolName = toolName;
		this.clientId = clientId;
		this.oauth2Url = oauth2Url;
		setUpKeys(publicKey, privateKey);
		this.secureRandom = new SecureRandom();
	}

	String build() {
		try {
			Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
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

	private void setUpKeys(String publicKeyString, String privateKeyString) {
		KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("RSA");
			byte[] encodedPb = Base64.getDecoder().decode(publicKeyString);
			X509EncodedKeySpec keySpecPb = new X509EncodedKeySpec(encodedPb);
			publicKey = (RSAPublicKey) kf.generatePublic(keySpecPb);

			DerInputStream derReader = new DerInputStream(Base64.getDecoder().decode(privateKeyString));

			DerValue[] seq = derReader.getSequence(0);

			if (seq.length < 9) {
				throw new GeneralSecurityException("Could not parse a PKCS1 private key.");
			}

			// skip version seq[0];
			BigInteger modulus = seq[1].getBigInteger();
			BigInteger publicExp = seq[2].getBigInteger();
			BigInteger privateExp = seq[3].getBigInteger();
			BigInteger prime1 = seq[4].getBigInteger();
			BigInteger prime2 = seq[5].getBigInteger();
			BigInteger exp1 = seq[6].getBigInteger();
			BigInteger exp2 = seq[7].getBigInteger();
			BigInteger crtCoef = seq[8].getBigInteger();

			RSAPrivateCrtKeySpec keySpecPv = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2, exp1, exp2, crtCoef);

			privateKey = (RSAPrivateKey) kf.generatePrivate(keySpecPv);

		} catch (GeneralSecurityException | IOException e) {
			throw new BadToolProviderConfigurationException(e);
		}
	}
}

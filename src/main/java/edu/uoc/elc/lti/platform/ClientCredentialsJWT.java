package edu.uoc.elc.lti.platform;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class ClientCredentialsJWT {

	private final static long _5_MINUTES = 5 * 30 * 1000;
	private RSAPublicKey publicKey;
	private RSAPrivateKey privateKey;
	private String kid;
	private String toolName;
	private String clientId;
	private String oauth2Url;

	private SecureRandom secureRandom;

	public ClientCredentialsJWT(String publicKey, String privateKey, String kid, String toolName, String clientId, String oauth2Url) throws InvalidKeyException {
		this.kid = kid;
		this.toolName = toolName;
		this.clientId = clientId;
		this.oauth2Url = oauth2Url;
		setUpKeys(publicKey, privateKey);
		this.secureRandom = new SecureRandom();
	}

	public String build() {
		try {
			Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
			byte bytes[] = new byte[10];
			secureRandom.nextBytes(bytes);
			String token = JWT.create()
							.withKeyId(kid)
							.withIssuer(toolName)
							.withSubject(clientId)
							.withAudience(oauth2Url)
							.withIssuedAt(new Date())
							.withExpiresAt(new Date(System.currentTimeMillis() + _5_MINUTES))
							.withJWTId(new String(bytes))
							.sign(algorithm);
			return token;
		} catch (JWTCreationException exception){
			//Invalid Signing configuration / Couldn't convert Claims.
		}
		return null;
	}

	private void setUpKeys(String publicKeyString, String privateKeyString) {
		KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("RSA");
			byte[] encodedPv = Base64.getDecoder().decode(privateKeyString);
			PKCS8EncodedKeySpec keySpecPv = new PKCS8EncodedKeySpec(encodedPv);
			privateKey = (RSAPrivateKey) kf.generatePrivate(keySpecPv);

			byte[] encodedPb = Base64.getDecoder().decode(publicKeyString);
			X509EncodedKeySpec keySpecPb = new X509EncodedKeySpec(encodedPb);
			publicKey = (RSAPublicKey) kf.generatePublic(keySpecPb);

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {

		}
	}
}

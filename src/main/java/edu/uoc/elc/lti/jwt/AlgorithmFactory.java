package edu.uoc.elc.lti.jwt;

import com.auth0.jwk.InvalidPublicKeyException;
import com.auth0.jwk.Jwk;
import com.auth0.jwt.algorithms.Algorithm;

import java.security.interfaces.RSAPublicKey;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class AlgorithmFactory {
	public static Algorithm createAlgorithm(Jwk jwt) throws InvalidPublicKeyException {
		if ("RS256".equals(jwt.getAlgorithm())) {
			return Algorithm.RSA256((RSAPublicKey) jwt.getPublicKey(), null);
		}
		return null;
	}
}

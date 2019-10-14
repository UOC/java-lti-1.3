package edu.uoc.elc.lti.tool.claims;

import io.jsonwebtoken.Jwts;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class TestJWSClaimAccessor extends JWSClaimAccessor {
	private final static long _1_YEAR = 365 * 24 * 60 * 60;

	public TestJWSClaimAccessor(String keySetUrl) {
		super(keySetUrl);
	}

	@Override
	public void decode(String token) {
		this.jws = Jwts.parser()
						.setSigningKeyResolver(ltiSigningKeyResolver)
						.setAllowedClockSkewSeconds(_1_YEAR)
						.parseClaimsJws(token);
	}
}

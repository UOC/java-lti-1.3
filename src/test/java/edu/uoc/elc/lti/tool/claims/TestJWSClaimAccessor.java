package edu.uoc.elc.lti.tool.claims;

import java.util.Date;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class TestJWSClaimAccessor extends JWSClaimAccessor {
	private final static long _5_YEAR = 5 * 365 * 24 * 60 * 60;

	public TestJWSClaimAccessor(String keySetUrl) {
		super(keySetUrl);
		allowedClockSkewSeconds = _5_YEAR;
	}

	@Override
	public Date getExpiration() {
		return new Date(System.currentTimeMillis() + _5_YEAR);
	}
}

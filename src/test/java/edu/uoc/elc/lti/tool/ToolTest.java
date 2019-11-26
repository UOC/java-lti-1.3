package edu.uoc.elc.lti.tool;

import edu.uoc.elc.lti.tool.oidc.InMemoryOIDCLaunchSession;
import edu.uoc.lti.accesstoken.AccessTokenRequestBuilder;
import edu.uoc.lti.accesstoken.JSONAccessTokenRequestBuilderImpl;
import edu.uoc.lti.claims.ClaimAccessor;
import edu.uoc.lti.jwt.claims.JWSClaimAccessor;
import edu.uoc.lti.jwt.claims.TestLaunch;
import edu.uoc.lti.jwt.claims.TestLaunchLoader;
import edu.uoc.lti.jwt.claims.TokenBuilder;
import edu.uoc.lti.oidc.OIDCLaunchSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class ToolTest {
	private final static String INVALID_LAUNCHES_DIR = "/invalid/";
	private final static String VALID_LAUNCHES_DIR = "/valid/";

	private Tool sut;
	private TokenBuilder tokenBuilder;

	@Before
	public void setUp() {
		String keysetUrl = "https://lti-ri.imsglobal.org/platforms/68/platform_keys/60.json";

		// since it's a test, we don't check expiration
		ClaimAccessor claimAccessor = new JWSClaimAccessor(keysetUrl);
		OIDCLaunchSession launchSession = new InMemoryOIDCLaunchSession();
		AccessTokenRequestBuilder accessTokenRequestBuilder = new JSONAccessTokenRequestBuilderImpl();
		this.sut = new Tool(
						"Universitat Oberta de Catalunya",
						"Universitat Oberta de Catalunya",
						"https://www.uoc.edu",
						"testDeployment",
						keysetUrl,
						"https://lti-ri.imsglobal.org/platforms/68/access_tokens",
						"https://lti-ri.imsglobal.org/platforms/68/authorizations/new",
						"MIIEpQIBAAKCAQEAwDN0DFZnsxETm+lzQh7dsbBX6srNGqQ+Ougc485pqys81TzjoWDsubj70NdTge8DO3ycJbPpDsTH5P1Ea+vCHq/h1SzFLLF0yLw5Y2sWrLlDF9PrvR9yApu0bZ3zNTl55B4EblYns3j3JydwfnnLuNeNVpwv7wosLZKhMvg9CQv39prJj8xI+l5FoRkhKS86tl5PrU4Xld+jBo84GWAx0SYxbGF/vR9ve9lnErNWLv8ozYC2J9FusberZrsZ8M0mpNR1vJMayqDIwMPX5rsKtWZJh33XwAKRsEIxWT0WUGM54SUc0jrszapXfOoznblwmcAd/MVLXOlgOoaeiQbxFwIDAQABAoIBAQCV6UwRt29I1v/xcuPoZcTLLF1Wj/nsVsEHFzY5ZS08Sgw30jdGDyB1N9iJqvnCddeSlX4BVlNopom3jOfKOWf+oXbG3BWq53rMeytT5RoZfKLdOfk8WQOHD0Vq3nDtSbhN+i/Ak8iBjs9Ppc5zh7IjqVTfghFNJCUTIrRC2iZZH7b+deK4vWd6GnIubftHs3H/worivZFPXm+qG0+mSLo9DdxuySQmG0KwwJOz/RwW7oKME0Y05+eUA0E2EUODEzf/iTBsnZxDJ1PVuJvWiSl4uDh167G4mnscPiLYcDc4xJ4rmr+mDoekWMn70iNzW4xLZQaLMLy/RtKoclFVDQZxAoGBAOxQJOm96wBVT+zfaO/gork6DwOFxdf765C8sNsRSwygzrh03UYbO0bFwi2r2/bBXvyt69DGxKapUhDH0z5inVjsZwHAdrHAGpf+jA9smumsJI25/AXmp9piHSx1SI5AjygViCETSD6ItWhTbEBJn0rwhAs+f8hFGymwF11T4HH/AoGBANA2h5O7vlYGHmgEVyol/oRiXrggx2z0iVkn3RUhzWqXqedaQjp8zyA+FG/7fPsYah9R1sgKZ2aey+npo7RKQgmsgJWTQsXq+Fcvivw1z0xIJ8AmK4lhTHlw/B9TT3cxzy1IqAUfP4luJXnsiZ45G78XD6Q5Ftd8Fxn+PhDq4NDpAoGBAIClyNKvH6ZYy2Aq59ffNPcdklrakrBYZw+uiaFZMsA3MxLcHDI0VPrcYi+25dLZxrpMfJp1+0y31QNppajKytpEKHedrYBrEo84dktXVqZrnqLBY2BbB3ot+6/eUZePsd+iiS9obeYNSqT29XGyItQLR/dPGQWQCY+SW8XlCcVFAoGBAMOP5AmDVkPQHXEPWptQ8lx/VH3W89jHWdXulj2J8TlD2CZfZUMwBQ9An8uKR5pEFTDzmitrcjE1x0sd0k+9S4dwiZlzpkzk5HpnQkCffeQlBYj5kPzI8Z5C29vEUSggFXpv+rhM4E2BshtxatS8yO3TiDJ0GJsuhzg3zy3unlg5AoGAZv9YjAoQdxrRl1AiWntZm8BXxXwXEVEG2Wbr9a6+OPiaQXgpk11nf2QnEPmFZ2vFSC6YWEba34qs3kNBg5hp0ttHNyzwNxL7B+4sRoN08lVtOYUOGeqg3yGkdQCxW4RMiAL37+01rrbSGUxieWjP3V4y5gP2//mnlan/s4lbkOc=",
						"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwDN0DFZnsxETm+lzQh7dsbBX6srNGqQ+Ougc485pqys81TzjoWDsubj70NdTge8DO3ycJbPpDsTH5P1Ea+vCHq/h1SzFLLF0yLw5Y2sWrLlDF9PrvR9yApu0bZ3zNTl55B4EblYns3j3JydwfnnLuNeNVpwv7wosLZKhMvg9CQv39prJj8xI+l5FoRkhKS86tl5PrU4Xld+jBo84GWAx0SYxbGF/vR9ve9lnErNWLv8ozYC2J9FusberZrsZ8M0mpNR1vJMayqDIwMPX5rsKtWZJh33XwAKRsEIxWT0WUGM54SUc0jrszapXfOoznblwmcAd/MVLXOlgOoaeiQbxFwIDAQAB",
						claimAccessor,
						launchSession,
						null,
						null,
						accessTokenRequestBuilder);
		this.tokenBuilder = new TokenBuilder(
						"pUaAdoefCd5Tg-TC807mjReHjS3ec8nsY9-nrpWDQS0",
						"https://www.uoc.edu",
						"Universitat Oberta de Catalunya",
						"testDeployment",
						"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0MkrXiaPUxRzGOwrmSQKlDXUFn9veJlUybecFN07QIlqU758DxsSAvv8ZGPnzQVBKy9ykoXoaxecpKEIe/kK5qPbAVvnK6lGFbUl1QkK/NnHwf2zDy4S1f/OLh0oyKcI7izkUUl4lLzim5jsNChxpY00xqi5lh8Sk2qRppbbUR8rojTnl64mZq3P6Rl3GlXKj4GpRCFTdWb4Gyrx6KU6IZ2rufnGSSfRK4jnuASvTBW4PBbipxXN3mjPukx0tsWIYHh3hhv0DZUnOPBShPf0aTeT4c8+rjZ7EhDZJJr/OlLW9d+wonFKIz+fCdjzBxdGUEdoMsU7pW5xsmp8obAHUQIDAQAB",
						"MIIEpQIBAAKCAQEA0MkrXiaPUxRzGOwrmSQKlDXUFn9veJlUybecFN07QIlqU758DxsSAvv8ZGPnzQVBKy9ykoXoaxecpKEIe/kK5qPbAVvnK6lGFbUl1QkK/NnHwf2zDy4S1f/OLh0oyKcI7izkUUl4lLzim5jsNChxpY00xqi5lh8Sk2qRppbbUR8rojTnl64mZq3P6Rl3GlXKj4GpRCFTdWb4Gyrx6KU6IZ2rufnGSSfRK4jnuASvTBW4PBbipxXN3mjPukx0tsWIYHh3hhv0DZUnOPBShPf0aTeT4c8+rjZ7EhDZJJr/OlLW9d+wonFKIz+fCdjzBxdGUEdoMsU7pW5xsmp8obAHUQIDAQABAoIBAQC9MX4t++0mkMJXlDNRu1omwbxlgqcFdpRhkhNKyMqXia4jItqSaaphr+wfIHT90MQkGQPOiK9609OrTw08IgnhxBuB2MDbTLHom9UjfeVKCSK9xGKM3+hLqVkxalT5tnseMOnYSyaMSbli3Ck2fmu1ZAat+ljqE1Am64v+lHc6wsq4tUXvZ6/dIthvcnbuPP0RwdZH05GWqiI8sUz0W2zi7rqFJadaEZbxb/WFhO51MbyrZh34/MpxfqJEIkFnrzt+FgJ4F7mbQrv+XXo1mQ2I0MCknzWspYLwCsVyGV9jSuK+zmD9R/JGByf2rCeO3BAlNBnnE/Fu103DkZIFD5vBAoGBAPtEruX93ggJfK/dY/Bq3WRC7S5dGnRQZ7Z5lErK2ZX448HOhwdOH5e9FXPH5X+QpYkDFMe49BD6eDNCPrdF+0ttMrQfV2HtKiTbRae7rYrsRBkY+MKENixz4ENVNQdueyv0CvBe7Ba7bXHdrPdiSUwEBmkn9wG+btDy+ItHYX65AoGBANS3r63tVIraNT5mhfBHChmmy35A2YaJc2IJGWTOZjNb+CHu/99DwiHWvYhWp4RZ0BKK/7GkBetDhVg21sscL2981oTOIiul8wc5P252QJvjsyumuB5+NcdmzYF7PbvotuKI4o8hu7dHYY4Qp/MGz2eQhYGBSB9GqbRMJShtjkFZAoGBAIaxI7xAIRRX2ZIAcIFBF9qWEcRnvjWZoG7tr3OEV60QFS8gAbwFweO6RVSiVEDUjhfrIemKGLM9QM/hc/MUvYeKSsLJhjMFSjElpaorbfTpf/ugKkFDVDLyDsapV1rbe4VtNavyhkYNRLbkKMMX2ci446Lc/Ijfx1GU3Wzz36xpAoGAQ4mutcJMvWlazl0u2YM0qcBTi9p7NkQd5lqNPXxq5pOkzOFdTD3vPV84/jjFJzh83+ZSGMzDNFdT1xZSTFq+lN9GHRR1tPYTm4+JnEDfcp9xG8LrYoMgABeb2CiRCUByEKr1hAxp1V9MkhanvHnFEFTKjrvFcmi1KRGkGpnuOMECgYEA88kCnSMb1yHfexJQZ+WUgb8m+WeyOgW2a2DzU1yXLFoCEZlbNQYFFWbDeTHfmaur3rox0ZvcoDv1ohXCsULZz9uu72cgRaObgGsjFAo9J0btEJT7s1ljUr55NwLsaPUkWzTIce2BnIE388y74i9DcPRrFkbOlxXPzvP0E1r6SK4="
		);

	}

	private void assertLaunches(String directory, boolean mustValidate) throws URISyntaxException {
		final URL launchesDirectory = getClass().getResource(directory);
		TestLaunchLoader testLaunchLoader = new TestLaunchLoader();
		final List<TestLaunch> testLaunches = testLaunchLoader.loadTestLaunches(launchesDirectory.toURI());
		if (testLaunches != null && testLaunches.size() > 0) {
			int count  = 0;
			for (TestLaunch launch : testLaunches) {
				final String s = tokenBuilder.build(launch);
				boolean result = sut.validate(s, null);
				if (mustValidate) {
					Assert.assertTrue(launch.getName() + " MUST validate, instead gotten reason " + sut.getReason(), result);
					Assert.assertNull("Reason for " + launch.getName(), sut.getReason());
				} else {
					Assert.assertFalse(launch.getName() + " MUST not validate", result);
					Assert.assertNotNull("Reason for " + launch.getName(), sut.getReason());
				}
				count++;
			}
			Assert.assertEquals(count, testLaunches.size());
		}
	}

	@Test
	public void validateValidTokensMustReturnTrue() throws URISyntaxException {
		assertLaunches(VALID_LAUNCHES_DIR, true);
	}

	@Test
	public void validateInvalidTokensMustReturnFalse() throws URISyntaxException {
		assertLaunches(INVALID_LAUNCHES_DIR, false);
	}

	/*
	@Test
	public void validateDeepLinkRequest() {
		String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6InBVYUFkb2VmQ2Q1VGctVEM4MDdtalJlSGpTM2VjOG5zWTktbnJwV0RRUzAifQ.eyJodHRwczovL3B1cmwuaW1zZ2xvYmFsLm9yZy9zcGVjL2x0aS9jbGFpbS9tZXNzYWdlX3R5cGUiOiJMdGlEZWVwTGlua2luZ1JlcXVlc3QiLCJnaXZlbl9uYW1lIjoiTGVzdGVyIiwiZmFtaWx5X25hbWUiOiJIeWF0dCIsIm1pZGRsZV9uYW1lIjoiRW5vY2giLCJwaWN0dXJlIjoiaHR0cDovL2V4YW1wbGUub3JnL0xlc3Rlci5qcGciLCJlbWFpbCI6Ikxlc3Rlci5IeWF0dEBleGFtcGxlLm9yZyIsIm5hbWUiOiJMZXN0ZXIgRW5vY2ggSHlhdHQiLCJodHRwczovL3B1cmwuaW1zZ2xvYmFsLm9yZy9zcGVjL2x0aS9jbGFpbS9yb2xlcyI6WyJodHRwOi8vcHVybC5pbXNnbG9iYWwub3JnL3ZvY2FiL2xpcy92Mi9pbnN0aXR1dGlvbi9wZXJzb24jSW5zdHJ1Y3RvciJdLCJodHRwczovL3B1cmwuaW1zZ2xvYmFsLm9yZy9zcGVjL2x0aS9jbGFpbS9yb2xlX3Njb3BlX21lbnRvciI6WyJhNjJjNTJjMDJiYTI2MjAwM2Y1ZSJdLCJodHRwczovL3B1cmwuaW1zZ2xvYmFsLm9yZy9zcGVjL2x0aS9jbGFpbS9jb250ZXh0Ijp7ImlkIjoiODgiLCJsYWJlbCI6IlRlc3QgQ291cnNlIiwidGl0bGUiOiJUZXN0IENvdXJzZSIsInR5cGUiOlsiQ291cnNlT2ZmZXJpbmciXX0sImh0dHBzOi8vcHVybC5pbXNnbG9iYWwub3JnL3NwZWMvbHRpL2NsYWltL3Rvb2xfcGxhdGZvcm0iOnsibmFtZSI6IlVPQyBUZXN0IHBsYXRmb3JtIiwiY29udGFjdF9lbWFpbCI6IiIsImRlc2NyaXB0aW9uIjoiIiwidXJsIjoiIiwicHJvZHVjdF9mYW1pbHlfY29kZSI6IiIsInZlcnNpb24iOiIxLjAifSwiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGktZGwvY2xhaW0vZGVlcF9saW5raW5nX3NldHRpbmdzIjp7ImFjY2VwdF90eXBlcyI6WyJsaW5rIiwiZmlsZSIsImh0bWwiLCJsdGlSZXNvdXJjZUxpbmsiLCJpbWFnZSJdLCJhY2NlcHRfbWVkaWFfdHlwZXMiOiJpbWFnZS8qLHRleHQvaHRtbCIsImFjY2VwdF9wcmVzZW50YXRpb25fZG9jdW1lbnRfdGFyZ2V0cyI6WyJpZnJhbWUiLCJ3aW5kb3ciLCJlbWJlZCJdLCJhY2NlcHRfbXVsdGlwbGUiOnRydWUsImF1dG9fY3JlYXRlIjp0cnVlLCJ0aXRsZSI6IlRoaXMgaXMgdGhlIGRlZmF1bHQgdGl0bGUiLCJ0ZXh0IjoiVGhpcyBpcyB0aGUgZGVmYXVsdCB0ZXh0IiwiZGF0YSI6IlNvbWUgcmFuZG9tIG9wYXF1ZSBkYXRhIHRoYXQgTVVTVCBiZSBzZW50IGJhY2siLCJkZWVwX2xpbmtfcmV0dXJuX3VybCI6Imh0dHBzOi8vbHRpLXJpLmltc2dsb2JhbC5vcmcvcGxhdGZvcm1zLzY4L2NvbnRleHRzLzg4L2RlZXBfbGlua3MifSwiaXNzIjoiaHR0cHM6Ly93d3cudW9jLmVkdSIsImF1ZCI6IlVuaXZlcnNpdGF0IE9iZXJ0YSBkZSBDYXRhbHVueWEiLCJpYXQiOjE1NTQxMzc0MjgsImV4cCI6MTU1NDEzNzcyOCwic3ViIjoiYzYzMTgwODdkNjg1NTEwNTU3ZjkiLCJodHRwczovL3B1cmwuaW1zZ2xvYmFsLm9yZy9zcGVjL2x0aS9jbGFpbS9sdGkxMV9sZWdhY3lfdXNlcl9pZCI6ImM2MzE4MDg3ZDY4NTUxMDU1N2Y5Iiwibm9uY2UiOiI1NDU1MTFkMzc5NjdmOGY0MzlhMiIsImh0dHBzOi8vcHVybC5pbXNnbG9iYWwub3JnL3NwZWMvbHRpL2NsYWltL3ZlcnNpb24iOiIxLjMuMCIsImxvY2FsZSI6ImVuLVVTIiwiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGkvY2xhaW0vbGF1bmNoX3ByZXNlbnRhdGlvbiI6eyJkb2N1bWVudF90YXJnZXQiOiJpZnJhbWUiLCJoZWlnaHQiOjMyMCwid2lkdGgiOjI0MH0sImh0dHBzOi8vd3d3LmV4YW1wbGUuY29tL2V4dGVuc2lvbiI6eyJjb2xvciI6InZpb2xldCJ9LCJodHRwczovL3B1cmwuaW1zZ2xvYmFsLm9yZy9zcGVjL2x0aS9jbGFpbS9jdXN0b20iOnsibXlDdXN0b21WYWx1ZSI6IjEyMyJ9LCJodHRwczovL3B1cmwuaW1zZ2xvYmFsLm9yZy9zcGVjL2x0aS9jbGFpbS9kZXBsb3ltZW50X2lkIjoiIiwiaHR0cHM6Ly9wdXJsLmltc2dsb2JhbC5vcmcvc3BlYy9sdGkvY2xhaW0vdGFyZ2V0X2xpbmtfdXJpIjoiaHR0cHM6Ly9sdGktcmkuaW1zZ2xvYmFsLm9yZy9sdGkvdG9vbHMvNzAvZGVlcF9saW5rX2xhdW5jaGVzIn0.iouIK8dqEuqKjH_Clqa7GriNuyEkQwLgov6ysw-Sr_NlhmW6BO0FIoqpZ_9q1d155X0nvbCfF1kI_hy3sNTKv8w0VBeP_fxv9GCruVLeAFrOJyvu_vSpkqI1UZblugAT5wL-8pa041N8UeqAzz4ClpFrR9MtzAnprKjOG_yG6tsyatWDsawpRaKHQGtQud3bWcPV_xFSfUZoEewxNjTtz1kwx-UbxBSzZDCxbWsEHqyIatr_mwywqcDcxqgRa05tAp5L4gZzxcrz7RPN83ATDuZYAfjfJTy9gJxFbTKgQ0plrxUbyCd_vybpXOn3wLlNHTR49IRV30AcZYooVl-3sA";
		boolean result = sut.validate(token, null);
		Assert.assertTrue(result);
		Assert.assertTrue(sut.isDeepLinkingRequest());
		Assert.assertFalse(sut.isResourceLinkLaunch());

		// assert deep linking settings exists
		Assert.assertNotNull(sut.getDeepLinkingSettings());
	}*/
}

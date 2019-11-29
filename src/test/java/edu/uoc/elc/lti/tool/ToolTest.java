package edu.uoc.elc.lti.tool;

import edu.uoc.elc.lti.tool.oidc.InMemoryOIDCLaunchSession;
import edu.uoc.lti.accesstoken.JSONAccessTokenRequestBuilderImpl;
import edu.uoc.lti.jwt.claims.JWSClaimAccessor;
import edu.uoc.lti.jwt.claims.TestLaunch;
import edu.uoc.lti.jwt.claims.TestLaunchLoader;
import edu.uoc.lti.jwt.claims.TokenBuilder;
import edu.uoc.lti.jwt.client.JWSClientCredentialsTokenBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class ToolTest {
	private final static String LTI_INVALID_LAUNCHES_DIR = "/lti/invalid/";
	private final static String LTI_VALID_LAUNCHES_DIR = "/lti/valid/";
	private final static String DEEP_LINKING_VALID_LAUNCHES_DIR = "/deeplinking/valid/";

	private Tool sut;
	private TokenBuilder tokenBuilder;

	@Before
	public void setUp() {
		String keysetUrl = "https://lti-ri.imsglobal.org/platforms/68/platform_keys/60.json";
		String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0MkrXiaPUxRzGOwrmSQKlDXUFn9veJlUybecFN07QIlqU758DxsSAvv8ZGPnzQVBKy9ykoXoaxecpKEIe/kK5qPbAVvnK6lGFbUl1QkK/NnHwf2zDy4S1f/OLh0oyKcI7izkUUl4lLzim5jsNChxpY00xqi5lh8Sk2qRppbbUR8rojTnl64mZq3P6Rl3GlXKj4GpRCFTdWb4Gyrx6KU6IZ2rufnGSSfRK4jnuASvTBW4PBbipxXN3mjPukx0tsWIYHh3hhv0DZUnOPBShPf0aTeT4c8+rjZ7EhDZJJr/OlLW9d+wonFKIz+fCdjzBxdGUEdoMsU7pW5xsmp8obAHUQIDAQAB";
		String privateKey = "MIIEpQIBAAKCAQEA0MkrXiaPUxRzGOwrmSQKlDXUFn9veJlUybecFN07QIlqU758DxsSAvv8ZGPnzQVBKy9ykoXoaxecpKEIe/kK5qPbAVvnK6lGFbUl1QkK/NnHwf2zDy4S1f/OLh0oyKcI7izkUUl4lLzim5jsNChxpY00xqi5lh8Sk2qRppbbUR8rojTnl64mZq3P6Rl3GlXKj4GpRCFTdWb4Gyrx6KU6IZ2rufnGSSfRK4jnuASvTBW4PBbipxXN3mjPukx0tsWIYHh3hhv0DZUnOPBShPf0aTeT4c8+rjZ7EhDZJJr/OlLW9d+wonFKIz+fCdjzBxdGUEdoMsU7pW5xsmp8obAHUQIDAQABAoIBAQC9MX4t++0mkMJXlDNRu1omwbxlgqcFdpRhkhNKyMqXia4jItqSaaphr+wfIHT90MQkGQPOiK9609OrTw08IgnhxBuB2MDbTLHom9UjfeVKCSK9xGKM3+hLqVkxalT5tnseMOnYSyaMSbli3Ck2fmu1ZAat+ljqE1Am64v+lHc6wsq4tUXvZ6/dIthvcnbuPP0RwdZH05GWqiI8sUz0W2zi7rqFJadaEZbxb/WFhO51MbyrZh34/MpxfqJEIkFnrzt+FgJ4F7mbQrv+XXo1mQ2I0MCknzWspYLwCsVyGV9jSuK+zmD9R/JGByf2rCeO3BAlNBnnE/Fu103DkZIFD5vBAoGBAPtEruX93ggJfK/dY/Bq3WRC7S5dGnRQZ7Z5lErK2ZX448HOhwdOH5e9FXPH5X+QpYkDFMe49BD6eDNCPrdF+0ttMrQfV2HtKiTbRae7rYrsRBkY+MKENixz4ENVNQdueyv0CvBe7Ba7bXHdrPdiSUwEBmkn9wG+btDy+ItHYX65AoGBANS3r63tVIraNT5mhfBHChmmy35A2YaJc2IJGWTOZjNb+CHu/99DwiHWvYhWp4RZ0BKK/7GkBetDhVg21sscL2981oTOIiul8wc5P252QJvjsyumuB5+NcdmzYF7PbvotuKI4o8hu7dHYY4Qp/MGz2eQhYGBSB9GqbRMJShtjkFZAoGBAIaxI7xAIRRX2ZIAcIFBF9qWEcRnvjWZoG7tr3OEV60QFS8gAbwFweO6RVSiVEDUjhfrIemKGLM9QM/hc/MUvYeKSsLJhjMFSjElpaorbfTpf/ugKkFDVDLyDsapV1rbe4VtNavyhkYNRLbkKMMX2ci446Lc/Ijfx1GU3Wzz36xpAoGAQ4mutcJMvWlazl0u2YM0qcBTi9p7NkQd5lqNPXxq5pOkzOFdTD3vPV84/jjFJzh83+ZSGMzDNFdT1xZSTFq+lN9GHRR1tPYTm4+JnEDfcp9xG8LrYoMgABeb2CiRCUByEKr1hAxp1V9MkhanvHnFEFTKjrvFcmi1KRGkGpnuOMECgYEA88kCnSMb1yHfexJQZ+WUgb8m+WeyOgW2a2DzU1yXLFoCEZlbNQYFFWbDeTHfmaur3rox0ZvcoDv1ohXCsULZz9uu72cgRaObgGsjFAo9J0btEJT7s1ljUr55NwLsaPUkWzTIce2BnIE388y74i9DcPRrFkbOlxXPzvP0E1r6SK4=";

		ToolDefinition toolDefinition = ToolDefinition.builder()
						.clientId("Universitat Oberta de Catalunya")
						.name("Universitat Oberta de Catalunya")
						.platform("https://www.uoc.edu")
						.deploymentId("testDeployment")
						.keySetUrl(keysetUrl)
						.accessTokenUrl("https://lti-ri.imsglobal.org/platforms/68/access_tokens")
						.oidcAuthUrl("https://lti-ri.imsglobal.org/platforms/68/authorizations/new")
						.privateKey("MIIEpQIBAAKCAQEAwDN0DFZnsxETm+lzQh7dsbBX6srNGqQ+Ougc485pqys81TzjoWDsubj70NdTge8DO3ycJbPpDsTH5P1Ea+vCHq/h1SzFLLF0yLw5Y2sWrLlDF9PrvR9yApu0bZ3zNTl55B4EblYns3j3JydwfnnLuNeNVpwv7wosLZKhMvg9CQv39prJj8xI+l5FoRkhKS86tl5PrU4Xld+jBo84GWAx0SYxbGF/vR9ve9lnErNWLv8ozYC2J9FusberZrsZ8M0mpNR1vJMayqDIwMPX5rsKtWZJh33XwAKRsEIxWT0WUGM54SUc0jrszapXfOoznblwmcAd/MVLXOlgOoaeiQbxFwIDAQABAoIBAQCV6UwRt29I1v/xcuPoZcTLLF1Wj/nsVsEHFzY5ZS08Sgw30jdGDyB1N9iJqvnCddeSlX4BVlNopom3jOfKOWf+oXbG3BWq53rMeytT5RoZfKLdOfk8WQOHD0Vq3nDtSbhN+i/Ak8iBjs9Ppc5zh7IjqVTfghFNJCUTIrRC2iZZH7b+deK4vWd6GnIubftHs3H/worivZFPXm+qG0+mSLo9DdxuySQmG0KwwJOz/RwW7oKME0Y05+eUA0E2EUODEzf/iTBsnZxDJ1PVuJvWiSl4uDh167G4mnscPiLYcDc4xJ4rmr+mDoekWMn70iNzW4xLZQaLMLy/RtKoclFVDQZxAoGBAOxQJOm96wBVT+zfaO/gork6DwOFxdf765C8sNsRSwygzrh03UYbO0bFwi2r2/bBXvyt69DGxKapUhDH0z5inVjsZwHAdrHAGpf+jA9smumsJI25/AXmp9piHSx1SI5AjygViCETSD6ItWhTbEBJn0rwhAs+f8hFGymwF11T4HH/AoGBANA2h5O7vlYGHmgEVyol/oRiXrggx2z0iVkn3RUhzWqXqedaQjp8zyA+FG/7fPsYah9R1sgKZ2aey+npo7RKQgmsgJWTQsXq+Fcvivw1z0xIJ8AmK4lhTHlw/B9TT3cxzy1IqAUfP4luJXnsiZ45G78XD6Q5Ftd8Fxn+PhDq4NDpAoGBAIClyNKvH6ZYy2Aq59ffNPcdklrakrBYZw+uiaFZMsA3MxLcHDI0VPrcYi+25dLZxrpMfJp1+0y31QNppajKytpEKHedrYBrEo84dktXVqZrnqLBY2BbB3ot+6/eUZePsd+iiS9obeYNSqT29XGyItQLR/dPGQWQCY+SW8XlCcVFAoGBAMOP5AmDVkPQHXEPWptQ8lx/VH3W89jHWdXulj2J8TlD2CZfZUMwBQ9An8uKR5pEFTDzmitrcjE1x0sd0k+9S4dwiZlzpkzk5HpnQkCffeQlBYj5kPzI8Z5C29vEUSggFXpv+rhM4E2BshtxatS8yO3TiDJ0GJsuhzg3zy3unlg5AoGAZv9YjAoQdxrRl1AiWntZm8BXxXwXEVEG2Wbr9a6+OPiaQXgpk11nf2QnEPmFZ2vFSC6YWEba34qs3kNBg5hp0ttHNyzwNxL7B+4sRoN08lVtOYUOGeqg3yGkdQCxW4RMiAL37+01rrbSGUxieWjP3V4y5gP2//mnlan/s4lbkOc=")
						.publicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwDN0DFZnsxETm+lzQh7dsbBX6srNGqQ+Ougc485pqys81TzjoWDsubj70NdTge8DO3ycJbPpDsTH5P1Ea+vCHq/h1SzFLLF0yLw5Y2sWrLlDF9PrvR9yApu0bZ3zNTl55B4EblYns3j3JydwfnnLuNeNVpwv7wosLZKhMvg9CQv39prJj8xI+l5FoRkhKS86tl5PrU4Xld+jBo84GWAx0SYxbGF/vR9ve9lnErNWLv8ozYC2J9FusberZrsZ8M0mpNR1vJMayqDIwMPX5rsKtWZJh33XwAKRsEIxWT0WUGM54SUc0jrszapXfOoznblwmcAd/MVLXOlgOoaeiQbxFwIDAQAB")
						.build();
		ToolBuilders toolBuilders = new ToolBuilders(new JWSClientCredentialsTokenBuilder(publicKey, privateKey),
						new JSONAccessTokenRequestBuilderImpl(),
						null);
		this.sut = new Tool(
						toolDefinition,
						new JWSClaimAccessor(keysetUrl),
						new InMemoryOIDCLaunchSession(),
						toolBuilders);
		this.tokenBuilder = new TokenBuilder(
						"pUaAdoefCd5Tg-TC807mjReHjS3ec8nsY9-nrpWDQS0",
						"https://www.uoc.edu",
						"Universitat Oberta de Catalunya",
						"testDeployment",
						publicKey,
						privateKey
		);

	}

	private void assertLaunches(String directory, BiConsumer<Boolean, String> callback) throws URISyntaxException {
		final List<TestLaunch> testLaunches = loadTestLaunches(directory);
		int count  = 0;
		for (TestLaunch launch : testLaunches) {
			assertLaunch(launch, callback);
			count++;
		}
		Assert.assertTrue("We must test something", count > 0);
		Assert.assertEquals(count, testLaunches.size());
	}

	private List<TestLaunch> loadTestLaunches(String directory) throws URISyntaxException {
		final URL launchesDirectory = getClass().getResource(directory);
		TestLaunchLoader testLaunchLoader = new TestLaunchLoader();
		return testLaunchLoader.loadTestLaunches(launchesDirectory.toURI());
	}

	private void assertLaunch(TestLaunch launch, BiConsumer<Boolean, String> callback) {
		boolean result = validateLaunch(launch);
		callback.accept(result, launch.getName());
	}

	private boolean validateLaunch(TestLaunch launch) {
		final String s = tokenBuilder.build(launch);
		return sut.validate(s, null);
	}

	@Test
	public void validateValidLtiLaunchesMustReturnTrue() throws URISyntaxException {
		assertLaunches(LTI_VALID_LAUNCHES_DIR, (result, name) -> {
			Assert.assertTrue(name + " MUST validate, instead gotten reason " + sut.getReason(), result);
			Assert.assertNull("Reason for " + name, sut.getReason());
			Assert.assertTrue("Launch MUST be a LTI Resource Link Launch", sut.isResourceLinkLaunch());
		});
	}

	@Test
	public void validateInvalidLtiLaunchesMustReturnFalse() throws URISyntaxException {
		assertLaunches(LTI_INVALID_LAUNCHES_DIR, (result, name) -> {
			Assert.assertFalse(name + " MUST not validate", result);
			Assert.assertNotNull("Reason for " + name, sut.getReason());
		});
	}

	@Test
	public void validateValidDeepLinkingLaunchesMustReturnTrue() throws URISyntaxException {
		assertLaunches(DEEP_LINKING_VALID_LAUNCHES_DIR, (result, name) -> {
			Assert.assertTrue(name + " MUST validate, instead gotten reason " + sut.getReason(), result);
			Assert.assertNull("Reason for " + name, sut.getReason());
			Assert.assertTrue("Launch MUST be a DeepLinking launch", sut.isDeepLinkingRequest());
		});
	}
}

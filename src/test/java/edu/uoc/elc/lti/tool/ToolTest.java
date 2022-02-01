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
 * @author xaracil@uoc.edu
 */
public class ToolTest {
	private final static String LTI_INVALID_LAUNCHES_DIR = "/lti/invalid/";
	private final static String LTI_VALID_LAUNCHES_DIR = "/lti/valid/";
	private final static String DEEP_LINKING_VALID_LAUNCHES_DIR = "/deeplinking/valid/";

	private Tool sut;
	private TokenBuilder tokenBuilder;

	@Before
	public void setUp() {
		// platform related settings
		String keysetUrl = "https://lti-ri.imsglobal.org/platforms/2647/platform_keys/2449.json";
		String platformPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArZaeXeNsstQ0sxiTB9ny" +
						"Em00dPHso5uURVaDXLn8XBYu38t9DKAIuCKeE12Zuqte3v5JY20qgWxXbBkGjRqn" +
						"2nn+HvltVSxy6nYjwvc+D8kRHWlrKpdxnbWCM6lpWVnzFpMXghkcMTH3wLWd22yq" +
						"/AnzDfYgTR+6gHS9YHoBmBv59CrUFMPF2ZADeG88Wu1LsMcVXEHgtS5py09kt5Rq" +
						"6Iq4S3EatZKGExaw4OdSwRq8T3wdB3NlQ23CPkeX3/iGHuD7F7qZdK3yWf4p5tpV" +
						"8v/ftr04uQXDBJ+GXPWEHmObBv6+jNuUq4U1qthB+P2RyilARUa5S99EYxZ4CN5S" +
						"7QIDAQAB";
		String platformPrivateKey = "MIIEogIBAAKCAQEArZaeXeNsstQ0sxiTB9nyEm00dPHso5uURVaDXLn8XBYu38t9" +
						"DKAIuCKeE12Zuqte3v5JY20qgWxXbBkGjRqn2nn+HvltVSxy6nYjwvc+D8kRHWlr" +
						"KpdxnbWCM6lpWVnzFpMXghkcMTH3wLWd22yq/AnzDfYgTR+6gHS9YHoBmBv59CrU" +
						"FMPF2ZADeG88Wu1LsMcVXEHgtS5py09kt5Rq6Iq4S3EatZKGExaw4OdSwRq8T3wd" +
						"B3NlQ23CPkeX3/iGHuD7F7qZdK3yWf4p5tpV8v/ftr04uQXDBJ+GXPWEHmObBv6+" +
						"jNuUq4U1qthB+P2RyilARUa5S99EYxZ4CN5S7QIDAQABAoIBABfJD6Ily3slgKMZ" +
						"tuvYfUbxF8L+c0JnywVFOXK3OVEU6Q9ZPqVAOHGirK5SoQHnAXTkuwGYr75ULhZt" +
						"x2wGPwG8vYlY/BYzpPtjfLr2Tdaz4lh5XWnUnojRmimiCQFpRdnOBeaZsjc7dYs+" +
						"V4b9eOBYLKm5E+v9cPyxAlH55cYFtXYXZT6eAqYZv4HnnOWa/Qi52rwUYqFA31TS" +
						"G0IynuhAseToXAy7v/5wyhG+nAiRZq1sKykuEoj679aJVHxgHr+lS+swNjVq6TaC" +
						"2yh/BxSeKoSCh/gG34ATX9IhONuK2cHQ+ajAkxIEBSEONV8y2o4xbVBQ06HtFC/b" +
						"fZ29/4ECgYEA3+TftsWFZ+HKuQjcrJ3zmnlcfuuUBfuIlUQQoFKrvP+8ndrOkdRO" +
						"xFab2q/Y52YWjZ6JrSlKzSnrUZ9GDUBFS0ECBZFxo/i1/jER0R6D8lxgiAJrgTVd" +
						"wzrHDw3OJx8KwaJPgkoPTgWP0ZLbdh3gokX+Ic20u53TVfNI3XGiT1UCgYEAxnsH" +
						"SO0Vgf+gpBMBF4O8J3mWDIV1N7pYnUn4Lfp22D9Ah4WqmoYLofkfQU8G9OaMswA7" +
						"SUltP8CjJL+V7IcqiBKNUz6OQAOiPciQnj77SkiW7c6haTdJ0ql+KAO/2hFfT9pJ" +
						"a6YOMOiUJZLullXN3KvaDpEqkO3/cRb7ZwN2BTkCgYBVwYZmrTUx2uwY/2n2u/Eo" +
						"g7+H5Zemyvc/pPhxT/jzxUhrdfmVJaqdzUaY9q2vxAxzZfv51U0PVDUL6GOeg7WG" +
						"43lwxIqwcXzTxdu7K2MCm5tlvPeMX/Jv6r1/6JvDpEIsdIzbrJ48FilF7mgcz8jQ" +
						"ntp9/BpzmjnuO+b22qV+mQKBgDo92cplf50xBNOAnB5pUqoGvKgZ9WaP8PbewMvF" +
						"JmCYVvgtmf1T/k/eSXShmvn8OSdBlyQPPtapUXY0HetVUn6xavUVcyqHpnEYzI8T" +
						"DiwjFt7bdnPofGorwFA/oWl9FYnaFRdYl+t0JBzOe+JzdUAe0ZLauDHlFML7qnP1" +
						"CQnJAoGAaDvrpqP5SHga9cZ1Wg1G8GVUpBz4Nf4wl/ugnCDEA4a8KYxeRgt0RIpq" +
						"nEUZtk7RilHR/2xfPFrkD2oZjVcQYnxoNJcPnC/kI3Hgyi5jz2KWFQo55rzB+gSU" +
						"Inc5r31qImVZfbiNId6kOiJxffaGixhtfEf0RvnNbnD4KO/quAE=";

		ToolDefinition toolDefinition = ToolDefinition.builder()
						.clientId("Universitat Oberta de Catalunya")
						.name("Universitat Oberta de Catalunya")
						.platform("https://www.uoc.edu")
						.deploymentId("testdeploy")
						.keySetUrl(keysetUrl)
						.accessTokenUrl("https://lti-ri.imsglobal.org/platforms/2647/access_tokens")
						.oidcAuthUrl("https://lti-ri.imsglobal.org/platforms/2647/authorizations/new")
						// tool's private key
						.privateKey("MIIEpQIBAAKCAQEA9meqL/mLQa/PdI+dU4D2ovMs3jGSF2BmFBOFP4zay7Ni5ABi" +
										"xyaghWyM5sNznITm847l6C+yzUo0CvmmmFNVEE/XEyRYkNry04Jm8IICSMnHhHch" +
										"t3rtGEAALTIiTsjbnj31NlsA+aXaWWY4kRt5jTO+r72LHvReb/RxektWtFE8MmZI" +
										"P1W7+2l5BYqT9l0lmi7iVcmOVx1aUtaqUhVPc7rmYhnd2cLbA3T69MRSGXt0fK25" +
										"P8oFUi2SaJ74A6ZJw1kVx+fQA3Wkkf2x7+LflrST1E1+n9h/F1o6ZSF8H/GsClYL" +
										"YDJ9PhCj48NtJH6Hho873x2tO6Z5kXoDFfwNdQIDAQABAoIBAQDkAxecX1o6xZq+" +
										"bOsTy4HvVgGN9ucOVLkBGPMz1H7fAree7rB5Q0hFTyavn+vKyb0BYpljG0hk6aZx" +
										"BYXg7TQI7SjD4N1H5iPQD5p8MoI0ouvKq4b6x3jA+PBw4jSXHQ0FSKYxz1J+sbf1" +
										"BD+SH4+CV4C8FTS4xz7gcJ3VogOwxK6A6XCYc/NaAYVdXUymO+GPbEWBiL59m3Fo" +
										"2gCvnStwQUzEWF1vOoR0Vq40JCUKJdboaCD7N5+UfTvaSmItI7e6AYkk+4Sw6uxd" +
										"vuE6CkmoQn6A6CVB6YyC8BKd1uiTwp15V0ZcIriLYkOqw7t+VJswWLO25wss8w0C" +
										"ZNQU83B9AoGBAP9d0/VTBqgb6oXaUupck/57xVjAbbuUqvtOyOddqw5dRAYkYh8k" +
										"2LsKxgK2OxApxiOe7PpInaVU9N1gz8vdjiDYMTbHAldae2gzlnuPnLZoRmxkyN+6" +
										"s254/LaL2lxVQLVBTRwVcxBeuPEJvtszh8e3HNaGas9LO4mm2c4gCLVjAoGBAPcE" +
										"JU6YwkTaUjYK/liGQGnbJ7pU6UY5E/DdQznh+Q0/oJLf9DYrvWKCChV7Tv6dMOgR" +
										"Rx3JbuHYC6BnMDJgkW8MSZtkpqsbWcZoAWyzPcZB5N24x6zWd9rxYPC8uzQgie3D" +
										"u+kAItrXO/orkCEFhFMop+IxVyaUPrTp55dQPvVHAoGBAJ7Hi1wVVKVAPlEdz8JS" +
										"794ivTES7OfeZ3W8peRd9FcJHJupEa2FrpCWAUp8XjsNjlplnbl9XzAGo/3mDS4Y" +
										"QyAe7HK/yK/h3auLr/yB7hHowijxfYjGwHxnE9K5IHuaspJA2R4mJBZAn0OYKKiI" +
										"4NsH+xFUIKNlhs5vBSxJ9MA7AoGAIqNcL+dP5qPOv3FPNU6uHanzqjdZ9tTuLOp6" +
										"ENXVPLxdYfsxnZ8IkZW2oEiITT/xqbkyhcTHXbgT+Uw+//F2s2G/uKCmlnvhXOcc" +
										"vAdudCytUUImDeCNMhCDDX8JqUeez86QvR2dxZ5E9NHDBj5lzdt3n0pX9Dr5iBj8" +
										"tsUz7uUCgYEAm45cbgVtMk7pPVBdBXMqx82bosj8PsT0Cn4foL3acQVFHWUPaC5W" +
										"i9rbGp2iuqPVVGK1ZDTP13KyYY9Jw4ZRBNJrdyErSlpGhiRuDRgdX/PWvr9krze8" +
										"nH4XC37FRa7UDQNK81IdsY2tyZTluheSuZ8EUnFGtBdXL1bzydxBjZI=")
						// tool's public key
						.publicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA9meqL/mLQa/PdI+dU4D2" +
										"ovMs3jGSF2BmFBOFP4zay7Ni5ABixyaghWyM5sNznITm847l6C+yzUo0CvmmmFNV" +
										"EE/XEyRYkNry04Jm8IICSMnHhHcht3rtGEAALTIiTsjbnj31NlsA+aXaWWY4kRt5" +
										"jTO+r72LHvReb/RxektWtFE8MmZIP1W7+2l5BYqT9l0lmi7iVcmOVx1aUtaqUhVP" +
										"c7rmYhnd2cLbA3T69MRSGXt0fK25P8oFUi2SaJ74A6ZJw1kVx+fQA3Wkkf2x7+Lf" +
										"lrST1E1+n9h/F1o6ZSF8H/GsClYLYDJ9PhCj48NtJH6Hho873x2tO6Z5kXoDFfwN" +
										"dQIDAQAB")
						.build();
		ToolBuilders toolBuilders = new ToolBuilders(new JWSClientCredentialsTokenBuilder(platformPublicKey, platformPrivateKey),
						new JSONAccessTokenRequestBuilderImpl(),
						null);
		this.sut = new Tool(
						toolDefinition,
						new JWSClaimAccessor(keysetUrl),
						new InMemoryOIDCLaunchSession(),
						toolBuilders);
		this.tokenBuilder = new TokenBuilder(
						"qPYR0iOgCEMTrSLS-Yw2LQ4kFcpGuDyg8HcScnm8VSE",
						"https://www.uoc.edu",
						"Universitat Oberta de Catalunya",
						"testdeploy",
						platformPublicKey,
						platformPrivateKey
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

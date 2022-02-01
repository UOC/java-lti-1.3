package edu.uoc.elc.lti.platform.accesstoken;

import edu.uoc.elc.lti.exception.BadToolProviderConfigurationException;
import edu.uoc.elc.lti.tool.ToolDefinition;
import edu.uoc.lti.accesstoken.AccessTokenRequestBuilder;
import edu.uoc.lti.accesstoken.UrlEncodedFormAccessTokenRequestBuilderImpl;
import edu.uoc.lti.clientcredentials.ClientCredentialsTokenBuilder;
import edu.uoc.lti.jwt.client.JWSClientCredentialsTokenBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author xaracil@uoc.edu
 */
public class AccessTokenRequestHandlerTest {
	private AccessTokenRequestHandler sut;

	private ToolDefinition toolDefinition;

	@Before
	public void setUp() {
		this.toolDefinition = ToolDefinition.builder()
						.clientId("Universitat Oberta de Catalunya")
						.name("Universitat Oberta de Catalunya")
						.keySetUrl("https://lti-ri.imsglobal.org/platforms/2647/platform_keys/2449.json")
						.accessTokenUrl("https://lti-ri.imsglobal.org/platforms/2647/access_tokens")
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
						.publicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA9meqL/mLQa/PdI+dU4D2" +
										"ovMs3jGSF2BmFBOFP4zay7Ni5ABixyaghWyM5sNznITm847l6C+yzUo0CvmmmFNV" +
										"EE/XEyRYkNry04Jm8IICSMnHhHcht3rtGEAALTIiTsjbnj31NlsA+aXaWWY4kRt5" +
										"jTO+r72LHvReb/RxektWtFE8MmZIP1W7+2l5BYqT9l0lmi7iVcmOVx1aUtaqUhVP" +
										"c7rmYhnd2cLbA3T69MRSGXt0fK25P8oFUi2SaJ74A6ZJw1kVx+fQA3Wkkf2x7+Lf" +
										"lrST1E1+n9h/F1o6ZSF8H/GsClYLYDJ9PhCj48NtJH6Hho873x2tO6Z5kXoDFfwN" +
										"dQIDAQAB")
						.build();

		ClientCredentialsTokenBuilder clientCredentialsTokenBuilder = new JWSClientCredentialsTokenBuilder(toolDefinition.getPublicKey(), toolDefinition.getPrivateKey());
		AccessTokenRequestBuilder accessTokenRequestBuilder = new UrlEncodedFormAccessTokenRequestBuilderImpl();
		this.sut = new AccessTokenRequestHandler(null, toolDefinition, clientCredentialsTokenBuilder, accessTokenRequestBuilder);
	}

	@Test
	public void getAccessToken() throws IOException, BadToolProviderConfigurationException {
		final AccessTokenResponse accessTokenResponse = this.sut.getAccessToken();
		Assert.assertNotNull(accessTokenResponse);
		Assert.assertNotNull(accessTokenResponse.getAccessToken());
		Assert.assertNotNull(accessTokenResponse.getScope());
		Assert.assertEquals("Bearer", accessTokenResponse.getTokenType());
		Assert.assertTrue(accessTokenResponse.getExpiresIn() > 0);
	}
}

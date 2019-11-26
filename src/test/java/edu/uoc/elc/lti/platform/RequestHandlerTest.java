package edu.uoc.elc.lti.platform;

import edu.uoc.elc.lti.tool.ToolDefinition;
import edu.uoc.elc.lti.exception.BadToolProviderConfigurationException;
import edu.uoc.lti.accesstoken.AccessTokenRequestBuilder;
import edu.uoc.lti.accesstoken.JSONAccessTokenRequestBuilderImpl;
import edu.uoc.lti.clientcredentials.ClientCredentialsTokenBuilder;
import edu.uoc.lti.jwt.client.JWSClientCredentialsTokenBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class RequestHandlerTest {
	private RequestHandler sut;

	private ToolDefinition toolDefinition;

	@Before
	public void setUp() {
		this.toolDefinition = ToolDefinition.builder()
						.clientId("Universitat Oberta de Catalunya")
						.name("Universitat Oberta de Catalunya")
						.keySetUrl("https://lti-ri.imsglobal.org/platforms/68/platform_keys/60.json")
						.accessTokenUrl("https://lti-ri.imsglobal.org/platforms/68/access_tokens")
						.privateKey("MIIEpQIBAAKCAQEAwDN0DFZnsxETm+lzQh7dsbBX6srNGqQ+Ougc485pqys81TzjoWDsubj70NdTge8DO3ycJbPpDsTH5P1Ea+vCHq/h1SzFLLF0yLw5Y2sWrLlDF9PrvR9yApu0bZ3zNTl55B4EblYns3j3JydwfnnLuNeNVpwv7wosLZKhMvg9CQv39prJj8xI+l5FoRkhKS86tl5PrU4Xld+jBo84GWAx0SYxbGF/vR9ve9lnErNWLv8ozYC2J9FusberZrsZ8M0mpNR1vJMayqDIwMPX5rsKtWZJh33XwAKRsEIxWT0WUGM54SUc0jrszapXfOoznblwmcAd/MVLXOlgOoaeiQbxFwIDAQABAoIBAQCV6UwRt29I1v/xcuPoZcTLLF1Wj/nsVsEHFzY5ZS08Sgw30jdGDyB1N9iJqvnCddeSlX4BVlNopom3jOfKOWf+oXbG3BWq53rMeytT5RoZfKLdOfk8WQOHD0Vq3nDtSbhN+i/Ak8iBjs9Ppc5zh7IjqVTfghFNJCUTIrRC2iZZH7b+deK4vWd6GnIubftHs3H/worivZFPXm+qG0+mSLo9DdxuySQmG0KwwJOz/RwW7oKME0Y05+eUA0E2EUODEzf/iTBsnZxDJ1PVuJvWiSl4uDh167G4mnscPiLYcDc4xJ4rmr+mDoekWMn70iNzW4xLZQaLMLy/RtKoclFVDQZxAoGBAOxQJOm96wBVT+zfaO/gork6DwOFxdf765C8sNsRSwygzrh03UYbO0bFwi2r2/bBXvyt69DGxKapUhDH0z5inVjsZwHAdrHAGpf+jA9smumsJI25/AXmp9piHSx1SI5AjygViCETSD6ItWhTbEBJn0rwhAs+f8hFGymwF11T4HH/AoGBANA2h5O7vlYGHmgEVyol/oRiXrggx2z0iVkn3RUhzWqXqedaQjp8zyA+FG/7fPsYah9R1sgKZ2aey+npo7RKQgmsgJWTQsXq+Fcvivw1z0xIJ8AmK4lhTHlw/B9TT3cxzy1IqAUfP4luJXnsiZ45G78XD6Q5Ftd8Fxn+PhDq4NDpAoGBAIClyNKvH6ZYy2Aq59ffNPcdklrakrBYZw+uiaFZMsA3MxLcHDI0VPrcYi+25dLZxrpMfJp1+0y31QNppajKytpEKHedrYBrEo84dktXVqZrnqLBY2BbB3ot+6/eUZePsd+iiS9obeYNSqT29XGyItQLR/dPGQWQCY+SW8XlCcVFAoGBAMOP5AmDVkPQHXEPWptQ8lx/VH3W89jHWdXulj2J8TlD2CZfZUMwBQ9An8uKR5pEFTDzmitrcjE1x0sd0k+9S4dwiZlzpkzk5HpnQkCffeQlBYj5kPzI8Z5C29vEUSggFXpv+rhM4E2BshtxatS8yO3TiDJ0GJsuhzg3zy3unlg5AoGAZv9YjAoQdxrRl1AiWntZm8BXxXwXEVEG2Wbr9a6+OPiaQXgpk11nf2QnEPmFZ2vFSC6YWEba34qs3kNBg5hp0ttHNyzwNxL7B+4sRoN08lVtOYUOGeqg3yGkdQCxW4RMiAL37+01rrbSGUxieWjP3V4y5gP2//mnlan/s4lbkOc=")
						.publicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwDN0DFZnsxETm+lzQh7dsbBX6srNGqQ+Ougc485pqys81TzjoWDsubj70NdTge8DO3ycJbPpDsTH5P1Ea+vCHq/h1SzFLLF0yLw5Y2sWrLlDF9PrvR9yApu0bZ3zNTl55B4EblYns3j3JydwfnnLuNeNVpwv7wosLZKhMvg9CQv39prJj8xI+l5FoRkhKS86tl5PrU4Xld+jBo84GWAx0SYxbGF/vR9ve9lnErNWLv8ozYC2J9FusberZrsZ8M0mpNR1vJMayqDIwMPX5rsKtWZJh33XwAKRsEIxWT0WUGM54SUc0jrszapXfOoznblwmcAd/MVLXOlgOoaeiQbxFwIDAQAB")
						.build();

		ClientCredentialsTokenBuilder clientCredentialsTokenBuilder = new JWSClientCredentialsTokenBuilder(toolDefinition.getPublicKey(), toolDefinition.getPrivateKey());
		AccessTokenRequestBuilder accessTokenRequestBuilder = new JSONAccessTokenRequestBuilderImpl();
		this.sut = new RequestHandler(null, toolDefinition, clientCredentialsTokenBuilder, accessTokenRequestBuilder);
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

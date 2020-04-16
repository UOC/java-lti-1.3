package edu.uoc.elc.lti.platform.deeplinking;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import edu.uoc.elc.lti.exception.InvalidLTICallException;
import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.lti.deeplink.DeepLinkingTokenBuilder;
import edu.uoc.lti.deeplink.content.FileItem;
import edu.uoc.lti.deeplink.content.Item;
import edu.uoc.lti.deeplink.content.LinkItem;
import edu.uoc.lti.deeplink.content.LtiResourceItem;
import edu.uoc.lti.jwt.deeplink.JWSTokenBuilder;

/**
 * @author xaracil@uoc.edu
 */
public class DeepLinkingClientTest {

	private DeepLinkingClient sut;
	private Settings settings;

	@Before
	public void setUp() {
		this.settings = Mockito.mock(Settings.class);
		this.sut = new DeepLinkingClient(null, null, null, null, null, null, settings);
		Mockito.when(settings.getAccept_types()).thenReturn(Arrays.asList("link"));
		Mockito.when(settings.getDeep_link_return_url()).thenReturn("https://lti-ri.imsglobal.org/platforms/68/contexts/88/deep_links");
	}

	private Item item() {
		return LinkItem.builder().url("http://www.uoc.edu").title("Main Portal").build();
	}

	@Test
	public void canAddItemWhenEmpty() {
		// empty list
		Assert.assertTrue(this.sut.canAddItem());
	}

	@Test
	public void canAddItemWhenAccepted() {
		Mockito.when(settings.isAccept_multiple()).thenReturn(true);

		// one element
		this.sut.addItem(item());
		Assert.assertTrue(this.sut.canAddItem());

		// more than one element
		this.sut.addItem(item());
		Assert.assertTrue(this.sut.canAddItem());
	}

	@Test
	public void cantAddItemWhenNotAccepted() {
		Mockito.when(settings.isAccept_multiple()).thenReturn(false);

		// empty list
		Assert.assertTrue(this.sut.canAddItem());

		// one element
		this.sut.addItem(item());
		Assert.assertFalse(this.sut.canAddItem());
	}

	@Test
	public void addItem() {
		Mockito.when(settings.getAccept_types()).thenReturn(Arrays.asList("link"));
		Mockito.when(settings.isAccept_multiple()).thenReturn(true);

		Assert.assertEquals(this.sut.getItemList().size(), 0);
		this.sut.addItem(item());
		Assert.assertEquals(this.sut.getItemList().size(), 1);
		this.sut.addItem(item());
		Assert.assertEquals(this.sut.getItemList().size(), 2);
		this.sut.addItem(item());
		Assert.assertEquals(this.sut.getItemList().size(), 3);
	}

	@Test(expected = InvalidLTICallException.class)
	public void addItemShouldThrowWhenNotMultiple() {
		Mockito.when(settings.getAccept_types()).thenReturn(Arrays.asList("link"));
		Mockito.when(settings.isAccept_multiple()).thenReturn(false);

		Assert.assertEquals(this.sut.getItemList().size(), 0);
		this.sut.addItem(item());
		Assert.assertEquals(this.sut.getItemList().size(), 1);

		this.sut.addItem(item());
		Assert.fail();
	}

	@Test(expected = InvalidLTICallException.class)
	public void addItemShouldThrowWhenTypeNotAllowed() {
		Mockito.when(settings.getAccept_types()).thenReturn(Arrays.asList("ltiResourceLink"));
		Mockito.when(settings.isAccept_multiple()).thenReturn(true);

		final Item ltitem = LtiResourceItem.builder().url("url").build();
		Assert.assertEquals(this.sut.getItemList().size(), 0);
		this.sut.addItem(ltitem);
		Assert.assertEquals(this.sut.getItemList().size(), 1);
		this.sut.addItem(ltitem);
		Assert.assertEquals(this.sut.getItemList().size(), 2);

		this.sut.addItem(item());
		Assert.fail();
	}

	@Test(expected = InvalidLTICallException.class)
	public void addItemShouldThrowWhenMediaTypeNotAllowed() {
		Mockito.when(settings.getAccept_types()).thenReturn(Arrays.asList("file"));
		Mockito.when(settings.getAccept_media_types()).thenReturn(Arrays.asList("application/pdf"));
		Mockito.when(settings.isAccept_multiple()).thenReturn(true);

		final Item fileItem = FileItem.builder().mediaType("application/pdf").build();
		Assert.assertEquals(this.sut.getItemList().size(), 0);
		this.sut.addItem(fileItem);
		Assert.assertEquals(this.sut.getItemList().size(), 1);
		this.sut.addItem(fileItem);
		Assert.assertEquals(this.sut.getItemList().size(), 2);

		final Item fileItem2 = FileItem.builder().mediaType("text/html").build();
		this.sut.addItem(fileItem2);
		Assert.fail();
	}

	@Test
	public void getReturnUrlShowReturnAValidURL() throws IOException {
		this.sut = filledDeepLinkingClient();

		final URL returnUrl = this.sut.getReturnUrl();
		Assert.assertNotNull(returnUrl);
		Assert.assertEquals(new URL(this.settings.getDeep_link_return_url()), returnUrl);
	}

	@Test
	public void buildJWTShouldReturnAValidJWT() {
		this.sut = filledDeepLinkingClient();

		this.sut.addItem(item());

		final String jwt = this.sut.buildJWT();
		Assert.assertNotNull(jwt);
	}

	private DeepLinkingClient filledDeepLinkingClient() {
		return new DeepLinkingClient(deepLinkingTokenBuilder(),
						"https://www.uoc.edu",
						"Universitat Oberta de Catalunya",
						null,
						"",
						"nonce",
						settings);
	}

	private DeepLinkingTokenBuilder deepLinkingTokenBuilder() {
		DeepLinkingTokenBuilder deepLinkingTokenBuilder = new JWSTokenBuilder(
						"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwDN0DFZnsxETm+lzQh7dsbBX6srNGqQ+Ougc485pqys81TzjoWDsubj70NdTge8DO3ycJbPpDsTH5P1Ea+vCHq/h1SzFLLF0yLw5Y2sWrLlDF9PrvR9yApu0bZ3zNTl55B4EblYns3j3JydwfnnLuNeNVpwv7wosLZKhMvg9CQv39prJj8xI+l5FoRkhKS86tl5PrU4Xld+jBo84GWAx0SYxbGF/vR9ve9lnErNWLv8ozYC2J9FusberZrsZ8M0mpNR1vJMayqDIwMPX5rsKtWZJh33XwAKRsEIxWT0WUGM54SUc0jrszapXfOoznblwmcAd/MVLXOlgOoaeiQbxFwIDAQAB",
						"MIIEpQIBAAKCAQEAwDN0DFZnsxETm+lzQh7dsbBX6srNGqQ+Ougc485pqys81TzjoWDsubj70NdTge8DO3ycJbPpDsTH5P1Ea+vCHq/h1SzFLLF0yLw5Y2sWrLlDF9PrvR9yApu0bZ3zNTl55B4EblYns3j3JydwfnnLuNeNVpwv7wosLZKhMvg9CQv39prJj8xI+l5FoRkhKS86tl5PrU4Xld+jBo84GWAx0SYxbGF/vR9ve9lnErNWLv8ozYC2J9FusberZrsZ8M0mpNR1vJMayqDIwMPX5rsKtWZJh33XwAKRsEIxWT0WUGM54SUc0jrszapXfOoznblwmcAd/MVLXOlgOoaeiQbxFwIDAQABAoIBAQCV6UwRt29I1v/xcuPoZcTLLF1Wj/nsVsEHFzY5ZS08Sgw30jdGDyB1N9iJqvnCddeSlX4BVlNopom3jOfKOWf+oXbG3BWq53rMeytT5RoZfKLdOfk8WQOHD0Vq3nDtSbhN+i/Ak8iBjs9Ppc5zh7IjqVTfghFNJCUTIrRC2iZZH7b+deK4vWd6GnIubftHs3H/worivZFPXm+qG0+mSLo9DdxuySQmG0KwwJOz/RwW7oKME0Y05+eUA0E2EUODEzf/iTBsnZxDJ1PVuJvWiSl4uDh167G4mnscPiLYcDc4xJ4rmr+mDoekWMn70iNzW4xLZQaLMLy/RtKoclFVDQZxAoGBAOxQJOm96wBVT+zfaO/gork6DwOFxdf765C8sNsRSwygzrh03UYbO0bFwi2r2/bBXvyt69DGxKapUhDH0z5inVjsZwHAdrHAGpf+jA9smumsJI25/AXmp9piHSx1SI5AjygViCETSD6ItWhTbEBJn0rwhAs+f8hFGymwF11T4HH/AoGBANA2h5O7vlYGHmgEVyol/oRiXrggx2z0iVkn3RUhzWqXqedaQjp8zyA+FG/7fPsYah9R1sgKZ2aey+npo7RKQgmsgJWTQsXq+Fcvivw1z0xIJ8AmK4lhTHlw/B9TT3cxzy1IqAUfP4luJXnsiZ45G78XD6Q5Ftd8Fxn+PhDq4NDpAoGBAIClyNKvH6ZYy2Aq59ffNPcdklrakrBYZw+uiaFZMsA3MxLcHDI0VPrcYi+25dLZxrpMfJp1+0y31QNppajKytpEKHedrYBrEo84dktXVqZrnqLBY2BbB3ot+6/eUZePsd+iiS9obeYNSqT29XGyItQLR/dPGQWQCY+SW8XlCcVFAoGBAMOP5AmDVkPQHXEPWptQ8lx/VH3W89jHWdXulj2J8TlD2CZfZUMwBQ9An8uKR5pEFTDzmitrcjE1x0sd0k+9S4dwiZlzpkzk5HpnQkCffeQlBYj5kPzI8Z5C29vEUSggFXpv+rhM4E2BshtxatS8yO3TiDJ0GJsuhzg3zy3unlg5AoGAZv9YjAoQdxrRl1AiWntZm8BXxXwXEVEG2Wbr9a6+OPiaQXgpk11nf2QnEPmFZ2vFSC6YWEba34qs3kNBg5hp0ttHNyzwNxL7B+4sRoN08lVtOYUOGeqg3yGkdQCxW4RMiAL37+01rrbSGUxieWjP3V4y5gP2//mnlan/s4lbkOc=");

		return deepLinkingTokenBuilder;
	}
}
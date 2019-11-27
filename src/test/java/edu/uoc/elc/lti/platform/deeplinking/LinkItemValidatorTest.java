package edu.uoc.elc.lti.platform.deeplinking;

import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.lti.deeplink.content.Embed;
import edu.uoc.lti.deeplink.content.IFrame;
import edu.uoc.lti.deeplink.content.LinkItem;
import edu.uoc.lti.deeplink.content.Window;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class LinkItemValidatorTest {
	private LinkItemValidator sut;
	private Settings settings;

	@Before
	public void setUp() {
		this.settings = Mockito.mock(Settings.class);
		this.sut = new LinkItemValidator(settings);
		Mockito.when(settings.getAccept_types()).thenReturn(Arrays.asList("link"));
	}

	private LinkItem item() {
		return LinkItem.builder()
						.url("http://www.uoc.edu")
						.title("Main Portal")
						.build();
	}

	private IFrame iFrame() {
		return IFrame.builder()
						.url("http://www.uoc.edu")
						.width(100)
						.height(100)
						.build();
	}

	private Window window() {
		return Window.builder()
						.build();
	}

	private Embed embed() {
		return Embed.builder()
						.build();
	}

	@Test
	public void isValidIframe() {
		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList("iframe"));
		final LinkItem item = item();
		item.setIframe(iFrame());
		Assert.assertTrue(this.sut.isValid(item));

		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList("iframe", "window"));
		Assert.assertTrue(this.sut.isValid(item));

		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList("window", "iframe"));
		Assert.assertTrue(this.sut.isValid(item));

		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList("window", "iframe", "embed"));
		Assert.assertTrue(this.sut.isValid(item));
	}

	@Test
	public void isValidEmbed() {
		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList("embed"));
		final LinkItem item = item();
		item.setEmbed(embed());
		Assert.assertTrue(this.sut.isValid(item));

		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList("embed", "window"));
		Assert.assertTrue(this.sut.isValid(item));

		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList("window", "embed"));
		Assert.assertTrue(this.sut.isValid(item));

		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList("window", "embed", "iframe"));
		Assert.assertTrue(this.sut.isValid(item));
	}

	@Test
	public void isValidWindow() {
		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList("window"));
		final LinkItem item = item();
		item.setWindow(window());
		Assert.assertTrue(this.sut.isValid(item));

		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList("window", "embed"));
		Assert.assertTrue(this.sut.isValid(item));

		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList("embed", "window"));
		Assert.assertTrue(this.sut.isValid(item));

		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList("iframe", "window", "embed"));
		Assert.assertTrue(this.sut.isValid(item));
	}
}

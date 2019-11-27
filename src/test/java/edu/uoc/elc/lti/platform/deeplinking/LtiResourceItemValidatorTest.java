package edu.uoc.elc.lti.platform.deeplinking;

import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.lti.deeplink.content.IFrame;
import edu.uoc.lti.deeplink.content.LtiResourceItem;
import edu.uoc.lti.deeplink.content.Window;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class LtiResourceItemValidatorTest {
	private LtiResourceItemValidator sut;
	private Settings settings;

	@Before
	public void setUp() {
		this.settings = Mockito.mock(Settings.class);
		this.sut = new LtiResourceItemValidator(settings);
		Mockito.when(settings.getAccept_types()).thenReturn(Arrays.asList("ltiResourceLink"));
	}

	private LtiResourceItem item() {
		return LtiResourceItem.builder()
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

	@Test
	public void isValidIframe() {
		final LtiResourceItem item = item();
		item.setIFrame(iFrame());
		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList());
		Assert.assertTrue(this.sut.isValid(item));

		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList("iframe"));
		Assert.assertTrue(this.sut.isValid(item));

		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList("iframe", "window"));
		Assert.assertTrue(this.sut.isValid(item));

		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList("window", "iframe"));
		Assert.assertTrue(this.sut.isValid(item));
	}

	@Test
	public void isValidWindow() {
		final LtiResourceItem item = item();
		item.setWindow(window());

		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList());
		Assert.assertTrue(this.sut.isValid(item));

		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList("window"));
		Assert.assertTrue(this.sut.isValid(item));

		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList("window", "embed"));
		Assert.assertTrue(this.sut.isValid(item));

		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList("embed", "window"));
		Assert.assertTrue(this.sut.isValid(item));

		Mockito.when(settings.getAccept_presentation_document_targets()).thenReturn(Arrays.asList("iframe", "window", "embed"));
		Assert.assertTrue(this.sut.isValid(item));
	}
}

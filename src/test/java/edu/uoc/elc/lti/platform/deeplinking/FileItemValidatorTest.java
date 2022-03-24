package edu.uoc.elc.lti.platform.deeplinking;

import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.lti.deeplink.content.FileItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

/**
 * @author xaracil@uoc.edu
 */
public class FileItemValidatorTest {
	private FileItemValidator sut;
	private Settings settings;

	@Before
	public void setUp() {
		this.settings = Mockito.mock(Settings.class);
		this.sut = new FileItemValidator(settings);
		Mockito.when(settings.getAccept_types()).thenReturn(Arrays.asList("file"));
	}

	private FileItem item() {
		return FileItem.builder()
						.url("http://www.uoc.edu")
						.title("Main Portal")
						.build();
	}

	@Test
	public void isValid() {
		Mockito.when(settings.getAccept_media_types()).thenReturn("application/pdf,text/html");

		Assert.assertTrue(this.sut.isValid(FileItem.builder().mediaType("application/pdf").build()));
		Assert.assertTrue(this.sut.isValid(FileItem.builder().mediaType("text/html").build()));
		Assert.assertFalse(this.sut.isValid(FileItem.builder().mediaType("text/plain").build()));
	}

	@Test
	public void isValidEmpty() {
		Mockito.when(settings.getAccept_media_types()).thenReturn(null);

		Assert.assertTrue(this.sut.isValid(FileItem.builder().mediaType("application/pdf").build()));
		Assert.assertTrue(this.sut.isValid(FileItem.builder().mediaType("text/html").build()));
		Assert.assertTrue(this.sut.isValid(FileItem.builder().mediaType("text/plain").build()));
	}

}

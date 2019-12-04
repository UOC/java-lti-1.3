package edu.uoc.elc.lti.platform.deeplinking;

import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.lti.deeplink.content.FileItem;
import edu.uoc.lti.deeplink.content.Item;
import edu.uoc.lti.deeplink.content.LinkItem;
import edu.uoc.lti.deeplink.content.LtiResourceItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

/**
 * @author xaracil@uoc.edu
 */
public class ItemValidatorTest {
	private ItemValidator sut;
	private Settings settings;

	@Before
	public void setUp() {
		this.settings = Mockito.mock(Settings.class);
		this.sut = new ItemValidator(settings);
	}

	@Test
	public void isValid() {
		Mockito.when(settings.getAccept_types()).thenReturn(Arrays.asList("link", "file"));

		Assert.assertTrue(this.sut.isValid(LinkItem.builder().build()));
		Assert.assertTrue(this.sut.isValid(FileItem.builder().build()));
		Assert.assertFalse(this.sut.isValid(LtiResourceItem.builder().build()));

		final Item item = Mockito.mock(Item.class);
		Mockito.when(item.getType()).thenReturn("otherType");
		Assert.assertFalse(this.sut.isValid(item));
	}
}

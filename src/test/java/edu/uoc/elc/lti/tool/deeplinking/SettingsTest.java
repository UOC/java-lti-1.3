package edu.uoc.elc.lti.tool.deeplinking;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class SettingsTest {
	private Settings sut;

	@Test
	public void getAcceptMediaTypesAsList() {
		this.sut = new Settings();
		this.sut.setAccept_media_types(Arrays.asList("application/pdf,text/html"));

		final List<String> list = this.sut.getAccept_media_types();
		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 2);
		Assert.assertEquals(list.get(0), "application/pdf");
		Assert.assertEquals(list.get(1), "text/html");
	}
}

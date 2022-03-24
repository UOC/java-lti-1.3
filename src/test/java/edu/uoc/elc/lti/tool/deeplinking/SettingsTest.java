package edu.uoc.elc.lti.tool.deeplinking;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author xaracil@uoc.edu
 */
public class SettingsTest {
	private Settings sut;

	@Test
	public void getAcceptMediaTypesAsList() {
		this.sut = new Settings();
		this.sut.setAccept_media_types("application/pdf,text/html");

		final List<String> list = Arrays.asList(this.sut.getAccept_media_types().split(","));
		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 2);
		Assert.assertEquals(list.get(0), "application/pdf");
		Assert.assertEquals(list.get(1), "text/html");
	}
}

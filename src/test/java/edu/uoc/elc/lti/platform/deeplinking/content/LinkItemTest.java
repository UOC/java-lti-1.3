package edu.uoc.elc.lti.platform.deeplinking.content;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import edu.uoc.lti.deeplink.content.LinkItem;

/**
 * @author xaracil@uoc.edu
 */
public class LinkItemTest {
	private LinkItem sut;

	@Test
	public void typeIsCorrect() {
		this.sut = LinkItem.builder().url("url").build();
		assertNotNull(this.sut);
		assertEquals("link", this.sut.getType());

		this.sut = new LinkItem("url");
		assertNotNull(this.sut);
		assertEquals("link", this.sut.getType());
	}

}
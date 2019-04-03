package edu.uoc.elc.lti.tool.deeplinking.content;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
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
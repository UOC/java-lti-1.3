package edu.uoc.elc.lti.tool.deeplinking.content;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class LinkTest {
	private Link sut;

	@Test
	public void typeIsCorrect() {
		this.sut = Link.builder().build();
		assertNotNull(this.sut);
		assertEquals("link", this.sut.getType());

		this.sut = new Link();
		assertNotNull(this.sut);
		assertEquals("link", this.sut.getType());
	}

}
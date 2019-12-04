package edu.uoc.elc.lti.platform.deeplinking.content;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uoc.lti.deeplink.content.Item;
import edu.uoc.lti.deeplink.content.LinkItem;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author xaracil@uoc.edu
 */
public class ItemTest {
	private Item sut;

	@Test
	public void typeIsCorrect() {
		this.sut = LinkItem.builder().url("url").build();
		assertNotNull(this.sut);
		assertEquals("link", this.sut.getType());

		this.sut = new LinkItem("url");
		assertNotNull(this.sut);
		assertEquals("link", this.sut.getType());
	}

	@Test
	public void testProperties() {
		this.sut = LinkItem.builder().url("url").build();

		// set some properties
		this.sut.set("key1", "value1");
		this.sut.set("key2", "value2");
		this.sut.set("key3", "value3");
		assertEquals(this.sut.getProperties().size(), 3);

		assertEquals("value1", this.sut.get("key1"));
		assertEquals("value2", this.sut.get("key2"));
		assertEquals("value3", this.sut.get("key3"));

		this.sut.set("key4", "value4");
		assertEquals(this.sut.getProperties().size(), 4);
		assertEquals("value4", this.sut.get("key4"));
	}

	@Test
	public void testBasicSerialize() throws JsonProcessingException {
		this.sut = LinkItem.builder().url("url").build();
		ObjectMapper mapper = new ObjectMapper();
		final String valueAsString = mapper.writeValueAsString(this.sut);
		assertNotNull(valueAsString);
		assertEquals("{\"type\":\"link\",\"url\":\"url\"}", valueAsString);
	}

	@Test
	public void testSerializeWithProperties() throws JsonProcessingException {
		this.sut = LinkItem.builder().url("url").build();
		// set some properties
		this.sut.set("key1", "value1");
		this.sut.set("key2", "value2");
		this.sut.set("key3", "value3");
		assertEquals(this.sut.getProperties().size(), 3);

		ObjectMapper mapper = new ObjectMapper();
		final String valueAsString = mapper.writeValueAsString(this.sut);
		assertNotNull(valueAsString);
		assertEquals("{\"type\":\"link\",\"url\":\"url\",\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\"}", valueAsString);
	}

}
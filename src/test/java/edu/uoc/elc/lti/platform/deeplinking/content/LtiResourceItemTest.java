package edu.uoc.elc.lti.platform.deeplinking.content;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class LtiResourceItemTest {
	private LtiResourceItem sut;

	@Test
	public void typeIsCorrect() {
		this.sut = new LtiResourceItem();
		assertNotNull(this.sut);
		assertEquals("ltiResourceLink", this.sut.getType());
	}

	@Test
	public void customPropertiesAddedToJSON() throws JsonProcessingException {
		this.sut = new LtiResourceItem();
		assertNotNull(this.sut);

		// set some properties
		this.sut.setCustom("key1", "value1");
		this.sut.setCustom("key2", "value2");
		this.sut.setCustom("key3", "value3");
		assertEquals(this.sut.getCustom().size(), 3);

		assertEquals("value1", this.sut.getCustom("key1"));
		assertEquals("value2", this.sut.getCustom("key2"));
		assertEquals("value3", this.sut.getCustom("key3"));

		ObjectMapper mapper = new ObjectMapper();
		final String valueAsString = mapper.writeValueAsString(this.sut);
		assertNotNull(valueAsString);
		assertEquals("{\"type\":\"ltiResourceLink\",\"custom\":{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\"}}", valueAsString);
	}

}
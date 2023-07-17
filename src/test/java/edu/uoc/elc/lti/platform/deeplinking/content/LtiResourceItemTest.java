package edu.uoc.elc.lti.platform.deeplinking.content;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uoc.lti.deeplink.content.LtiResourceItem;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author xaracil@uoc.edu
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
		Map<String, Object> custom = new HashMap<>();
		custom.put("key1", "value1");
		custom.put("key2", "value2");
		custom.put("key3", "value3");
		this.sut.setCustom(custom);
		assertEquals(this.sut.getCustom().size(), 3);

		assertEquals("value1", this.sut.getCustom().get("key1"));
		assertEquals("value2", this.sut.getCustom().get("key2"));
		assertEquals("value3", this.sut.getCustom().get("key3"));

		ObjectMapper mapper = new ObjectMapper();
		final String valueAsString = mapper.writeValueAsString(this.sut);
		assertNotNull(valueAsString);
		assertEquals("{\"type\":\"ltiResourceLink\",\"custom\":{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\"}}", valueAsString);
	}

}
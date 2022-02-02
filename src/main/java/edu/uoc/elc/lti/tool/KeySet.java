package edu.uoc.elc.lti.tool;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * Universitat Oberta de Catalunya
 * Made for the project lti-13
 */
@Data
@Builder
public class KeySet {
	private String id;
	private List<Key> keys;

	public Key getKey(String kid) {
		return Objects.nonNull(keys) ? keys.stream().filter(key -> key.getId().equals(kid)).findFirst().orElse(null) : null;
	}
}

package edu.uoc.elc.lti.tool;

import lombok.Builder;
import lombok.Data;

/**
 * Universitat Oberta de Catalunya
 * Made for the project lti-13
 */
@Data
@Builder
public class Key {
	private String id;
	private String privateKey;
	private String publicKey;
	private String algorithm;
}

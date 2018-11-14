package edu.uoc.elc.lti;

import lombok.Getter;
import lombok.experimental.Builder;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Builder
public class ToolDefinition {
	private String clientId;
	private String name;
	private String keySetUrl;
	private String accessTokenUrl;
	private String privateKey;
	private String publicKey;
}

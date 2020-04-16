package edu.uoc.elc.lti.tool;

import lombok.Builder;
import lombok.Value;

/**
 * @author xaracil@uoc.edu
 */
@Value
@Builder
public class ToolDefinition {
	String clientId;
	String name;
	String platform;
	String keySetUrl;
	String accessTokenUrl;
	String oidcAuthUrl;
	String privateKey;
	String publicKey;
	String deploymentId;
}

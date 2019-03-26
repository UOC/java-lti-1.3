package edu.uoc.elc.lti.platform;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Builder
public class AccessTokenRequest {
	private String grant_type;
	private String client_assertion_type;
	private String scope;
	private String client_assertion;
}

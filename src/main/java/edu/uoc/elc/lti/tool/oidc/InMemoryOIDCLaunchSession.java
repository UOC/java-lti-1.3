package edu.uoc.elc.lti.tool.oidc;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
public class InMemoryOIDCLaunchSession implements OIDCLaunchSession {
	private String state;
	private String nonce;
}

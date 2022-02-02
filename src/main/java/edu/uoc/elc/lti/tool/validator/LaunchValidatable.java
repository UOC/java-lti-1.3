package edu.uoc.elc.lti.tool.validator;

import edu.uoc.elc.lti.tool.Registration;
import edu.uoc.lti.claims.ClaimAccessor;
import edu.uoc.lti.oidc.OIDCLaunchSession;

/**
 * @author xaracil@uoc.edu
 */
public interface LaunchValidatable {
	boolean validate(String state, Registration toolDefinition, ClaimAccessor claimAccessor, OIDCLaunchSession oidcLaunchSession);

	String getReason();
}

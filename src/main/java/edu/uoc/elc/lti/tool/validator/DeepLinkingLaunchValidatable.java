package edu.uoc.elc.lti.tool.validator;

import edu.uoc.elc.lti.tool.ToolDefinition;
import edu.uoc.lti.claims.ClaimAccessor;
import edu.uoc.lti.oidc.OIDCLaunchSession;
import lombok.Getter;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class DeepLinkingLaunchValidatable implements LaunchValidatable {
	@Getter
	private String reason;

	@Override
	public boolean validate(String state, ToolDefinition toolDefinition, ClaimAccessor claimAccessor, OIDCLaunchSession oidcLaunchSession) {
		return false;
	}

}

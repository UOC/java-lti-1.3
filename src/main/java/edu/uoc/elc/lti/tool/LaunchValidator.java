package edu.uoc.elc.lti.tool;

import edu.uoc.elc.lti.tool.claims.ClaimAccessor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
public class LaunchValidator {
	private final ToolDefinition toolDefinition;
	private final ClaimAccessor claimAccessor;

	@Getter
	private String reason;


	public boolean validate(String token) {
		// TODO: implement
		return false;
	}
}

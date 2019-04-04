package edu.uoc.elc.lti.platform.ags;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@RequiredArgsConstructor
public enum ActivityProgressEnum {
	INITIALIZED("Initialized"),
	STARTED("Started"),
	IN_PROGRESS("InProgress"),
	SUBMITTED("Submitted"),
	COMPLETED("Completed")
	;

	private final String value;
}

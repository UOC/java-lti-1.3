package edu.uoc.elc.lti.platform.ags;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@RequiredArgsConstructor
public enum GradingProgressEnum {
	NOT_READY("NotReady"),
	FAILED("Failed"),
	PENDING("Pending"),
	PENDING_MANUAL("PendingManual"),
	FULLY_GRADED("FullyGraded")
	;

	private final String value;
}

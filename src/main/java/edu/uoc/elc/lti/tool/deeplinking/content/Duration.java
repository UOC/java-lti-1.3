package edu.uoc.elc.lti.tool.deeplinking.content;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Setter
@Builder
public class Duration {
	private Instant startDate;
	private Instant endDate;

	public String getStartDateTime() {
		return startDate != null ? startDate.toString() : null;
	}

	public String getEndDateTime() {
		return endDate != null ? endDate.toString() : null;
	}
}

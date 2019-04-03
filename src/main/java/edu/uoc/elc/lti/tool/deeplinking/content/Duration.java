package edu.uoc.elc.lti.tool.deeplinking.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Setter;

import java.time.Instant;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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

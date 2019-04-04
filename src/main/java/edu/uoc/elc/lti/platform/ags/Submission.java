package edu.uoc.elc.lti.platform.ags;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Setter;

import java.time.Instant;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Submission {
	/**
	 * Date and time in ISO 8601 format when a submission can start being submitted by learner
	 */
	private Instant startDateTime;

	public String getStartDateTime() {
		return startDateTime != null ? startDateTime.toString() : null;
	}

	/**
	 * Date and time in ISO 8601 format when a submission can last be submitted by learner
	 */
	private Instant endDateTime;

	public String getEndDateTime() {
		return endDateTime != null ? endDateTime.toString() : null;
	}
}

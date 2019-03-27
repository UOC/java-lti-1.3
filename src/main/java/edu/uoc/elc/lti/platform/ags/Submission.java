package edu.uoc.elc.lti.platform.ags;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Submission {
	/**
	 * Date and time in ISO 8601 format when a submission can start being submitted by learner
	 */
	private String startDateTime;

	/**
	 * Date and time in ISO 8601 format when a submission can last be submitted by learner
	 */
	private String endDateTime;
}

package edu.uoc.elc.lti.platform.ags;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
public class Submission {
	@JsonProperty
	private String startDateTime;
	@JsonProperty
	private String endDateTime;
}

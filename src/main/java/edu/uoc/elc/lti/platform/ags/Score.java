package edu.uoc.elc.lti.platform.ags;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
@Builder
public class Score {
	@JsonProperty
	private String userId;
	@JsonProperty
	private double scoreGiven;
	@JsonProperty
	private double scoreMaximum;
	@JsonProperty
	private String comment;
	@JsonProperty
	private String timeStamp; // TODO: set as Date?
	@JsonProperty
	private String activityProgress; // TODO: set as Enum?
	@JsonProperty
	private String gradingProgress; // TODO: set as Enum?
}

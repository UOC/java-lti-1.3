package edu.uoc.elc.lti.platform.ags;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
public class Result {
	@JsonProperty
	private String id;
	@JsonProperty
	private String userId;
	@JsonProperty
	private double resultScore;
	@JsonProperty
	private double resultMaximun;
	@JsonProperty
	private String comment;
	@JsonProperty
	private String scoreOf;
}

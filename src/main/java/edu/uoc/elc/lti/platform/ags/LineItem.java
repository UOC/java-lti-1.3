package edu.uoc.elc.lti.platform.ags;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
public class LineItem {
	@JsonProperty
	private String id;
	@JsonProperty
	private double scoreMaximum;
	@JsonProperty
	private String label;
	@JsonProperty
	private String tag;
	@JsonProperty
	private String resourceId;
	@JsonProperty
	private String resourceLinkId;
	@JsonProperty
	private Submission submission;
}

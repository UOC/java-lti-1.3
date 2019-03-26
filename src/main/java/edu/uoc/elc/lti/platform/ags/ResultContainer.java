package edu.uoc.elc.lti.platform.ags;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
public class ResultContainer {
	@JsonProperty
	private List<Result> results;
}

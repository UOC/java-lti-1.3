package edu.uoc.elc.lti.platform.ags;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultContainer {
	private List<Result> results;
}

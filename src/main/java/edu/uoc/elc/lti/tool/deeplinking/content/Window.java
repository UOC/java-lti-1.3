package edu.uoc.elc.lti.tool.deeplinking.content;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
@Builder
public class Window {
	private String targetName;
	private int with;
	private int height;
	private String windowFeatures;
}

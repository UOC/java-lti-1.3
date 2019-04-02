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
public class Presentation {
	private String documentTarget;
	private int width;
	private int height;

}

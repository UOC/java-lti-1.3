package edu.uoc.elc.lti.tool;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xaracil@uoc.edu
 */
@Getter
@Setter
public class Context {
	private String id;
	private String label;
	private String title;
	private List<String> type;

}

package edu.uoc.elc.lti.tool;

import java.util.List;

import lombok.Data;

/**
 * @author xaracil@uoc.edu
 */
@Data
public class Context {
	private String id;
	private String label;
	private String title;
	private List<String> type;

}

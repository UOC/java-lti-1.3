package edu.uoc.elc.lti.tool;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Universitat Oberta de Catalunya
 * Made for the project lti-13
 */
@Getter
@Setter
public class GroupService {
	private String context_groups_url;
	private String context_group_sets_url;
	private List<String> scope;
	private List<String> service_versions;
}

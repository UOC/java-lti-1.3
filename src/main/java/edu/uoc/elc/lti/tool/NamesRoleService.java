package edu.uoc.elc.lti.tool;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xaracil@uoc.edu
 */
@Getter
@Setter
public class NamesRoleService {
	private String context_memberships_url;
	private List<String> service_versions;
}

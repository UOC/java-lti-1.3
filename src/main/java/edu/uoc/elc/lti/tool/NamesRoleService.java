package edu.uoc.elc.lti.tool;

import java.util.List;

import lombok.Data;

/**
 * @author xaracil@uoc.edu
 */
@Data
public class NamesRoleService {
	private String context_memberships_url;
	private List<String> service_versions;
}

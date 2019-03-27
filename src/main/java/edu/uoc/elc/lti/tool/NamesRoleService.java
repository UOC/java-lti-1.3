package edu.uoc.elc.lti.tool;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
public class NamesRoleService {
	private String context_memberships_url;
	private List<String> service_versions;
}

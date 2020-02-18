package edu.uoc.elc.lti.platform;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xaracil@uoc.edu
 */
@Getter
@Setter
public class NamesRoleServiceResponse {
	private String id;
	private List<Member> members;
}

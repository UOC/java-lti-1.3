package edu.uoc.elc.lti.platform;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xaracil@uoc.edu
 */
@Getter
@Setter
@ToString
public class NamesRoleServiceResponse {
	private String id;
	private List<Member> members;
}

package edu.uoc.lti.jwt.claims;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
public class Token {
	private String name;
	private Map<String, Object> claims;
}

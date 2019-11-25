package edu.uoc.lti.jwt.claims;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
public class Tokens {
	private List<Token> tokens;
}

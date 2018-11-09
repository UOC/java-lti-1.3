package edu.uoc.elc.lti;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class InvalidTokenException extends RuntimeException {
	public InvalidTokenException(String message) {
		super(message);
	}
}

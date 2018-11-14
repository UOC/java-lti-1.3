package edu.uoc.elc.lti.exception;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class InvalidTokenException extends RuntimeException {
	public InvalidTokenException(String message) {
		super(message);
	}
}

package edu.uoc.elc.lti;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
@Builder
public class User {
	private String id;
	private String givenName;
	private String familyName;
	private String middleName;
	private String picture;
	private String email;
	private String name;
}

package edu.uoc.elc.lti;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@AllArgsConstructor
@Getter
public enum RolesEnum {
	LEARNER("http://purl.imsglobal.org/vocab/lis/v2/membership#Learner"),
	MENTOR("http://purl.imsglobal.org/vocab/lis/v2/membership#Mentor"),
	INSTRUCTOR("http://purl.imsglobal.org/vocab/lis/v2/membership#Instructor")
	;

	private String name;

}

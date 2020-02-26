package edu.uoc.elc.lti.tool;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xaracil@uoc.edu
 */
@Getter
@Setter
/*
 * To fix an issue with Moodle 3.8 not compliant with LTI. While LTI 1.3
 * specifies the key to be "product_family_code", Moodle 3.8 uses
 * "familiy_code". The @JsonIgnoreProperties annotation ignores the bad key for
 * now without remapping it to the original spec.
 * 
 * @author heutelbeck
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Platform {
	private String guid;
	private String name;
	private String contact_email;
	private String description;
	private String url;
	private String product_family_code;
	private String version;
}

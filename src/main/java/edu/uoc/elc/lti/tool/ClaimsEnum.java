package edu.uoc.elc.lti.tool;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@AllArgsConstructor
@Getter
public enum ClaimsEnum {
	// header claims
	KID("kid"),

	// general claims
	MESSAGE_TYPE("https://purl.imsglobal.org/spec/lti/claim/message_type"),
	GIVEN_NAME("given_name"),
	FAMILY_NAME("family_name"),
	MIDDLE_NAME("middle_name"),
	PICTURE("picture"),
	EMAIL("email"),
	NAME("name"),
	NONCE("nonce"),
	VERSION("https://purl.imsglobal.org/spec/lti/claim/version"),
	LOCALE("locale"),
	RESOURCE_LINK("https://purl.imsglobal.org/spec/lti/claim/resource_link"),
	CONTEXT("https://purl.imsglobal.org/spec/lti/claim/context"),
	ROLES("https://purl.imsglobal.org/spec/lti/claim/roles"),
	TOOL_PLATFORM("https://purl.imsglobal.org/spec/lti/claim/tool_platform"),
	ENDPOINT("https://purl.imsglobal.org/spec/lti-ags/claim/endpoint"),
	NAMES_ROLE_SERVICE("https://purl.imsglobal.org/spec/lti-nrps/claim/namesroleservice"),
	CALIPER_SERVICE("https://purl.imsglobal.org/spec/lti-ces/claim/caliper-endpoint-service"),
	PRESENTATION("https://purl.imsglobal.org/spec/lti/claim/launch_presentation"),
	CUSTOM("https://purl.imsglobal.org/spec/lti/claim/custom"),
	DEPLOYMENT_ID("https://purl.imsglobal.org/spec/lti/claim/deployment_id")
	;

	private String name;
}

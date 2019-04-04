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
	ASSIGNMENT_GRADE_SERVICE("https://purl.imsglobal.org/spec/lti-ags/claim/endpoint"),
	NAMES_ROLE_SERVICE("https://purl.imsglobal.org/spec/lti-nrps/claim/namesroleservice"),
	CALIPER_SERVICE("https://purl.imsglobal.org/spec/lti-ces/claim/caliper-endpoint-service"),
	PRESENTATION("https://purl.imsglobal.org/spec/lti/claim/launch_presentation"),
	CUSTOM("https://purl.imsglobal.org/spec/lti/claim/custom"),

	// deep linking claims
	DEPLOYMENT_ID("https://purl.imsglobal.org/spec/lti/claim/deployment_id"),
	AUTHORIZED_PART("azp"),
	DEEP_LINKING_SETTINGS("https://purl.imsglobal.org/spec/lti-dl/claim/deep_linking_settings"),
	DEEP_LINKING_CONTENT_ITEMS("https://purl.imsglobal.org/spec/lti-dl/claim/content_items"),
	DEEP_LINKING_MESSAGE("https://purl.imsglobal.org/spec/lti-dl/claim/msg"),
	DEEP_LINKING_LOG("https://purl.imsglobal.org/spec/lti-dl/claim/log"),
	DEEP_LINKING_ERROR_MESSAGE("https://purl.imsglobal.org/spec/lti-dl/claim/errormsg"),
	DEEP_LINKING_ERROR_LOG("https://purl.imsglobal.org/spec/lti-dl/claim/errorlog"),
	DEEP_LINKING_DATA("https://purl.imsglobal.org/spec/lti-dl/data")
	;

	private String name;
}

package edu.uoc.lti.jwt.claims;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
@Getter
public class TestLaunch {
	private final String name;
	private final Map<String, Object> header;
	private final Map<String, Object> payload;
	private final List<String> specialFieldsToKeepFromPayload;
}

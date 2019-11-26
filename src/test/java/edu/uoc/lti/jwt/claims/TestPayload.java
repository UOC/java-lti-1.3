package edu.uoc.lti.jwt.claims;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
@Getter
public class TestPayload {
	private final String name;
	private final Map<String, Object> payload;
}

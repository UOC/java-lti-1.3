package edu.uoc.lti.jwt.claims;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.net.URI;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class TokensLoader {
	public Tokens loadTokens(URI uri) {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		try {
			Tokens tokens = mapper.readValue(new File(uri), Tokens.class);
			return tokens;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

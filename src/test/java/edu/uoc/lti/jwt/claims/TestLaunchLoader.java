package edu.uoc.lti.jwt.claims;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.java.Log;

/**
 * @author xaracil@uoc.edu
 */
@Log
public class TestLaunchLoader {
	private final static String HEADER = "header.json";
	private final static String PAYLOAD = "payload.json";
	private final static String KEEP = "keep.json";

	private ObjectMapper mapper = new ObjectMapper();

	public List<TestLaunch> loadTestLaunches(URI uri) {
		List<TestLaunch> payloads = new ArrayList<>();

		File directory = new File(uri);
		if (directory.isDirectory()) {
			final File[] files = directory.listFiles(pathname -> pathname.isDirectory());
			for (File file : files) {
				try {
					payloads.add(loadTestLaunch(file));
				} catch (IOException e) {
					log.warning("Cannot load test launch: " + e.getMessage());
				}
			}
		}
		return payloads;
	}

	private TestLaunch loadTestLaunch(File testDirectory) throws IOException {
		File headerFile = new File(testDirectory, HEADER);
		File payloadFile = new File(testDirectory, PAYLOAD);
		File keepFile = new File(testDirectory, KEEP);
		return new TestLaunch(testDirectory.getName(), loadJsonFileAsMap(headerFile), loadJsonFileAsMap(payloadFile),
				loadJsonFileAsList(keepFile));
	}

	private Map loadJsonFileAsMap(File file) throws IOException {
		return file.exists() ? mapper.readValue(file, Map.class) : null;
	}

	private List loadJsonFileAsList(File file) throws IOException {
		return file.exists() ? mapper.readValue(file, List.class) : null;
	}
}

package edu.uoc.lti.jwt.claims;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class PayloadLoader {
	private ObjectMapper mapper = new ObjectMapper();

	public List<TestPayload> loadPayloads(URI uri) {
		List<TestPayload> payloads = new ArrayList<>();

		File directory = new File(uri);
		if (directory.isDirectory()) {
			final File[] files = directory.listFiles(pathname -> pathname.isFile());
			for (File file : files) {
				try {
					payloads.add(loadPayload(file));
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		} else {
			try {
				payloads.add(loadPayload(new File(uri)));
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		return payloads;
	}

	private TestPayload loadPayload(File file) throws IOException {
		final Map payload = mapper.readValue(file, Map.class);
		return new TestPayload(file.getName(), payload);
	}
}

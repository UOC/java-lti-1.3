package edu.uoc.elc.lti.platform.deeplinking;

import edu.uoc.elc.lti.platform.deeplinking.content.Item;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
@Getter
public class DeepLinkingResponse {
	private final String platformName;
	private final String toolName;
	private final String azp;

	private final String kid;
	private final String deploymentId;
	private final String data;
	private final List<Item> itemList;

	@Setter
	private String message;
	@Setter
	private String log;
	@Setter
	private String errorMessage;
	@Setter
	private String errorLog;
}

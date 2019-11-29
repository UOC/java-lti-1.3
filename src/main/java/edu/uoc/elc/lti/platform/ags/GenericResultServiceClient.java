package edu.uoc.elc.lti.platform.ags;

import edu.uoc.elc.lti.exception.UnauthorizedAgsCallException;
import edu.uoc.lti.ags.Result;
import edu.uoc.lti.ags.ResultServiceClient;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
public class GenericResultServiceClient {
	private final URI lineItemsUri;
	private final boolean canRead;
	private final ResultServiceClient resultServiceClient;

	public List<Result> getLineItemResults(String id, Integer limit, Integer page, String userId) {
		if (!canRead) {
			throw new UnauthorizedAgsCallException("getLineItemResults");
		}
		resultServiceClient.setServiceUri(lineItemsUri);
		return resultServiceClient.getLineItemResults(id, limit, page, userId);
	}
}

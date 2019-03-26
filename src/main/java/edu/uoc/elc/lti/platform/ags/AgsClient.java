package edu.uoc.elc.lti.platform.ags;

import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public interface AgsClient {
	List<LineItem> getLineItems(int limit, int page, String resourceLinkId, String tag, String resourceId);
	LineItem createLineItem(LineItem lineItem);
	LineItem getLineItem(String id);
	LineItem updateLineItem(String id, LineItem lineItem);
	void deleteLineItem(String id);
	ResultContainer getLineItemResults(String id, int limit, int page, String userId);
	boolean score(String lineItemId, Score score);
}

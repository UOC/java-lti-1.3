package edu.uoc.elc.lti.platform.ags.empty;

import edu.uoc.elc.lti.platform.ags.GenericScoreServiceClient;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class EmptyScoreServiceClient extends GenericScoreServiceClient {

	public EmptyScoreServiceClient() {
		super(null, false, null);
	}
}

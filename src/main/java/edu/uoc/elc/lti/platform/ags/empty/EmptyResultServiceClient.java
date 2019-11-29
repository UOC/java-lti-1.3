package edu.uoc.elc.lti.platform.ags.empty;

import edu.uoc.elc.lti.platform.ags.GenericResultServiceClient;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class EmptyResultServiceClient extends GenericResultServiceClient {
	public EmptyResultServiceClient() {
		super(false, null);
	}
}

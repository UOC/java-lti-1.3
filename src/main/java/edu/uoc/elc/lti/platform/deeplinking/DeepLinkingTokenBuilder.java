package edu.uoc.elc.lti.platform.deeplinking;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public interface DeepLinkingTokenBuilder {

	String build(DeepLinkingResponse response);
}

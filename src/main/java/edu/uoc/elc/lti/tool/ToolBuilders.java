package edu.uoc.elc.lti.tool;

import edu.uoc.lti.accesstoken.AccessTokenRequestBuilder;
import edu.uoc.lti.ags.LineItemServiceClient;
import edu.uoc.lti.ags.ResultServiceClient;
import edu.uoc.lti.ags.ScoreServiceClient;
import edu.uoc.lti.clientcredentials.ClientCredentialsTokenBuilder;
import edu.uoc.lti.deeplink.DeepLinkingTokenBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
@Getter
public class ToolBuilders {
	private final ClientCredentialsTokenBuilder clientCredentialsTokenBuilder;
	private final AccessTokenRequestBuilder accessTokenRequestBuilder;
	private final DeepLinkingTokenBuilder deepLinkingTokenBuilder;
	private final LineItemServiceClient lineItemServiceClient;
	private final ResultServiceClient resultServiceClient;
	private final ScoreServiceClient scoreServiceClient;
}

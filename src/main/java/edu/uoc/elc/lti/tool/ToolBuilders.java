package edu.uoc.elc.lti.tool;

import edu.uoc.lti.accesstoken.AccessTokenRequestBuilder;
import edu.uoc.lti.clientcredentials.ClientCredentialsTokenBuilder;
import edu.uoc.lti.deeplink.DeepLinkingTokenBuilder;
import lombok.Value;

/**
 * @author xaracil@uoc.edu
 */
@Value
public class ToolBuilders {
	ClientCredentialsTokenBuilder clientCredentialsTokenBuilder;
	AccessTokenRequestBuilder accessTokenRequestBuilder;
	DeepLinkingTokenBuilder deepLinkingTokenBuilder;
}

package edu.uoc.elc.lti.platform.accesstoken;

import edu.uoc.elc.lti.platform.PlatformClient;
import edu.uoc.elc.lti.tool.ScopeEnum;
import edu.uoc.elc.lti.tool.ToolDefinition;
import edu.uoc.lti.accesstoken.AccessTokenRequest;
import edu.uoc.lti.accesstoken.AccessTokenRequestBuilder;
import edu.uoc.lti.clientcredentials.ClientCredentialsRequest;
import edu.uoc.lti.clientcredentials.ClientCredentialsTokenBuilder;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URL;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
public class AccessTokenRequestHandler {
	private final String kid;
	private final ToolDefinition toolDefinition;
	private final ClientCredentialsTokenBuilder clientCredentialsTokenBuilder;
	private final AccessTokenRequestBuilder accessTokenRequestBuilder;

	public AccessTokenResponse getAccessToken() throws IOException {
		AccessTokenRequest request = AccessTokenRequest.builder()
						.grant_type("client_credentials")
						.client_assertion_type("urn:ietf:params:oauth:client-assertion-type:jwt-bearer")
						.scope(ScopeEnum.AGS_SCOPE_LINE_ITEM.getScope() + " " + ScopeEnum.AGS_SCOPE_RESULT.getScope() + " " + ScopeEnum.NAMES_AND_ROLES_SCOPE.getScope())
						.client_assertion(getClientAssertion())
						.build();

		// do a post to the service
		return postToService(new URL(toolDefinition.getAccessTokenUrl()), request);
	}

	private String getClientAssertion() {
		ClientCredentialsRequest clientCredentialsRequest = new ClientCredentialsRequest(kid,
						toolDefinition.getName(),
						toolDefinition.getClientId(),
						toolDefinition.getAccessTokenUrl());
		return clientCredentialsTokenBuilder.build(clientCredentialsRequest);
	}

	private AccessTokenResponse postToService(URL url, AccessTokenRequest request) throws IOException {
		final String bodyAsString = accessTokenRequestBuilder.build(request);
		final String contentType = accessTokenRequestBuilder.getContentType();

		PlatformClient platformClient = new PlatformClient();
		return platformClient.post(url, bodyAsString, contentType, AccessTokenResponse.class);
	}
}

package edu.uoc.elc.lti.platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uoc.elc.lti.tool.ScopeEnum;
import edu.uoc.elc.lti.tool.ToolDefinition;
import edu.uoc.lti.clientcredentials.ClientCredentialsRequest;
import edu.uoc.lti.clientcredentials.ClientCredentialsTokenBuilder;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@AllArgsConstructor
public class RequestHandler {
	private String kid;
	private ToolDefinition toolDefinition;
	private ClientCredentialsTokenBuilder clientCredentialsTokenBuilder;

	public AccessTokenResponse getAccessToken() throws IOException {
		AccessTokenRequest request = AccessTokenRequest.builder()
						.grant_type("client_credentials")
						.client_assertion_type("urn:ietf:params:oauth:client-assertion-type:jwt-bearer")
						.scope(ScopeEnum.AGS_SCOPE_LINE_ITEM.getScope() + " " + ScopeEnum.AGS_SCOPE_RESULT.getScope() + " " + ScopeEnum.NAMES_AND_ROLES_SCOPE.getScope())
						.client_assertion(getClientAssertion())
						.build();

		// do a post to the service
		return post(new URL(toolDefinition.getAccessTokenUrl()), request, AccessTokenResponse.class);
	}

	private String getClientAssertion() {
		ClientCredentialsRequest clientCredentialsRequest = new ClientCredentialsRequest(kid,
						toolDefinition.getName(),
						toolDefinition.getClientId(),
						toolDefinition.getAccessTokenUrl());
		return clientCredentialsTokenBuilder.build(clientCredentialsRequest);
	}


	private final static String CHARSET = "UTF-8";

	private <T> T post(URL url, Object body, Class<T> type) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		String bodyAsString = objectMapper.writeValueAsString(body);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Accept-Charset", CHARSET);
		connection.setRequestProperty("Content-Type", "application/json"); // FIXME: fixed JSON body

		try (OutputStream output = connection.getOutputStream()) {
			output.write(bodyAsString.getBytes(CHARSET));
		}

		String response = IOUtils.toString(connection.getInputStream(), CHARSET);
		return objectMapper.readValue(response, type);
	}
}

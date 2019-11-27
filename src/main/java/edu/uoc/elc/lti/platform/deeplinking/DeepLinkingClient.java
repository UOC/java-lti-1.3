package edu.uoc.elc.lti.platform.deeplinking;

import edu.uoc.elc.lti.exception.InvalidLTICallException;
import edu.uoc.elc.lti.platform.PlatformClient;
import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.lti.deeplink.DeepLinkingResponse;
import edu.uoc.lti.deeplink.DeepLinkingTokenBuilder;
import edu.uoc.lti.deeplink.content.Item;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
public class DeepLinkingClient {

	private final DeepLinkingTokenBuilder deepLinkingTokenBuilder;

	private final String platformName;
	private final String toolName;
	private final String azp;
	private final String kid;

	private final String deploymentId;
	private final Settings settings;

	@Getter
	private List<Item> itemList = new ArrayList<>();

	@Getter
	private String jwt;

	public boolean canAddItem() {
		return settings.isAccept_multiple() || itemList.size() == 0;
	}

	public void addItem(Item item) {
		// check for multiple content item
		if (!canAddItem()) {
			throw new InvalidLTICallException("Platform doesn't allow multiple content items");
		}

		ItemValidator itemValidator = ItemValidatorFactory.itemValidatorFor(item, settings);
		if (!itemValidator.isValid(item)) {
			throw new InvalidLTICallException(itemValidator.getMessage());
		}

		itemList.add(item);
	}

	public String sendResponseBackToPlatform() throws IOException {
		this.jwt = generateJWT();
		URL url = new URL(settings.getDeep_link_return_url());
		return postToService(url);
	}

	private String generateJWT() {
		DeepLinkingResponse deepLinkingResponse = new DeepLinkingResponse(platformName,
						toolName, azp, kid, deploymentId, settings.getData(), itemList);
		return deepLinkingTokenBuilder.build(deepLinkingResponse);
	}

	private String postToService(URL url) throws IOException {
		PlatformClient platformClient = new PlatformClient();
		final String body = "JWT=" + this.jwt;
		return platformClient.post(url, body, null, String.class);
	}
}

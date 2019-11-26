package edu.uoc.elc.lti.platform.deeplinking;

import edu.uoc.elc.lti.exception.InvalidLTICallException;
import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.lti.deeplink.DeepLinkingResponse;
import edu.uoc.lti.deeplink.DeepLinkingTokenBuilder;
import edu.uoc.lti.deeplink.content.FileItem;
import edu.uoc.lti.deeplink.content.Item;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@RequiredArgsConstructor
@Getter
@Setter
public class DeepLinkingClient {

	private final DeepLinkingTokenBuilder deepLinkingTokenBuilder;

	private final String platformName;
	private final String toolName;
	private final String azp;
	private final String kid;

	private final String deploymentId;
	private final Settings settings;

	private List<Item> itemList = new ArrayList<>();

	public boolean canAddItem() {
		return settings.isAccept_multiple() || itemList.size() == 0;
	}

	public boolean canAddItemOfType(String type) {
		return settings.getAccept_types().contains(type);
	}

	public boolean canAddFileItemOfMediaType(String type) {
		final List<String> acceptMediaTypes = settings.getAccept_media_types();
		return (acceptMediaTypes != null && acceptMediaTypes.size() > 0 ? acceptMediaTypes.contains(type) : true);
	}

	public void addItem(Item item) {
		// check for multiple content item
		if (!canAddItem()) {
			throw new InvalidLTICallException("Platform doesn't allow multiple content items");
		}

		// check for accepted types
		if (!canAddItemOfType(item.getType())) {
			throw new InvalidLTICallException("Platform doesn't allow content items of type " + item.getType());
		}

		if (item instanceof FileItem) {
			FileItem fileItem = (FileItem) item;
			if (!canAddFileItemOfMediaType(fileItem.getMediaType())) {
				throw new InvalidLTICallException("Platform doesn't allow file content items of media type " + fileItem.getMediaType());
			}
		}

		// TODO: add more validations like presentation

		itemList.add(item);
	}

	/**
	 * Gets the default text to be used as the title or alt text for the content item returned by the tool
	 * @return the default title provided by the platform.
	 */
	public String getDefaultTitle() {
		return settings.getTitle();
	}

	/**
	 * Gets the default text to be used as the visible text for the content item returned by the tool.
	 * If no text is returned by the tool, the platform may use the value of the title parameter instead (if any).
	 * @return the default text provided by the platform.
	 */
	public String getDefaultText() {
		return settings.getText();
	}

	/**
	 * Performs the DeepLinking response back to the platform
	 */
	public void perform() throws IOException {

		// generate the JWT
		DeepLinkingResponse deepLinkingResponse = new DeepLinkingResponse(platformName,
						toolName, azp, kid, deploymentId, settings.getData(), itemList);
		String token = deepLinkingTokenBuilder.build(deepLinkingResponse);


		URL url = new URL(settings.getDeep_link_return_url());
		// post the JWT back to the platform
		post(url, "JWT=" + token, null);
	}

	private final static String CHARSET = "UTF-8";

	private <T> String post(URL url, String body, Class<T> type) throws IOException {

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Accept-Charset", CHARSET);

		try (OutputStream output = connection.getOutputStream()) {
			output.write(body.getBytes(CHARSET));
		}

		String response = IOUtils.toString(connection.getInputStream(), CHARSET);
		return response;
		//return objectMapper.readValue(response, type);
	}
}

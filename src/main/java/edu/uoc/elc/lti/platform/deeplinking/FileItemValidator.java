package edu.uoc.elc.lti.platform.deeplinking;

import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.lti.deeplink.content.FileItem;
import edu.uoc.lti.deeplink.content.Item;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author xaracil@uoc.edu
 */
public class FileItemValidator extends ItemValidator {
	public FileItemValidator(Settings settings) {
		super(settings);
	}

	@Override
	public boolean isValid(Item item) {
		boolean valid = super.isValid(item);
		valid = valid && fileItemIsValid((FileItem) item);
		return valid;
	}

	private boolean fileItemIsValid(FileItem item) {
		if (!mediaTypeIsValid(item.getMediaType())) {
			message = "Platform doesn't allow file content items of media type " + item.getMediaType();
			return false;
		}

		return true;
	}

	private boolean mediaTypeIsValid(String type) {
		final List<String> acceptMediaTypes = Arrays.asList(settings.getAccept_media_types().split(","));
		return (acceptMediaTypes != null && acceptMediaTypes.size() > 0 ? acceptMediaTypes.contains(type) : true);
	}

}

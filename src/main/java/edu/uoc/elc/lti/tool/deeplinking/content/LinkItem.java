package edu.uoc.elc.lti.tool.deeplinking.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LinkItem extends Item {
	private static String TYPE = "link";

	private String title;
	private final String url;
	private Image icon;
	private Image thumbnail;

	private Window window;
	private IFrame iframe;
	private Embed embed;

	public LinkItem(String url) {
		super(TYPE);
		this.url = url;
	}

	@Builder
	public LinkItem(String title, String url, Image icon, Image thumbnail) {
		this(url);
		this.title = title;
		this.icon = icon;
		this.thumbnail = thumbnail;
	}
}

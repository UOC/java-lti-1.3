package edu.uoc.elc.lti.tool.deeplinking.content;

import lombok.*;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
public class Link extends Item {
	private static String TYPE = "link";

	private String title;
	private String url;
	private Image icon;
	private Image thumbnail;

	public Link() {
		super(TYPE);
	}

	@Builder
	public Link(String title, String url, Image icon, Image thumbnail) {
		this();
		this.title = title;
		this.url = url;
		this.icon = icon;
		this.thumbnail = thumbnail;
	}
}

package edu.uoc.elc.lti.tool.deeplinking;

import java.util.List;

import lombok.Data;

/**
 * @author xaracil@uoc.edu
 */
@Data
public class Settings {
	private List<String> accept_types;
	private List<String> accept_media_types;
	private List<String> accept_presentation_document_targets;
	private boolean accept_multiple;
	private boolean auto_create;
	private String title;
	private String text;
	private String data;
	private String deep_link_return_url;
}

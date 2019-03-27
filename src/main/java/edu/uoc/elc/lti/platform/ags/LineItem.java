package edu.uoc.elc.lti.platform.ags;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineItem {
	/**
	 * URL end point for the resource. It must be present on all responses containing the resource and may be used for subsequent operations on that resource.
	 */
	private String id;

	/**
	 *
	 */
	@NonNull private double scoreMaximum;

	/**
	 * label to use in the Tool Consumer UI (Gradebook)
	 */
	@NonNull private String label;

	/**
	 * Additional information about the line item; may be used by the tool to identify line items attached to the same resource or resource link (example: grade, originality, participation)
	 */
	private String tag;

	/**
	 * Tool resource identifier for which this line item is receiving scores from
	 */
	private String resourceId;

	/**
	 * Id of the tool platform's resource link to which this line item is attached to
	 */
	private String resourceLinkId;

	/**
	 * Submission information
	 */
	private Submission submission;
}

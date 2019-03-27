package edu.uoc.elc.lti.platform.ags;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Score {
	/**
	 * Recipient of the score, usually a student. Must be present when publishing a score update through Scores
	 */
	private String userId;
	/**
	 * Current score received in the tool for this line item and user, in scale with scoreMaximum
	 */
	private double scoreGiven;
	/**
	 * Maximum possible score for this result; It must be present if scoreGiven is present.
	 */
	private double scoreMaximum;
	/**
	 * Comment visible to the student about this score.
	 */
	private String comment;
	/**
	 * Date and time when the score was modified in the tool. Should use subsecond precision.
	 */
	private String timeStamp; // TODO: set as Date?
	/**
	 * Indicate to the tool platform the status of the user towards the activity's completion.
	 * [ Initialized, Started, InProgress, Submitted, Completed ]
	 */
	private String activityProgress; // TODO: set as Enum?
	/**
	 * Indicate to the platform the status of the grading process, including allowing to inform when human intervention is needed. A value other than FullyGraded may cause the tool platform to ignore the scoreGiven value if present.
	 * [ NotReady, Failed, Pending, PendingManual, FullyGraded ]
	 */
	private String gradingProgress; // TODO: set as Enum?
}

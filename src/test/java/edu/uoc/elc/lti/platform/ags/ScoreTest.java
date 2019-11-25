package edu.uoc.elc.lti.platform.ags;

import edu.uoc.lti.ags.ActivityProgressEnum;
import edu.uoc.lti.ags.GradingProgressEnum;
import edu.uoc.lti.ags.Score;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
public class ScoreTest {

	private Score sut;

	@Test
	public void getTimeStamp() {
		final Instant now = Instant.now();
		this.sut = Score.builder()
						.timeStamp(now)
						.build();

		Assert.assertEquals(now.toString(), this.sut.getTimeStamp());
	}

	@Test
	public void getActivityProgress() {
		for (ActivityProgressEnum activityProgressEnum : ActivityProgressEnum.values()) {
			this.sut = Score.builder()
							.activityProgress(activityProgressEnum)
							.build();

			Assert.assertEquals(activityProgressEnum.getValue(), this.sut.getActivityProgress());
		}
	}

	@Test
	public void getGradingProgress() {
		for (GradingProgressEnum gradingProgressEnum : GradingProgressEnum.values()) {
			this.sut = Score.builder()
							.gradingProgress(gradingProgressEnum)
							.build();

			Assert.assertEquals(gradingProgressEnum.getValue(), this.sut.getGradingProgress());
		}
	}
}
package edu.uoc.elc.lti.tool.deeplinking.content;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
public class DataItem extends Item {
	private String data;

	@Builder
	public DataItem(String type, String data) {
		super(type);
		this.data = data;
	}
}

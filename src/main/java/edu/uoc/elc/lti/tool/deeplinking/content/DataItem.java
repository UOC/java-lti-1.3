package edu.uoc.elc.lti.tool.deeplinking.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DataItem extends Item {
	private String data;

	@Builder
	public DataItem(String type, String data) {
		super(type);
		this.data = data;
	}
}

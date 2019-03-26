package edu.uoc.elc.lti.tool;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Xavi Aracil <xaracil@uoc.edu>
 */
@Getter
@Setter
public class AssignmentGradeService {
	private List<String> scope;
	private String lineitems;

	public boolean canReadGrades() {
		return scope != null && scope.contains(ScopeEnum.AGS_SCOPE_RESULT.getScope());
	}

	public boolean canReadLineItems() {
		return scope != null && scope.contains(ScopeEnum.AGS_SCOPE_LINE_ITEM.getScope());
	}

	public boolean canWrite() {
		return scope != null && scope.contains(ScopeEnum.AGS_SCOPE_LINE_ITEM.getScope());
	}
}

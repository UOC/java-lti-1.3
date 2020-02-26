package edu.uoc.elc.lti.tool;

import java.util.List;

import lombok.Data;

/**
 * @author xaracil@uoc.edu
 */
@Data
public class AssignmentGradeService {
	private List<String> scope;
	private String lineitems;
	private String lineitem;

	public boolean canReadResults() {
		return scope != null && scope.contains(ScopeEnum.AGS_SCOPE_RESULT.getScope());
	}

	public boolean canReadLineItems() {
		return canManageLineItems() || (scope != null && scope.contains(ScopeEnum.AGS_SCOPE_LINE_ITEM_READONLY.getScope()));
	}

	public boolean canManageLineItems() {
		return scope != null && scope.contains(ScopeEnum.AGS_SCOPE_LINE_ITEM.getScope());
	}

	public boolean canScore() {
		return scope != null && scope.contains(ScopeEnum.AGS_SCOPE_SCORE.getScope());
	}
}

package interpreter;

import java.util.ArrayList;
import java.util.List;

import cop5556fa19.AST.StatLabel;

public class StatLabelRep {
	StatLabel statLabel;
	List<Integer> blockPattern;
	public StatLabelRep(StatLabel statLabel) {
		super();
		this.statLabel = statLabel;
		this.blockPattern = new ArrayList<>();
	}
	@Override
	public String toString() {
		return "StatLabelRep [statLabel=" + statLabel.label.name + ", blockPattern=" + blockPattern + "]";
	}


	
	
}

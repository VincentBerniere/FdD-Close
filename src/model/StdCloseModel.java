package model;

import java.util.HashMap;

public class StdCloseModel implements CloseModel {
	public HashMap<Integer,Line> hashLines;
	
	public StdCloseModel() {
		hashLines = new HashMap<Integer,Line>();
	}
	
	@Override
	public void addLine(int id, Line line) {
		if (!hashLines.keySet().contains(id)) {
			hashLines.put(id, line);
		}
	}

	@Override
	public Line getLine(int i) {
		return hashLines.get(i);
	}
	
	@Override
	public void removeLine(int i) {
		hashLines.remove(i);
	}
	
	@Override
	public HashMap<Integer,Line> getLines() {
		return hashLines;
	}
}

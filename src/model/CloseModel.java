package model;

import java.util.ArrayList;
import java.util.HashMap;

public interface CloseModel {
	
	public void addLine(int id, Line line);
	public Line getLine(int i);
	public void removeLine(int i);
	public HashMap<Integer,Line> getLines();
}

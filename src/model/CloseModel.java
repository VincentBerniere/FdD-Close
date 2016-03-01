package model;

import java.util.ArrayList;

public interface CloseModel {
	
	public void addLine(int id, Line line);
	public Line getLine(int i);
	public void removeLine(int i);
}

package org.warp.picalculator;

import java.util.List;

public interface Function {
	public String getSymbol();

	public int getStepsCount();

	public List<Function> solveOneStep() throws Error;

	public void generateGraphics();

	public void draw(int x, int y);

	public int getWidth();

	public int getHeight();

	public int getLine();
	
	public void setSmall(boolean small);
	
	@Override
	public int hashCode();
	
	@Override
	public boolean equals(Object o);
}

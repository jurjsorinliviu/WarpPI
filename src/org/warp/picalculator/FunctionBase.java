package org.warp.picalculator;

import java.util.List;

public abstract class FunctionBase implements Function {

	@Override
	public abstract String getSymbol();

	@Override
	public abstract int getStepsCount();
	
	@Override
	public abstract List<Function> solveOneStep() throws Error;

	@Override
	public abstract void generateGraphics();

	@Override
	public abstract void draw(int x, int y);

	@Override	
	public abstract int getWidth();

	@Override
	public abstract int getHeight();

	@Override
	public abstract int getLine();

	@Override
	public abstract void setSmall(boolean small);

}

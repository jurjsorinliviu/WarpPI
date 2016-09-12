package org.warp.picalculator;

public interface Function {
	public String getSymbol();

	public Function solve() throws Error;

	public void generateGraphics();

	public void draw(int x, int y);

	public int getWidth();

	public int getHeight();

	public int getLine();
	
	public void setSmall(boolean small);
}

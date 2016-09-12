package org.warp.picalculator;

public class Power extends FunctionTwoValuesBase {

	public Power(FunctionBase value1, FunctionBase value2) {
		super(value1, value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.POWER;
	}

	@Override
	public void generateGraphics() {
		variable1.setSmall(small);
		variable1.generateGraphics();
		
		variable2.setSmall(true);
		variable2.generateGraphics();
		
		height = variable1.getHeight() + variable2.getHeight() - 4;
		line = variable2.getHeight() - 4 + variable1.getLine();
		width = getVariable1().getWidth() + getVariable2().getWidth()+1;
	}

	@Override
	public Number solve() throws NumberFormatException, Error {
		return getVariable1().solve().pow(getVariable2().solve());
	}

	@Override
	public void draw(int x, int y) {
//		glColor3f(0, 127-50+new Random().nextInt(50), 0);
//		glFillRect(x,y,width,height);
//		glColor3f(0, 0, 0);
		
		int dx = 0;
		variable1.draw(dx + x, getHeight() - variable1.getHeight() + y);
		dx += variable1.getWidth();
		variable2.draw(dx + x, y);
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getLine() {
		return line;
	}

	@Override
	public int getWidth() {
		return width;
	}
}

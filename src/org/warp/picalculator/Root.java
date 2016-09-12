package org.warp.picalculator;

import static org.warp.engine.Display.Render.glDrawLine;

import org.nevec.rjm.NumeroAvanzatoVec;

public class Root extends FunctionTwoValuesBase {

	public Root(FunctionBase value1, FunctionBase value2) {
		super(value1, value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.NTH_ROOT;
	}

	@Override
	public void generateGraphics() {
		variable1.setSmall(true);
		variable1.generateGraphics();
		
		variable2.setSmall(small);
		variable2.generateGraphics();
		
		width = 1 + variable1.getWidth() + 2 + variable2.getWidth() + 2;
		height = variable1.getHeight() + variable2.getHeight() - 2;
		line = variable1.getHeight() + variable2.getLine() - 2;
	}

	@Override
	public Number solve() throws NumberFormatException, Error {
		Number exponent = new Number(NumeroAvanzatoVec.ONE);
		exponent = exponent.divide(getVariable1().solve());
		return getVariable2().solve().pow(exponent);
	}

	@Override
	public void draw(int x, int y) {
//		glColor3f(0, 255, 0);
//		glFillRect(x,y,width,height);
//		glColor3f(0, 0, 0);
		
		int w1 = getVariable2().getWidth();
		int h1 = getVariable2().getHeight();
		int w2 = getVariable1().getWidth();
		int h2 = getVariable1().getHeight();
		int height = getHeight();
		int hh = (int) Math.ceil((double) h1 / 2);

		getVariable1().draw(x + 1, y);
		getVariable2().draw(x + 1 + w2 + 2, y + h2 - 2);

		glDrawLine(x + 1 + w2 - 2, y + height - 3, x + 1 + w2, y + height);
		glDrawLine(x + 1 + w2, y + height - 1 - hh, x + 1 + w2, y + height - 1);
		glDrawLine(x + 1 + w2 + 1, y + height - 2 - h1, x + 1 + w2 + 1, y + height - 1 - hh - 1);
		glDrawLine(x + 1 + w2 + 1, y + height - h1 - 2, x + 1 + w2 + 2 + w1 + 1, y + height - h1 - 2);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getLine() {
		return line;
	}
}

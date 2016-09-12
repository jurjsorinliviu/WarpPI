package org.warp.picalculator;

import org.nevec.rjm.Rational;

public class RootSquare extends AnteriorFunctionBase {

	public RootSquare(FunctionBase value) {
		super(value);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.SQUARE_ROOT;
	}
	
	@Override
	public void generateGraphics() {
		variable.setSmall(small);
		variable.generateGraphics();
		
		height = getVariable().getHeight() + 2;
		width = 1 + 4 + getVariable().getWidth() + 1;
		line = getVariable().getLine() + 2;
	}

	@Override
	public Number solve() throws Error {
		try {
			Number result = getVariable().solve();
			result = result.pow(new Number(new Rational(1, 2)));
			return result;
		} catch(NullPointerException ex) {
			throw new Error(Errors.ERROR);
		} catch(NumberFormatException ex) {
			throw new Error(Errors.SYNTAX_ERROR);
		} catch(ArithmeticException ex) {
			throw new Error(Errors.NUMBER_TOO_SMALL);
		}
	}

	@Override
	public void draw(int x, int y) {
//		glColor3f(0, 255, 0);
//		glFillRect(x,y,width,height);
//		glColor3f(0, 0, 0);
		
		Utils.writeSquareRoot(getVariable(), x, y, small);
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

package org.warp.picalculator.math.functions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathematicalSymbols;

public class Root extends FunctionOperator {

	public Root(MathContext root, Function value1, Function value2) {
		super(root, value1, value2);
	}

	@Override
	protected Function NewInstance(MathContext root, Function value1, Function value2) {
		return new Root(root, value1, value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.NTH_ROOT;
	}

	@Override
	public void generateGraphics() {
		parameter1.setSmall(true);
		parameter1.recomputeDimensions();

		parameter2.setSmall(small);
		parameter2.recomputeDimensions();

		width = 1 + parameter1.getWidth() + 2 + parameter2.getWidth() + 2;
		height = parameter1.getHeight() + parameter2.getHeight() - 2;
		line = parameter1.getHeight() + parameter2.getLine() - 2;
	}

	@Override
	protected boolean isSolvable() {
		if (parameter1 instanceof Number & parameter2 instanceof Number) {
			if (mathContext.exactMode == false) {
				return true;
			}
			try {
				Number exponent = new Number(mathContext, BigDecimal.ONE);
				exponent = exponent.divide((Number) parameter1);
				final Number resultVal = ((Number) parameter2).pow(exponent);
				final Number originalVariable = resultVal.pow(new Number(mathContext, 2));
				if (originalVariable.equals(parameter2)) {
					return true;
				}
			} catch (Exception | Error ex) {
				ex.printStackTrace();
			}
		}
		if (parameter1 instanceof Number && ((Number) parameter1).equals(new Number(mathContext, 2))) {
			return true;
		}
		return false;
	}

	@Override
	public ArrayList<Function> solve() throws Error {
		final ArrayList<Function> result = new ArrayList<>();
		if (mathContext.exactMode) {
			if (parameter1 instanceof Number && ((Number) parameter1).equals(new Number(mathContext, 2))) {
				result.add(new RootSquare(mathContext, parameter2));
			} else {
				Number exponent = new Number(mathContext, BigInteger.ONE);
				exponent = exponent.divide((Number) parameter1);
				result.add(((Number) parameter2).pow(exponent));
			}
		} else {
			final Number exp = (Number) parameter1;
			final Number numb = (Number) parameter2;

			result.add(numb.pow(new Number(mathContext, 1).divide(exp)));
		}
		return result;
	}

	@Override
	public void draw(int x, int y, boolean small, int caretPos) {
//		glColor3f(0, 255, 0);
//		glFillRect(x,y,width,height);
//		glColor3f(0, 0, 0);

		final int w1 = getVariable2().getWidth();
		final int h1 = getVariable2().getHeight();
		final int w2 = getParameter1().getWidth();
		final int h2 = getParameter1().getHeight();
		final int height = getHeight();
		final int hh = (int) Math.ceil((double) h1 / 2);

		getParameter1().draw(x + 1, y, null, null);
		getVariable2().draw(x + 1 + w2 + 2, y + h2 - 2, null, null);

		DisplayManager.renderer.glDrawLine(x + 1 + w2 - 2, y + height - 2, x + 1 + w2 - 2, y + height - 2);
		DisplayManager.renderer.glDrawLine(x + 1 + w2 - 1, y + height - 1, x + 1 + w2 - 1, y + height - 1);
		DisplayManager.renderer.glDrawLine(x + 1 + w2 - 0, y + height - 0, x + 1 + w2 - 0, y + height - 0);
		DisplayManager.renderer.glDrawLine(x + 1 + w2, y + height - 1 - hh, x + 1 + w2, y + height - 1);
		DisplayManager.renderer.glDrawLine(x + 1 + w2 + 1, y + height - 2 - h1, x + 1 + w2 + 1, y + height - 1 - hh - 1);
		DisplayManager.renderer.glDrawLine(x + 1 + w2 + 1, y + height - h1 - 2, x + 1 + w2 + 2 + w1 + 1, y + height - h1 - 2);
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

	@Override
	public boolean equals(Object o) {
		if (o instanceof Root) {
			final FunctionOperator f = (FunctionOperator) o;
			return parameter1.equals(f.parameter1) && parameter2.equals(f.parameter2);
		}
		return false;
	}
}

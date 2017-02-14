package org.warp.picalculator.math.functions;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Utils;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.graphicengine.cpu.CPUEngine;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.rules.ExpandRule1;
import org.warp.picalculator.math.rules.NumberRule3;
import org.warp.picalculator.math.rules.NumberRule4;
import org.warp.picalculator.math.rules.NumberRule5;

public class SumSubtraction extends FunctionOperator {

	public SumSubtraction(MathContext root, Function value1, Function value2) {
		super(root, value1, value2);
	}

	@Override
	protected Function NewInstance(MathContext root, Function value1, Function value2) {
		return new SumSubtraction(root, value1, value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.SUM_SUBTRACTION;
	}

	@Override
	protected boolean isSolvable() {
		if (parameter1 instanceof Number & parameter2 instanceof Number) {
			return true;
		}
		if (NumberRule3.compare(this)) {
			return true;
		}
		if (ExpandRule1.compare(this)) {
			return true;
		}
		if (NumberRule4.compare(this)) {
			return true;
		}
		if (NumberRule5.compare(this)) {
			return true;
		}
		return false;
	}

	@Override
	public ArrayList<Function> solve() throws Error {
		if (parameter1 == null || parameter2 == null) {
			throw new Error(Errors.SYNTAX_ERROR);
		}
		ArrayList<Function> result = new ArrayList<>();
		if (NumberRule3.compare(this)) {
			result = NumberRule3.execute(this);
		} else if (ExpandRule1.compare(this)) {
			result = ExpandRule1.execute(this);
		} else if (NumberRule4.compare(this)) {
			result = NumberRule4.execute(this);
		} else if (NumberRule5.compare(this)) {
			result = NumberRule5.execute(this);
		} else if (parameter1.isSimplified() & parameter2.isSimplified()) {
			result.add(((Number) parameter1).add((Number) parameter2));
			result.add(((Number) parameter1).add(((Number) parameter2).multiply(new Number(mathContext, "-1"))));
		}
		return result;
	}

	@Override
	public void generateGraphics() {
		parameter1.setSmall(small);
		parameter1.recomputeDimensions();

		parameter2.setSmall(small);
		parameter2.recomputeDimensions();

		width = calcWidth();
		height = calcHeight();
		line = calcLine();
	}

	@Override
	public void draw(int x, int y, boolean small, int caretPos) {
//		glColor3f(127, 127-50+new Random().nextInt(50), 255);
//		glFillRect(x,y,width,height);
//		glColor3f(0, 0, 0);

		final int ln = getLine();
		int dx = 0;
		parameter1.draw(dx + x, ln - parameter1.getLine() + y, null, null);
		dx += parameter1.getWidth();
		Utils.getFont(small).use(DisplayManager.engine);
		dx += 1;
		DisplayManager.renderer.glDrawStringLeft(dx + x, ln - Utils.getFontHeight(small) / 2 + y, getSymbol());
		dx += Utils.getFont(small).getStringWidth(getSymbol());
		parameter2.draw(dx + x, ln - parameter2.getLine() + y, null, null);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	protected int calcWidth() {
		int dx = 0;
		dx += parameter1.getWidth();
		dx += 1;
		dx += Utils.getFont(small).getStringWidth(getSymbol());
		return dx += parameter2.getWidth();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SumSubtraction) {
			final FunctionOperator f = (FunctionOperator) o;
			return parameter1.equals(f.parameter1) && parameter2.equals(f.parameter2);
		}
		return false;
	}
}

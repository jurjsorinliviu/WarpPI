package org.warp.picalculator.math.functions;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Utils;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.graphicengine.cpu.CPUEngine;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.rules.ExpandRule1;
import org.warp.picalculator.math.rules.ExpandRule5;

public class Negative extends FunctionSingle {

	public Negative(MathContext root, Function value) {
		super(root, value);
	}

	@Override
	public Function NewInstance(MathContext root, Function value) {
		return new Negative(root, value);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.MINUS;
	}

	@Override
	public void recomputeDimensions() {
		variable.setSmall(small);
		variable.recomputeDimensions();

		height = getParameter().getHeight();
		width = Utils.getFont(small).getCharacterWidth() /* Width of - */ + getParameter().getWidth();
		line = getParameter().getLine();
	}

	@Override
	protected boolean isSolvable() {
		if (variable instanceof Number) {
			return true;
		}
		if (ExpandRule1.compare(this)) {
			return true;
		}
		if (ExpandRule5.compare(this)) {
			return true;
		}
		return false;
	}

	@Override
	public ArrayList<Function> solve() throws Error {
		if (variable == null) {
			throw new Error(Errors.SYNTAX_ERROR);
		}
		ArrayList<Function> result = new ArrayList<>();
		if (ExpandRule1.compare(this)) {
			result = ExpandRule1.execute(this);
		} else if (ExpandRule5.compare(this)) {
			result = ExpandRule5.execute(this);
		} else if (variable.isSimplified()) {
			try {
				final Number var = (Number) getParameter();
				result.add(var.multiply(new Number(mathContext, "-1")));
			} catch (final NullPointerException ex) {
				throw new Error(Errors.ERROR);
			} catch (final NumberFormatException ex) {
				throw new Error(Errors.SYNTAX_ERROR);
			} catch (final ArithmeticException ex) {
				throw new Error(Errors.NUMBER_TOO_SMALL);
			}
		} else {
			final ArrayList<Function> l1 = new ArrayList<>();
			if (variable.isSimplified()) {
				l1.add(variable);
			} else {
				l1.addAll(variable.simplify());
			}

			for (final Function f : l1) {
				result.add(new Negative(mathContext, f));
			}
		}
		return result;
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
		if (o instanceof Negative) {
			return ((Negative) o).variable.equals(variable);
		}
		return false;
	}
}

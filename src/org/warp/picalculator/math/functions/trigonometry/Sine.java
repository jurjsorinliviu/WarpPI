package org.warp.picalculator.math.functions.trigonometry;

import java.util.ArrayList;

import org.nevec.rjm.BigDecimalMath;
import org.warp.picalculator.Error;
import org.warp.picalculator.math.AngleMode;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.functions.Number;

public class Sine extends FunctionSingle {

	public Sine(MathContext root, Function value) {
		super(root, value);
	}

	@Override
	public Function NewInstance(MathContext root, Function value) {
		return new Sine(root, value);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.getGraphicRepresentation(MathematicalSymbols.SINE);
	}

	@Override
	protected boolean isSolvable() {
		if (variable instanceof Number) {
			if (mathContext.exactMode == false) {
				return true;
			}
		}
		if (mathContext.angleMode == AngleMode.DEG) {
			final Function[] solvableValues = new Function[] { new Number(mathContext, 0), new Number(mathContext, 30), new Number(mathContext, 90), };
		}
		return false;
	}

	@Override
	public ArrayList<Function> solve() throws Error {
		final ArrayList<Function> results = new ArrayList<>();
		if (variable instanceof Number) {
			if (mathContext.exactMode == false) {
				results.add(new Number(mathContext, BigDecimalMath.sin(((Number) variable).getTerm())));
			}
		}
		return results;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Sine) {
			final FunctionSingle f = (FunctionSingle) o;
			if (variable.equals(f.getParameter())) {
				return true;
			}
		}
		return false;
	}

}

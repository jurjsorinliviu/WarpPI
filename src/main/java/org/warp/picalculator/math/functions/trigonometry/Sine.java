package org.warp.picalculator.math.functions.trigonometry;

import org.nevec.rjm.BigDecimalMath;
import org.warp.picalculator.Error;
import org.warp.picalculator.math.AngleMode;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Number;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Sine extends FunctionSingle {

	public Sine(MathContext root, Function value) {
		super(root, value);
	}

	@SuppressWarnings("unused")
	@Override
	protected boolean isSolvable() {
		if (parameter instanceof Number) {
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
	public ObjectArrayList<Function> solve() throws Error {
		final ObjectArrayList<Function> results = new ObjectArrayList<>();
		if (parameter instanceof Number) {
			if (mathContext.exactMode == false) {
				results.add(new Number(mathContext, BigDecimalMath.sin(((Number) parameter).getTerm())));
			}
		}
		return results;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Sine) {
			final FunctionSingle f = (FunctionSingle) o;
			if (parameter.equals(f.getParameter())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Sine clone() {
		return new Sine(mathContext, parameter);
	}

}

package org.warp.picalculator.math.functions;

import java.math.BigInteger;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionSingle;

public class RootSquare extends FunctionSingle {

	public RootSquare(MathContext root, Function value) {
		super(root, value);
	}

	@Override
	protected boolean isSolvable() {
		if (parameter instanceof Number) {
			if (mathContext.exactMode == false) {
				return true;
			}
			try {
				Number exponent = new Number(mathContext, BigInteger.ONE);
				exponent = exponent.divide(new Number(mathContext, 2));
				final Number resultVal = ((Number) parameter).pow(exponent);
				final Number originalVariable = resultVal.pow(new Number(mathContext, 2));
				if (originalVariable.equals(parameter)) {
					return true;
				}
			} catch (Exception | Error ex) {

			}
		}
		return false;
	}

	@Override
	public ObjectArrayList<Function> solve() throws Error {
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		if (mathContext.exactMode) {
			Number exponent = new Number(mathContext, BigInteger.ONE);
			exponent = exponent.divide(new Number(mathContext, 2));
			result.add(((Number) parameter).pow(exponent));
		} else {
			final Number exp = new Number(mathContext, 2);
			final Number numb = (Number) parameter;

			result.add(numb.pow(new Number(mathContext, 1).divide(exp)));
		}
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof RootSquare) {
			return ((RootSquare) o).getParameter().equals(parameter);
		}
		return false;
	}

	@Override
	public RootSquare clone() {
		return new RootSquare(mathContext, parameter);
	}
}

package org.warp.picalculator.math.functions;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Root extends FunctionOperator {

	public Root(MathContext root, Function value1, Function value2) {
		super(root, value1, value2);
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
	public ObjectArrayList<Function> solve() throws Error {
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
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
	public boolean equals(Object o) {
		if (o instanceof Root) {
			final FunctionOperator f = (FunctionOperator) o;
			return parameter1.equals(f.getParameter1()) && parameter2.equals(f.getParameter2());
		}
		return false;
	}

	@Override
	public Root clone() {
		return new Root(mathContext, parameter1, parameter2);
	}

	@Override
	public ObjectArrayList<Block> toBlock(MathContext context) throws Error {
		// TODO Auto-generated method stub
		throw new Error(Errors.NOT_IMPLEMENTED, "Unknown function " + getClass().getSimpleName());
	}

}

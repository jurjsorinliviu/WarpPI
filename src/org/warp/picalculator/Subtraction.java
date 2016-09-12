package org.warp.picalculator;

public class Subtraction extends FunctionTwoValuesBase {

	public Subtraction(FunctionBase value1, FunctionBase value2) {
		super(value1, value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.SUBTRACTION;
	}

	@Override
	public Number solve() throws Error {
		return getVariable1().solve().add(getVariable2().solve().multiply(new Number("-1")));
	}

}
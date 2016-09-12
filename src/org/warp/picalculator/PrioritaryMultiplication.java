package org.warp.picalculator;

public class PrioritaryMultiplication extends FunctionTwoValuesBase {

	public PrioritaryMultiplication(FunctionBase value1, FunctionBase value2) {
		super(value1, value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.PRIORITARY_MULTIPLICATION;
	}

	@Override
	public Number solve() throws Error {
		return getVariable1().solve().multiply(getVariable2().solve());
	}

	@Override
	public boolean drawSignum() {
		return false;
	}
}
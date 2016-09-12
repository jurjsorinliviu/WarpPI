package org.warp.picalculator;

import java.math.BigInteger;

public class Multiplication extends FunctionTwoValuesBase {

	public Multiplication(FunctionBase value1, FunctionBase value2) {
		super(value1, value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.MULTIPLICATION;
	}

	@Override
	public Number solve() throws Error {
		return getVariable1().solve().multiply(getVariable2().solve());
	}

	@Override
	public boolean drawSignum() {
		Function[] tmpVar = new Function[] { variable1, variable2 };
		boolean[] ok = new boolean[] { false, false };
		for (int val = 0; val < 2; val++) {
			while (!ok[val]) {
				if (tmpVar[val] instanceof Division) {
					ok[0] = true;
					ok[1] = true;
				} else if (tmpVar[val] instanceof Variable) {
					ok[val] = true;
				} else if (tmpVar[val] instanceof Number) {
					if (val == 0) {
						ok[val] = true;
					} else {
						if (!(tmpVar[0] instanceof Number)) {
							ok[val] = true;
						} else {
							if (((Number) tmpVar[val]).term.isBigInteger(false)) { // TODO: prima era tmpVar[0], ma crashava. RICONTROLLARE! La logica potrebbe essere sbagliata
								if (((Number) tmpVar[val]).term.toBigInteger(true).compareTo(new BigInteger("1")) == 0) {
									if (((Number) tmpVar[val]).term.toNumeroAvanzato().getVariableY().count() > 0) {
										ok[val] = true;
									} else {
										break;
									}
								} else {
									break;
								}
							} else {
								ok[val] = true;
							}
						}
					}
				} else if (tmpVar[val] instanceof Power) {
					tmpVar[val] = ((Power) tmpVar[val]).variable1;
				} else if (tmpVar[val] instanceof Root) {
					ok[val] = true;
				} else if (tmpVar[val] instanceof RootSquare) {
					ok[val] = true;
				} else if (tmpVar[val] instanceof Expression) {
					ok[0] = true;
					ok[1] = true;
				} else if (tmpVar[val] instanceof FunctionTwoValuesBase) {
					if (val == 0) {
						tmpVar[val] = ((FunctionTwoValuesBase) tmpVar[val]).variable2;
					} else {
						tmpVar[val] = ((FunctionTwoValuesBase) tmpVar[val]).variable1;
					}
				} else if (tmpVar[val] instanceof AnteriorFunctionBase) {
					tmpVar[val] = ((AnteriorFunctionBase) tmpVar[val]).variable;
				}
			}
		}

		if (ok[0] == true && ok[1] == true) {
			return false;
		} else {
			return true;
		}
	}
}
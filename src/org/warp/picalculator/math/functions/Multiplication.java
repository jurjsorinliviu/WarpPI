package org.warp.picalculator.math.functions;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.Variable;

public class Multiplication extends FunctionTwoValues {

	public Multiplication(Function value1, Function value2) {
		super(value1, value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.MULTIPLICATION;
	}

	@Override
	protected boolean isSolvable() throws Error {
		if (variable1 instanceof Number & variable2 instanceof Number) {
			return true;
		}
		return false;
	}

	@Override
	public List<Function> solveOneStep() throws Error {
		ArrayList<Function> result = new ArrayList<>();
		if (variable1.isSolved() & variable2.isSolved()) {
			result.add(((Number)variable1).multiply((Number)variable2));
		} else {
			List<Function> l1 = new ArrayList<Function>();
			List<Function> l2 = new ArrayList<Function>();
			if (variable1.isSolved()) {
				l1.add(variable1);
			} else {
				l1.addAll(variable1.solveOneStep());
			}
			if (variable2.isSolved()) {
				l2.add(variable2);
			} else {
				l2.addAll(variable2.solveOneStep());
			}

			Function[][] results = Utils.joinFunctionsResults(l1, l2);
			
			for (Function[] f : results) {
				result.add(new Multiplication(f[0], f[1]));
			}
		}
		return result;
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
				} else if (tmpVar[val] instanceof FunctionTwoValues) {
					if (val == 0) {
						tmpVar[val] = ((FunctionTwoValues) tmpVar[val]).variable2;
					} else {
						tmpVar[val] = ((FunctionTwoValues) tmpVar[val]).variable1;
					}
				} else if (tmpVar[val] instanceof AnteriorFunction) {
					tmpVar[val] = ((AnteriorFunction) tmpVar[val]).variable;
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
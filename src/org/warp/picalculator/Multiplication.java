package org.warp.picalculator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Multiplication extends FunctionTwoValuesBase {

	public Multiplication(FunctionBase value1, FunctionBase value2) {
		super(value1, value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.MULTIPLICATION;
	}

	@Override
	public List<Function> solveOneStep() throws Error {
		if (variable1 == null || variable2 == null) {
			throw new Error(Errors.SYNTAX_ERROR);
		}
		ArrayList<Function> result = new ArrayList<>();
		if (stepsCount == 1) {
			result.add(((Number)variable1).multiply((Number)variable2));
		} else {
			List<Function> l1 = new ArrayList<Function>();
			List<Function> l2 = new ArrayList<Function>();
			if (variable1.getStepsCount() >= stepsCount - 1) {
				l1.addAll(variable1.solveOneStep());
			} else {
				l1.add(variable1);
			}
			if (variable2.getStepsCount() >= stepsCount - 1) {
				l2.addAll(variable2.solveOneStep());
			} else {
				l2.add(variable2);
			}
			
			int size1 = l1.size();
			int size2 = l2.size();
			int cur1 = 0;
			int cur2 = 0;
			int total = l1.size()*l2.size();
			Function[][] results = new Function[total][2];
			for (int i = 0; i < total; i++) {
				results[i] = new Function[]{l1.get(cur1), l2.get(cur2)};
				if (cur1 < cur2 && cur2 % size1 == 0) {
					cur2+=1;
				} else if (cur2 < cur1 && cur1 % size2 == 0) {
					cur1+=1;
				}
				if (cur1 >= size1) cur1 = 0;
				if (cur2 >= size1) cur2 = 0;
			}
			for (Function[] f : results) {
				result.add(new Multiplication((FunctionBase)f[0], (FunctionBase)f[1]));
			}
		}
		stepsCount=-1;
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
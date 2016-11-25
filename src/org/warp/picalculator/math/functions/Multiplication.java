package org.warp.picalculator.math.functions;

import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.rules.ExponentRule15;
import org.warp.picalculator.math.rules.NumberRule1;
import org.warp.picalculator.math.rules.NumberRule2;
import org.warp.picalculator.math.rules.NumberRule6;
import org.warp.picalculator.math.rules.SyntaxRule1;

public class Multiplication extends FunctionTwoValues {

	public Multiplication(Function parent, Function value1, Function value2) {
		super(parent, value1, value2);
		if (value1 instanceof Variable && value2 instanceof Variable == false) {
			variable1 = value2;
			variable2 = value1;
		}
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.MULTIPLICATION;
	}

	@Override
	protected boolean isSolvable() {
		if (variable1 instanceof Number & variable2 instanceof Number) {
			return true;
		}
		if (SyntaxRule1.compare(this)) return true;
		if (NumberRule1.compare(this)) return true;
		if (NumberRule2.compare(this)) return true;
		if (NumberRule6.compare(this)) return true;
		if (ExponentRule15.compare(this)) return true;
		return false;
	}

	@Override
	public List<Function> solveOneStep() throws Error {
		List<Function> result = new ArrayList<>();
		if (SyntaxRule1.compare(this)) {
			result = SyntaxRule1.execute(this);
		} else if (NumberRule1.compare(this)) {
			result = NumberRule1.execute(this);
		} else if (NumberRule2.compare(this)) {
			result = NumberRule2.execute(this);
		} else if (NumberRule6.compare(this)) {
			result = NumberRule6.execute(this);
		} else if (ExponentRule15.compare(this)) {
			result = ExponentRule15.execute(this);
		} else if (variable1.isSolved() & variable2.isSolved()) {
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
				result.add(new Multiplication(this.parent, f[0], f[1]));
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
							break;
						}
					}
				} else if (tmpVar[val] instanceof Power) {
					tmpVar[val] = ((Power) tmpVar[val]).variable1;
				} else if (tmpVar[val] instanceof Root) {
					if (val == 0) {
						break;
					}
					ok[val] = true;
				} else if (tmpVar[val] instanceof RootSquare) {
					if (val == 0) {
						break;
					}
					ok[val] = true;
				} else if (tmpVar[val] instanceof Undefined) {
					break;
				} else if (tmpVar[val] instanceof Joke) {
					break;
				} else if (tmpVar[val] instanceof Negative) {
					if (val == 1) {
						break;
					}
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
				} else {
					ok[val] = true;
				}
			}
		}

		if (ok[0] == true && ok[1] == true) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Multiplication) {
			FunctionTwoValues f = (FunctionTwoValues) o;
			if (variable1.equals(f.variable1) && variable2.equals(f.variable2)) {
				return true;
			} else if (variable1.equals(f.variable2) && variable2.equals(f.variable1)) {
				return true;
			}
		}
		return false;
	}
}
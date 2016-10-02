package org.warp.picalculator.math.functions;

import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.MathematicalSymbols;

public class PrioritaryMultiplication extends FunctionTwoValues {

	public PrioritaryMultiplication(Function value1, Function value2) {
		super(value1, value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.PRIORITARY_MULTIPLICATION;
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
		if (variable1 == null || variable2 == null) {
			throw new Error(Errors.SYNTAX_ERROR);
		}
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
				result.add(new PrioritaryMultiplication((Function)f[0], (Function)f[1]));
			}
		}
		return result;
	}

	@Override
	public boolean drawSignum() {
		return false;
	}
}
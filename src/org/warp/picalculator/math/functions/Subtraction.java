package org.warp.picalculator.math.functions;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.nevec.rjm.NumeroAvanzato;
import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.Variable;
import org.warp.picalculator.math.Variables;

public class Subtraction extends FunctionTwoValues {

	public Subtraction(Function value1, Function value2) {
		super(value1, value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.SUBTRACTION;
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
			if (((Number)variable1).term.isBigInteger(false) & ((Number)variable2).term.isBigInteger(false)) {
				if (((Number)variable1).term.toBigInteger(false).compareTo(new BigInteger("2")) == 0 && ((Number)variable2).term.toBigInteger(false).compareTo(new BigInteger("2")) == 0) {
					NumeroAvanzato na = NumeroAvanzato.ONE;
					Variables iy = na.getVariableY();
					List<Variable> newVariableList = iy.getVariablesList();
					newVariableList.add(new Variable('♓', 1, 1));
					iy = new Variables(newVariableList.toArray(new Variable[newVariableList.size()]));
					na = na.setVariableY(iy);
					result.add(new Number(na));
					return result;
				}
			}
			result.add(((Number)variable1).add((Number)variable2).multiply(new Number("-1")));
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
				result.add(new Sum((Function)f[0], (Function)f[1]));
			}
		}
		return result;
	}

}
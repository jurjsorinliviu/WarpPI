package org.warp.picalculator.math.functions.equations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.SolveMethod;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.FunctionTwoValues;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Subtraction;

import com.rits.cloning.Cloner;

public class Equation extends FunctionTwoValues {

	public Equation(Calculator root, Function value1, Function value2) {
		super(root, value1, value2);
	}

	@Override
	protected Function NewInstance(Calculator root, Function value1, Function value2) {
		return new Equation(root, value1, value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.EQUATION;
	}

	@Override
	protected boolean isSolvable() {
		if (variable1 instanceof Number & variable2 instanceof Number) {
			return true;
		}
		return false;
	}

	@Override
	public ArrayList<Function> solve() throws Error {
		if (variable1 == null || variable2 == null) {
			throw new Error(Errors.SYNTAX_ERROR);
		}
		final ArrayList<Function> result = new ArrayList<>();
		if (variable1.isSolved() & variable2.isSolved()) {
			if (((Number) variable2).getTerm().compareTo(new BigDecimal(0)) == 0) {
				result.add(this);
			} else {
				final Equation e = new Equation(root, null, null);
				e.setVariable1(new Subtraction(root, variable1, variable2));
				e.setVariable2(new Number(root, "0"));
				result.add(e);
			}
		}
		return result;
	}

	public List<Function> solve(char variableCharacter) {
		@SuppressWarnings("unused")
		final ArrayList<Equation> e;
		//TODO: WORK IN PROGRESS.
		//TODO: Finire. Fare in modo che risolva i passaggi fino a che non ce ne sono più
		return null;
	}

	//WORK IN PROGRESS
	public ArrayList<Equation> solveStep(char charIncognita) {
		ArrayList<Equation> result = new ArrayList<>();
		result.add(clone());
		for (final SolveMethod t : SolveMethod.techniques) {
			final ArrayList<Equation> newResults = new ArrayList<>();
			final int sz = result.size();
			for (int n = 0; n < sz; n++) {
				newResults.addAll(t.solve(result.get(n)));
			}
			final Set<Equation> hs = new HashSet<>();
			hs.addAll(newResults);
			newResults.clear();
			newResults.addAll(hs);
			result = newResults;
		}
		// TODO: controllare se è a posto
		return result;
	}

	@Override
	public Equation clone() {
		final Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

}
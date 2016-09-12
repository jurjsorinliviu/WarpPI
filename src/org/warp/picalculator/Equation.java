package org.warp.picalculator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.rits.cloning.Cloner;

public class Equation extends FunctionTwoValues {

	public Equation(Function value1, Function value2) {
		super(value1,value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.EQUATION;
	}

	@Override
	public Function solve() throws Error {
		return new Equation(new Subtraction((FunctionBase)variable1.solve(), (FunctionBase)variable2.solve()).solve(), new Number("0"));
	}
	
	public Function solve(char variableCharacter) {
		@SuppressWarnings("unused")
		ArrayList<Equation> e;
		//TODO: WORK IN PROGRESS.
		//TODO: Finire. Fare in modo che risolva i passaggi fino a che non ce ne sono più
		return null;
	}
	
	//WORK IN PROGRESS
	public ArrayList<Equation> solveStep(char charIncognita) {
		ArrayList<Equation> result = new ArrayList<Equation>();
		result.add(this.clone());
		for (SolveMethod t : SolveMethod.techniques) {
			ArrayList<Equation> newResults = new ArrayList<Equation>();
			final int sz = result.size();
			for (int n = 0; n < sz; n++) {
				newResults.addAll(t.solve(result.get(n)));
			}
			Set<Equation> hs = new HashSet<>();
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
		Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}

}
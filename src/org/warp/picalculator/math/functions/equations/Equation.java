package org.warp.picalculator.math.functions.equations;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.SolveMethod;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.FunctionTwoValues;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Subtraction;

import com.rits.cloning.Cloner;

public class Equation extends FunctionTwoValues {

	public Equation(Function parent, Function value1, Function value2) {
		super(parent, value1,value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.EQUATION;
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
			if (((Number)variable2).getTerm().compareTo(new BigInteger("0")) == 0) {
				result.add(this);
			} else {
				Equation e = new Equation(this.parent, null, null);
				e.setVariable1(new Subtraction(e, variable1, variable2));
				e.setVariable2(new Number(e, "0"));
				result.add(e);
			}
		} else {
			List<Function> l1 = new ArrayList<Function>();
			List<Function> l2 = new ArrayList<Function>();
			if (variable1.isSolved() == false) {
				l1.addAll(variable1.solveOneStep());
			} else {
				l1.add(variable1);
			}
			if (variable2.isSolved() == false) {
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
				result.add(new Equation(this.parent, f[0], f[1]));
			}
		}
		return result;
	}
	
	public List<Function> solve(char variableCharacter) {
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
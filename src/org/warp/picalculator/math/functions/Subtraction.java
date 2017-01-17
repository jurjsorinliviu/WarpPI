package org.warp.picalculator.math.functions;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.rules.ExpandRule1;
import org.warp.picalculator.math.rules.ExpandRule5;
import org.warp.picalculator.math.rules.NumberRule3;
import org.warp.picalculator.math.rules.NumberRule5;
import org.warp.picalculator.math.rules.VariableRule1;
import org.warp.picalculator.math.rules.VariableRule2;
import org.warp.picalculator.math.rules.VariableRule3;
import org.warp.picalculator.math.rules.methods.SumMethod1;

public class Subtraction extends FunctionTwoValues {

	public Subtraction(Calculator root, Function value1, Function value2) {
		super(root, value1, value2);
	}
	
	@Override
	protected Function NewInstance(Calculator root, Function value1, Function value2) {
		return new Subtraction(root, value1, value2);
	}
	
	@Override
	public String getSymbol() {
		return MathematicalSymbols.SUBTRACTION;
	}

	@Override
	protected boolean isSolvable() {
		if (variable1 instanceof Number & variable2 instanceof Number) {
			return true;
		}
		if (VariableRule1.compare(this)) return true;
		if (VariableRule2.compare(this)) return true;
		if (VariableRule3.compare(this)) return true;
		if (NumberRule3.compare(this)) return true;
		if (ExpandRule1.compare(this)) return true;
		if (ExpandRule5.compare(this)) return true;
		if (NumberRule5.compare(this)) return true;
		if (SumMethod1.compare(this)) return true;
		return false;
	}
	
	@Override
	public ArrayList<Function> solve() throws Error {
		ArrayList<Function> result = new ArrayList<>();
		if (VariableRule1.compare(this)) {
			result = VariableRule1.execute(this);
		} else if (VariableRule2.compare(this)) {
			result = VariableRule2.execute(this);
		} else if (VariableRule3.compare(this)) {
			result = VariableRule3.execute(this);
		} else if (NumberRule3.compare(this)) {
			result = NumberRule3.execute(this);
		} else if (ExpandRule1.compare(this)) {
			result = ExpandRule1.execute(this);
		} else if (ExpandRule5.compare(this)) {
			result = ExpandRule5.execute(this);
		} else if (NumberRule5.compare(this)) {
			result = NumberRule5.execute(this);
		} else if (SumMethod1.compare(this)) {
			result = SumMethod1.execute(this);
		} else if (variable1.isSolved() & variable2.isSolved()) {
			result.add(((Number)variable1).add(((Number)variable2).multiply(new Number(root, "-1"))));
		}
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Subtraction) {
			FunctionTwoValues f = (FunctionTwoValues) o;
			return variable1.equals(f.variable1) && variable2.equals(f.variable2);
		}
		return false;
	}
}
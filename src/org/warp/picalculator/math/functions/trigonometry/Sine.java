package org.warp.picalculator.math.functions.trigonometry;

import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.functions.AnteriorFunction;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.FunctionTwoValues;
import org.warp.picalculator.math.functions.Sum;

public class Sine extends AnteriorFunction {
	
	public Sine(Function parent, Function value) {
		super(parent, value);
	}
	
	@Override
	public Function NewInstance(Function parent, Function value) {
		return new Sine(parent, value);
	}
	
	@Override
	public String getSymbol() {
		return MathematicalSymbols.getGraphicRepresentation(MathematicalSymbols.SINE);
	}

	@Override
	public ArrayList<Function> solve() throws Error {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isSolvable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Sine) {
			AnteriorFunction f = (AnteriorFunction) o;
			if (variable.equals(f.getVariable())) {
				return true;
			}
		}
		return false;
	}

}

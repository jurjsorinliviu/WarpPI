package org.warp.picalculator.math.functions.trigonometry;

import java.util.ArrayList;

import org.nevec.rjm.BigDecimalMath;
import org.warp.picalculator.Error;
import org.warp.picalculator.math.AngleMode;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.functions.AnteriorFunction;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Number;

public class Sine extends AnteriorFunction {
	
	public Sine(Calculator root, Function value) {
		super(root, value);
	}
	
	@Override
	public Function NewInstance(Calculator root, Function value) {
		return new Sine(root, value);
	}
	
	@Override
	public String getSymbol() {
		return MathematicalSymbols.getGraphicRepresentation(MathematicalSymbols.SINE);
	}

	@Override
	protected boolean isSolvable() {
		if (variable instanceof Number) {
			if (root.exactMode == false) {
				return true;
			}
		}
		if (root.angleMode == AngleMode.DEG) {
			Function[] solvableValues = new Function[]{new Number(root, 0), new Number(root, 30), new Number(root, 90), };
		}
		return false;
	}

	@Override
	public ArrayList<Function> solve() throws Error {
		ArrayList<Function> results = new ArrayList<>();
		if (variable instanceof Number) {
			if (root.exactMode == false) {
				results.add(new Number(root, BigDecimalMath.sin(((Number) variable).getTerm())));
			}
		}
		return results;
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

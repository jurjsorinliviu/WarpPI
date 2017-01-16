package org.warp.picalculator.math.functions.trigonometry;

import java.math.BigInteger;
import java.util.ArrayList;

import org.nevec.rjm.BigDecimalMath;
import org.nevec.rjm.BigIntegerMath;
import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.AngleMode;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.functions.AnteriorFunction;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;

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
	protected boolean isSolvable() {
		if (variable instanceof Number) {
			if (Calculator.exactMode == false) {
				return true;
			}
		}
		if (Calculator.angleMode == AngleMode.DEG) {
			Function[] solvableValues = new Function[]{new Number(null, 0), new Number(null, 30), new Number(null, 90), };
		}
		return false;
	}

	@Override
	public ArrayList<Function> solve() throws Error {
		ArrayList<Function> results = new ArrayList<>();
		if (variable instanceof Number) {
			if (Calculator.exactMode == false) {
				results.add(new Number(parent, BigDecimalMath.sin(((Number) variable).getTerm())));
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

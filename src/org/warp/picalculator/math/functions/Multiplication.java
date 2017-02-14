package org.warp.picalculator.math.functions;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.rules.ExponentRule15;
import org.warp.picalculator.math.rules.ExponentRule16;
import org.warp.picalculator.math.rules.FractionsRule14;
import org.warp.picalculator.math.rules.NumberRule1;
import org.warp.picalculator.math.rules.NumberRule2;
import org.warp.picalculator.math.rules.NumberRule6;
import org.warp.picalculator.math.rules.SyntaxRule1;
import org.warp.picalculator.math.rules.methods.MultiplicationMethod1;

public class Multiplication extends FunctionOperator {

	public Multiplication(MathContext root, Function value1, Function value2) {
		super(root, value1, value2);
		if (value1 instanceof Variable && value2 instanceof Variable == false) {
			parameter1 = value2;
			parameter2 = value1;
		}
	}

	@Override
	protected boolean isSolvable() {
		Function variable1 = getParameter1();
		Function variable2 = getParameter2();
		if (variable1 instanceof Number & variable2 instanceof Number) {
			return true;
		}
		if (SyntaxRule1.compare(this)) {
			return true;
		}
		if (NumberRule1.compare(this)) {
			return true;
		}
		if (NumberRule2.compare(this)) {
			return true;
		}
		if (NumberRule6.compare(this)) {
			return true;
		}
		if (ExponentRule15.compare(this)) {
			return true;
		}
		if (ExponentRule16.compare(this)) {
			return true;
		}
		if (FractionsRule14.compare(this)) {
			return true;
		}
		if (MultiplicationMethod1.compare(this)) {
			return true;
		}
		return false;
	}

	@Override
	public ArrayList<Function> solve() throws Error {
		ArrayList<Function> result = new ArrayList<>();
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
		} else if (ExponentRule16.compare(this)) {
			result = ExponentRule16.execute(this);
		} else if (FractionsRule14.compare(this)) {
			result = FractionsRule14.execute(this);
		} else if (MultiplicationMethod1.compare(this)) {
			result = MultiplicationMethod1.execute(this);
		} else if (parameter1.isSimplified() & parameter2.isSimplified()) {
			result.add(((Number) parameter1).multiply((Number) parameter2));
		}
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Multiplication) {
			final FunctionOperator f = (FunctionOperator) o;
			if (parameter1.equals(f.getParameter1()) && parameter2.equals(f.getParameter2())) {
				return true;
			} else if (parameter1.equals(f.getParameter2()) && parameter2.equals(f.getParameter1())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Multiplication clone() {
		return new Multiplication(mathContext, parameter1, parameter2);
	}
	
	@Override
	public String toString() {
		if (parameter1 != null && parameter2 != null) {
			return parameter1.toString()+"*"+parameter2.toString();
		} else {
			return super.toString();
		}
	}
}
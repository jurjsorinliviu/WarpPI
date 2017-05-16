package org.warp.picalculator.math.functions;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.rules.ExpandRule1;
import org.warp.picalculator.math.rules.ExpandRule5;
import org.warp.picalculator.math.rules.NumberRule3;
import org.warp.picalculator.math.rules.NumberRule5;
import org.warp.picalculator.math.rules.VariableRule1;
import org.warp.picalculator.math.rules.VariableRule2;
import org.warp.picalculator.math.rules.VariableRule3;
import org.warp.picalculator.math.rules.methods.SumMethod1;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Subtraction extends FunctionOperator {

	public Subtraction(MathContext root, Function value1, Function value2) {
		super(root, value1, value2);
	}

	@Override
	protected boolean isSolvable() {
		if (parameter1 instanceof Number & parameter2 instanceof Number) {
			return true;
		}
		if (VariableRule1.compare(this)) {
			return true;
		}
		if (VariableRule2.compare(this)) {
			return true;
		}
		if (VariableRule3.compare(this)) {
			return true;
		}
		if (NumberRule3.compare(this)) {
			return true;
		}
		if (ExpandRule1.compare(this)) {
			return true;
		}
		if (ExpandRule5.compare(this)) {
			return true;
		}
		if (NumberRule5.compare(this)) {
			return true;
		}
		if (SumMethod1.compare(this)) {
			return true;
		}
		return false;
	}

	@Override
	public ObjectArrayList<Function> solve() throws Error {
		ObjectArrayList<Function> result = new ObjectArrayList<>();
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
		} else if (parameter1.isSimplified() & parameter2.isSimplified()) {
			result.add(((Number) parameter1).add(((Number) parameter2).multiply(new Number(mathContext, "-1"))));
		}
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Subtraction) {
			final FunctionOperator f = (FunctionOperator) o;
			return parameter1.equals(f.getParameter1()) && parameter2.equals(f.getParameter2());
		}
		return false;
	}

	@Override
	public Subtraction clone() {
		return new Subtraction(mathContext, parameter1, parameter2);
	}

}
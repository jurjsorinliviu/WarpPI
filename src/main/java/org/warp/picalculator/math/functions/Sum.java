package org.warp.picalculator.math.functions;

import java.math.BigDecimal;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.rules.NumberRule3;
import org.warp.picalculator.math.rules.NumberRule5;
import org.warp.picalculator.math.rules.NumberRule7;
import org.warp.picalculator.math.rules.SyntaxRule2;
import org.warp.picalculator.math.rules.VariableRule1;
import org.warp.picalculator.math.rules.VariableRule2;
import org.warp.picalculator.math.rules.VariableRule3;
import org.warp.picalculator.math.rules.methods.SumMethod1;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Sum extends FunctionOperator {

	public Sum(MathContext root, Function value1, Function value2) {
		super(root, value1, value2);
	}

	@Override
	protected boolean isSolvable() {
		if (parameter1 instanceof Number & parameter2 instanceof Number) {
			return true;
		}
		if (SyntaxRule2.compare(this)) {
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
		if (NumberRule5.compare(this)) {
			return true;
		}
		if (NumberRule7.compare(this)) {
			return true;
		}
		if (SumMethod1.compare(this)) {
			return true;
		}
		return false;
	}

	@Override
	public ObjectArrayList<Function> solve() throws Error {
		if (parameter1 == null || parameter2 == null) {
			throw new Error(Errors.SYNTAX_ERROR);
		}
		ObjectArrayList<Function> result = new ObjectArrayList<>();
		if (SyntaxRule2.compare(this)) {
			result = SyntaxRule2.execute(this);
		} else if (VariableRule1.compare(this)) {
			result = VariableRule1.execute(this);
		} else if (VariableRule2.compare(this)) {
			result = VariableRule2.execute(this);
		} else if (VariableRule3.compare(this)) {
			result = VariableRule3.execute(this);
		} else if (NumberRule3.compare(this)) {
			result = NumberRule3.execute(this);
		} else if (NumberRule5.compare(this)) {
			result = NumberRule5.execute(this);
		} else if (NumberRule7.compare(this)) {
			result = NumberRule7.execute(this);
		} else if (SumMethod1.compare(this)) {
			result = SumMethod1.execute(this);
		} else if (parameter1.isSimplified() & parameter2.isSimplified()) {
			if ((mathContext.getChild().equals(this))) {
				if (((Number) parameter1).term.compareTo(new BigDecimal(2)) == 0 && ((Number) parameter2).term.compareTo(new BigDecimal(2)) == 0) {
					result.add(new Joke(mathContext, Joke.FISH));
					return result;
				} else if (((Number) parameter1).term.compareTo(new BigDecimal(20)) == 0 && ((Number) parameter2).term.compareTo(new BigDecimal(20)) == 0) {
					result.add(new Joke(mathContext, Joke.TORNADO));
					return result;
				} else if (((Number) parameter1).term.compareTo(new BigDecimal(29)) == 0 && ((Number) parameter2).term.compareTo(new BigDecimal(29)) == 0) {
					result.add(new Joke(mathContext, Joke.SHARKNADO));
					return result;
				}
			}
			result.add(((Number) parameter1).add((Number) parameter2));
		}
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Sum) {
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
	public Sum clone() {
		return new Sum(mathContext, parameter1, parameter2);
	}

}

package org.warp.picalculator.math.functions;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.gui.expression.blocks.BlockChar;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.rules.ExpandRule1;
import org.warp.picalculator.math.rules.NumberRule3;
import org.warp.picalculator.math.rules.NumberRule4;
import org.warp.picalculator.math.rules.NumberRule5;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class SumSubtraction extends FunctionOperator {

	public SumSubtraction(MathContext root, Function value1, Function value2) {
		super(root, value1, value2);
	}

	@Override
	protected boolean isSolvable() {
		if (parameter1 instanceof Number & parameter2 instanceof Number) {
			return true;
		}
		if (NumberRule3.compare(this)) {
			return true;
		}
		if (ExpandRule1.compare(this)) {
			return true;
		}
		if (NumberRule4.compare(this)) {
			return true;
		}
		if (NumberRule5.compare(this)) {
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
		if (NumberRule3.compare(this)) {
			result = NumberRule3.execute(this);
		} else if (ExpandRule1.compare(this)) {
			result = ExpandRule1.execute(this);
		} else if (NumberRule4.compare(this)) {
			result = NumberRule4.execute(this);
		} else if (NumberRule5.compare(this)) {
			result = NumberRule5.execute(this);
		} else if (parameter1.isSimplified() & parameter2.isSimplified()) {
			result.add(((Number) parameter1).add((Number) parameter2));
			result.add(((Number) parameter1).add(((Number) parameter2).multiply(new Number(mathContext, "-1"))));
		}
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SumSubtraction) {
			final FunctionOperator f = (FunctionOperator) o;
			return parameter1.equals(f.getParameter1()) && parameter2.equals(f.getParameter2());
		}
		return false;
	}

	@Override
	public SumSubtraction clone() {
		return new SumSubtraction(mathContext, parameter1, parameter2);
	}

	@Override
	public ObjectArrayList<Block> toBlock(MathContext context) throws Error {
		ObjectArrayList<Block> result = new ObjectArrayList<>();
		result.addAll(getParameter1().toBlock(context));
		result.add(new BlockChar(MathematicalSymbols.SUM_SUBTRACTION));
		result.addAll(getParameter2().toBlock(context));
		return result;
	}

}

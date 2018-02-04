package org.warp.picalculator.math.functions;

import org.warp.picalculator.Error;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.gui.expression.blocks.BlockChar;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.MathematicalSymbols;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Subtraction extends FunctionOperator {

	public Subtraction(MathContext root, Function value1, Function value2) {
		super(root, value1, value2);
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

	@Override
	public ObjectArrayList<Block> toBlock(MathContext context) throws Error {
		ObjectArrayList<Block> result = new ObjectArrayList<>();
		result.addAll(getParameter1().toBlock(context));
		result.add(new BlockChar(MathematicalSymbols.SUBTRACTION));
		result.addAll(getParameter2().toBlock(context));
		return result;
	}

}
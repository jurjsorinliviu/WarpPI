package org.warp.picalculator.math.functions;

import org.warp.picalculator.Error;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.gui.expression.blocks.BlockContainer;
import org.warp.picalculator.gui.expression.blocks.BlockSquareRoot;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class RootSquare extends FunctionOperator {

	public RootSquare(MathContext root, Function value2) {
		super(root, new Number(root, 2), value2);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Root) {
			final FunctionOperator f = (FunctionOperator) o;
			return parameter1.equals(f.getParameter1()) && parameter2.equals(f.getParameter2());
		}
		return false;
	}

	@Override
	public RootSquare clone() {
		return new RootSquare(mathContext, parameter2);
	}

	@Override
	public ObjectArrayList<Block> toBlock(MathContext context) throws Error {
		ObjectArrayList<Block> result = new ObjectArrayList<>();
		BlockSquareRoot bsqr = new BlockSquareRoot();
		BlockContainer bsqrc = bsqr.getNumberContainer();
		for (Block b : getParameter2().toBlock(context)) {
			bsqrc.appendBlockUnsafe(b);
		}
		bsqrc.recomputeDimensions();
		bsqr.recomputeDimensions();
		result.add((bsqr));
		return result;
	}

}

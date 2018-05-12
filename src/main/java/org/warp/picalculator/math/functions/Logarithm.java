package org.warp.picalculator.math.functions;

import org.warp.picalculator.Error;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.gui.expression.blocks.BlockContainer;
import org.warp.picalculator.gui.expression.blocks.BlockLogarithm;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Logarithm extends FunctionOperator {

	public Logarithm(MathContext root, Function value1, Function value2) {
		super(root, value1, value2);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Logarithm) {
			final FunctionOperator f = (FunctionOperator) o;
			return parameter1.equals(f.getParameter1()) && parameter2.equals(f.getParameter2());
		}
		return false;
	}

	@Override
	public Logarithm clone() {
		return new Logarithm(mathContext, parameter1, parameter2);
	}

	@Override
	public ObjectArrayList<Block> toBlock(MathContext context) throws Error {
		final ObjectArrayList<Block> result = new ObjectArrayList<>();
		final ObjectArrayList<Block> sub1 = getParameter1().toBlock(context);
		final ObjectArrayList<Block> sub2 = getParameter2().toBlock(context);
		final BlockLogarithm bd = new BlockLogarithm();
		final BlockContainer uc = bd.getBaseContainer();
		final BlockContainer lc = bd.getNumberContainer();
		for (final Block b : sub1) {
			uc.appendBlockUnsafe(b);
		}
		for (final Block b : sub2) {
			lc.appendBlockUnsafe(b);
		}
		uc.recomputeDimensions();
		lc.recomputeDimensions();
		bd.recomputeDimensions();
		result.add(bd);
		return result;
	}

}

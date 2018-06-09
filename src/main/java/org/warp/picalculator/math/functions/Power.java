package org.warp.picalculator.math.functions;

import org.warp.picalculator.Error;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.gui.expression.blocks.BlockContainer;
import org.warp.picalculator.gui.expression.blocks.BlockPower;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Power extends FunctionOperator {

	public Power(MathContext root, Function value1, Function value2) {
		super(root, value1, value2);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Power) {
			final FunctionOperator f = (FunctionOperator) o;
			return parameter1.equals(f.getParameter1()) && parameter2.equals(f.getParameter2());
		}
		return false;
	}

	@Override
	public Power clone() {
		return new Power(mathContext, parameter1, parameter2);
	}

	@Override
	public ObjectArrayList<Block> toBlock(MathContext context) throws Error {
		final ObjectArrayList<Block> result = new ObjectArrayList<>();
		final ObjectArrayList<Block> sub1 = getParameter1().toBlock(context);
		final ObjectArrayList<Block> sub2 = getParameter2().toBlock(context);
		final BlockPower bp = new BlockPower();
		final BlockContainer ec = bp.getExponentContainer();
		result.addAll(sub1);
		for (final Block b : sub2) {
			ec.appendBlockUnsafe(b);
		}
		ec.recomputeDimensions();
		bp.recomputeDimensions();
		result.add(bp);
		return result;
	}
}

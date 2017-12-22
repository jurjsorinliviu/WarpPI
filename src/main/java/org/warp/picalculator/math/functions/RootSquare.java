package org.warp.picalculator.math.functions;

import java.math.BigInteger;

import org.warp.picalculator.Error;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.gui.expression.blocks.BlockContainer;
import org.warp.picalculator.gui.expression.blocks.BlockSquareRoot;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathContext;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class RootSquare extends FunctionSingle {

	public RootSquare(MathContext root, Function value) {
		super(root, value);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof RootSquare) {
			return ((RootSquare) o).getParameter().equals(parameter);
		}
		return false;
	}

	@Override
	public RootSquare clone() {
		return new RootSquare(mathContext, parameter);
	}

	@Override
	public ObjectArrayList<Block> toBlock(MathContext context) throws Error {
		ObjectArrayList<Block> result = new ObjectArrayList<>();
		BlockSquareRoot bsqr = new BlockSquareRoot();
		BlockContainer bsqrc = bsqr.getNumberContainer();
		for (Block b : getParameter().toBlock(context)) {
			bsqrc.appendBlockUnsafe(b);
		}
		bsqrc.recomputeDimensions();
		bsqr.recomputeDimensions();
		result.add((bsqr));
		return result;
	}
}

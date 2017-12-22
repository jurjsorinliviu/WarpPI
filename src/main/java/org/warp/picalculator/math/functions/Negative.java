package org.warp.picalculator.math.functions;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.gui.expression.blocks.BlockChar;
import org.warp.picalculator.gui.expression.blocks.BlockParenthesis;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.rules.ExpandRule1;
import org.warp.picalculator.math.rules.ExpandRule5;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Negative extends FunctionSingle {

	public Negative(MathContext root, Function value) {
		super(root, value);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Negative) {
			return ((Negative) o).getParameter().equals(parameter);
		}
		return false;
	}

	@Override
	public Negative clone() {
		return new Negative(mathContext, parameter);
	}

	@Override
	public ObjectArrayList<Block> toBlock(MathContext context) throws Error {
		ObjectArrayList<Block> blocks = new ObjectArrayList<Block>();
		blocks.add(new BlockChar(MathematicalSymbols.MINUS));
		if (new Expression(context, getParameter()).parenthesisNeeded()) {
			BlockParenthesis par = new BlockParenthesis();
			ObjectArrayList<Block> parBlocks = getParameter().toBlock(context);
			for (Block b : parBlocks) {
				par.getNumberContainer().appendBlockUnsafe(b); // Skips recomputeDimension
			}
			par.recomputeDimensions(); // Recompute dimensions after appendBlockUnsafe
			blocks.add(par);
		} else {
			blocks.addAll(getParameter().toBlock(context));
		}
		return blocks;
		// throw new Error(Errors.NOT_IMPLEMENTED, "Unknown function " + getClass().getSimpleName());
	}
}

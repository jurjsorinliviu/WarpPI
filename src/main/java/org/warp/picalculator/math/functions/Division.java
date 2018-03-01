package org.warp.picalculator.math.functions;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedList;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.gui.expression.blocks.BlockContainer;
import org.warp.picalculator.gui.expression.blocks.BlockDivision;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.rules.methods.DivisionRule1;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Division extends FunctionOperator {

	public Division(MathContext root, Function value1, Function value2) {
		super(root, value1, value2);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Division) {
			final FunctionOperator f = (FunctionOperator) o;
			return getParameter1().equals(f.getParameter1()) && getParameter2().equals(f.getParameter2());
		}
		return false;
	}

	@Override
	public FunctionOperator clone() {
		return new Division(getMathContext(), getParameter1(), getParameter2());
	}

	@Override
	public String toString() {
		return "(" + getParameter1() + ")/(" + getParameter2() + ")";
	}

	@Override
	public ObjectArrayList<Block> toBlock(MathContext context) throws Error {
		ObjectArrayList<Block> result = new ObjectArrayList<>();
		ObjectArrayList<Block> sub1 = getParameter1().toBlock(context);
		ObjectArrayList<Block> sub2 = getParameter2().toBlock(context);
		BlockDivision bd = new BlockDivision();
		BlockContainer uc = bd.getUpperContainer();
		BlockContainer lc = bd.getLowerContainer();
		for (Block b : sub1) {
			uc.appendBlockUnsafe(b);
		}
		for (Block b : sub2) {
			lc.appendBlockUnsafe(b);
		}
		uc.recomputeDimensions();
		lc.recomputeDimensions();
		bd.recomputeDimensions();
		result.add(bd);
		return result;
	}
}
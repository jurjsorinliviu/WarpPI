package org.warp.picalculator.math.functions;

import org.warp.picalculator.Error;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.gui.expression.blocks.BlockChar;
import org.warp.picalculator.gui.expression.blocks.BlockParenthesis;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.MathematicalSymbols;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Multiplication extends FunctionOperator {

	public Multiplication(MathContext root, Function value1, Function value2) {
		super(root, value1, value2);
		/*if (value1 instanceof Variable && value2 instanceof Variable == false) {
			parameter1 = value2;
			parameter2 = value1;
		}*/
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Multiplication) {
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
	public Multiplication clone() {
		return new Multiplication(mathContext, parameter1, parameter2);
	}

	@Override
	public ObjectArrayList<Block> toBlock(MathContext context) throws Error {
		final ObjectArrayList<Block> result = new ObjectArrayList<>();
		final Function par1 = getParameter1();
		final Function par2 = getParameter2();
		final ObjectArrayList<Block> sub1 = par1.toBlock(context);
		final ObjectArrayList<Block> sub2 = par2.toBlock(context);
		final Block nearLeft = sub1.get(sub1.size() - 1);
		final Block nearRight = sub2.get(0);

		if (par1 instanceof Number && ((Number) par1).equals(new Number(context, -1))) {
			result.add(new BlockChar(MathematicalSymbols.MINUS));
			if (new Expression(context, par2).parenthesisNeeded()) {
				final ObjectArrayList<Block> parBlocks = par2.toBlock(context);
				final BlockParenthesis par = new BlockParenthesis(parBlocks);
				result.add(par);
			} else {
				result.addAll(sub2);
			}
			return result;
		} else {
			if (new Expression(context, par1).parenthesisNeeded()) {
				final ObjectArrayList<Block> parBlocks = par1.toBlock(context);
				final BlockParenthesis par = new BlockParenthesis(parBlocks);
				result.add(par);
			} else {
				result.addAll(sub1);
			}
			if ((nearLeft instanceof BlockChar && nearRight instanceof BlockChar) && !(par2 instanceof Negative) && !(par1 instanceof Number && par2 instanceof Number)) {

			} else {
				result.add(new BlockChar(MathematicalSymbols.MULTIPLICATION));
			}
			if (new Expression(context, par2).parenthesisNeeded()) {
				final ObjectArrayList<Block> parBlocks = par2.toBlock(context);
				final BlockParenthesis par = new BlockParenthesis(parBlocks);
				result.add(par);
			} else {
				result.addAll(sub2);
			}
			return result;
		}
	}

	public boolean isNegative() {
		return parameter1.equals(new Number(getMathContext(), -1)) || parameter2.equals(new Number(getMathContext(), -1));
	}

	public Function toPositive() {
		if (parameter1.equals(new Number(getMathContext(), -1))) {
			return parameter2;
		} else if (parameter2.equals(new Number(getMathContext(), -1))) {
			return parameter2;
		} else {
			return null;
		}
	}

	public static Multiplication newNegative(MathContext context, Function value2) {
		return new Multiplication(context, new Number(context, -1), value2);
	}
}
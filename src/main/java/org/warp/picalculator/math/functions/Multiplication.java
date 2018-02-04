package org.warp.picalculator.math.functions;

import org.warp.picalculator.Error;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.gui.expression.blocks.BlockChar;
import org.warp.picalculator.gui.expression.blocks.BlockParenthesis;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.rules.FractionsRule14;
import org.warp.picalculator.math.rules.NumberRule1;
import org.warp.picalculator.math.rules.NumberRule2;
import org.warp.picalculator.math.rules.methods.MultiplicationMethod1;

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
		ObjectArrayList<Block> result = new ObjectArrayList<>();
		Function par1 = getParameter1();
		Function par2 = getParameter2();
		ObjectArrayList<Block> sub1 = par1.toBlock(context);
		ObjectArrayList<Block> sub2 = par2.toBlock(context);
		Block nearLeft = sub1.get(sub1.size() - 1);
		Block nearRight = sub2.get(0);

		if (par1 instanceof Number && ((Number) par1).equals(new Number(context, -1))) {
			result.add(new BlockChar(MathematicalSymbols.MINUS));
			if (new Expression(context, par2).parenthesisNeeded()) {
				ObjectArrayList<Block> parBlocks = par2.toBlock(context);
				BlockParenthesis par = new BlockParenthesis(parBlocks);
				result.add(par);
			} else {
				result.addAll(sub2);
			}
			return result;
		} else {
			if (new Expression(context, par1).parenthesisNeeded()) {
				ObjectArrayList<Block> parBlocks = par1.toBlock(context);
				BlockParenthesis par = new BlockParenthesis(parBlocks);
				result.add(par);
			} else {
				result.addAll(sub1);
			}
			if ((nearLeft instanceof BlockChar && nearRight instanceof BlockChar)
					&& !(par2 instanceof Negative)
					&& !(par1 instanceof Number && par2 instanceof Number)
					) {

			} else {
				result.add(new BlockChar(MathematicalSymbols.MULTIPLICATION));
			}
			if (new Expression(context, par2).parenthesisNeeded()) {
				ObjectArrayList<Block> parBlocks = par2.toBlock(context);
				BlockParenthesis par = new BlockParenthesis(parBlocks);
				result.add(par);
			} else {
				result.addAll(sub2);
			}
			return result;
		}
	}
}
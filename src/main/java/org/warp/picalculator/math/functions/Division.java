package org.warp.picalculator.math.functions;

import java.math.BigInteger;
import java.util.LinkedList;

import org.nevec.rjm.BigIntegerMath;
import org.warp.picalculator.Error;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.gui.expression.blocks.BlockChar;
import org.warp.picalculator.gui.expression.blocks.BlockContainer;
import org.warp.picalculator.gui.expression.blocks.BlockDivision;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.rules.FractionsRule1;
import org.warp.picalculator.math.rules.FractionsRule11;
import org.warp.picalculator.math.rules.FractionsRule12;
import org.warp.picalculator.math.rules.FractionsRule2;
import org.warp.picalculator.math.rules.FractionsRule3;
import org.warp.picalculator.math.rules.UndefinedRule2;
import org.warp.picalculator.math.rules.methods.DivisionRule1;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Division extends FunctionOperator {

	public Division(MathContext root, Function value1, Function value2) {
		super(root, value1, value2);
	}

	@Override
	protected boolean isSolvable() {
		final Function variable1 = getParameter1();
		final Function variable2 = getParameter2();
		if (FractionsRule1.compare(this)) {
			return true;
		}
		if (FractionsRule2.compare(this)) {
			return true;
		}
		if (FractionsRule3.compare(this)) {
			return true;
		}
		if (FractionsRule11.compare(this)) {
			return true;
		}
		if (FractionsRule12.compare(this)) {
			return true;
		}
		if (UndefinedRule2.compare(this)) {
			return true;
		}
		if (DivisionRule1.compare(this)) {
			return true;
		}
		if (variable1 instanceof Number && variable2 instanceof Number) {
			if (getMathContext().exactMode) {
				try {
					if (((Number) variable1).isInteger() && ((Number) variable2).isInteger()) {
						LinkedList<BigInteger> factors1 = ((Number) variable1).getFactors();
						LinkedList<BigInteger> factors2 = ((Number) variable2).getFactors();
						return factors1.retainAll(factors2) /* True If something changed in the factors list by keeping only the intersection of the two factor lists */ && factors1.size() > 0 /* true if there is at least one common factor */;
					} else if (((Number) variable1).divide((Number) variable2).isInteger()) {
						return true;
					} else {
						return false;
					}
				} catch (final Error e) {
					return false;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public ObjectArrayList<Function> solve() throws Error {
		final Function variable1 = getParameter1();
		final Function variable2 = getParameter2();
		ObjectArrayList<Function> result = new ObjectArrayList<>();
		if (FractionsRule1.compare(this)) {
			result = FractionsRule1.execute(this);
		} else if (FractionsRule2.compare(this)) {
			result = FractionsRule2.execute(this);
		} else if (FractionsRule3.compare(this)) {
			result = FractionsRule3.execute(this);
		} else if (FractionsRule11.compare(this)) {
			result = FractionsRule11.execute(this);
		} else if (FractionsRule12.compare(this)) {
			result = FractionsRule12.execute(this);
		} else if (UndefinedRule2.compare(this)) {
			result = UndefinedRule2.execute(this);
		} else if (DivisionRule1.compare(this)) {
			result = DivisionRule1.execute(this);
		} else if (variable1 instanceof Number && variable2 instanceof Number) {
			if (getMathContext().exactMode && (((Number) variable1).isInteger() && ((Number) variable2).isInteger())) {
				LinkedList<BigInteger> factors1 = ((Number) variable1).getFactors();
				LinkedList<BigInteger> factors2 = ((Number) variable2).getFactors();
				if(factors1.retainAll(factors2)) { //True If something changed in the factors list by keeping only the intersection of the two factor lists.
					BigInteger nmb1 = ((Number) this.getParameter1()).term.toBigIntegerExact();
					BigInteger nmb2 = ((Number) this.getParameter2()).term.toBigIntegerExact();
					for (BigInteger i : factors1) {
						nmb1 = nmb1.divide(i);
						nmb2 = nmb2.divide(i);
					}
					result.add(new Division(mathContext, new Number(mathContext, nmb1), new Number(mathContext, nmb2)));
				}
			} else {
				result.add(((Number) variable1).divide((Number) variable2));
			}
		}
		return result;
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
package org.warp.picalculator.math.functions;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.rules.ExpandRule1;
import org.warp.picalculator.math.rules.ExpandRule5;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Negative extends FunctionSingle {

	public Negative(MathContext root, Function value) {
		super(root, value);
	}

	@Override
	protected boolean isSolvable() {
		if (parameter instanceof Number) {
			return true;
		}
		if (ExpandRule1.compare(this)) {
			return true;
		}
		if (ExpandRule5.compare(this)) {
			return true;
		}
		return false;
	}

	@Override
	public ObjectArrayList<Function> solve() throws Error {
		if (parameter == null) {
			throw new Error(Errors.SYNTAX_ERROR);
		}
		ObjectArrayList<Function> result = new ObjectArrayList<>();
		if (ExpandRule1.compare(this)) {
			result = ExpandRule1.execute(this);
		} else if (ExpandRule5.compare(this)) {
			result = ExpandRule5.execute(this);
		} else if (parameter.isSimplified()) {
			try {
				final Number var = (Number) getParameter();
				result.add(var.multiply(new Number(mathContext, "-1")));
			} catch (final NullPointerException ex) {
				throw new Error(Errors.ERROR);
			} catch (final NumberFormatException ex) {
				throw new Error(Errors.SYNTAX_ERROR);
			} catch (final ArithmeticException ex) {
				throw new Error(Errors.NUMBER_TOO_SMALL);
			}
		} else {
			final ObjectArrayList<Function> l1 = new ObjectArrayList<>();
			if (parameter.isSimplified()) {
				l1.add(parameter);
			} else {
				l1.addAll(parameter.simplify());
			}

			for (final Function f : l1) {
				result.add(new Negative(mathContext, f));
			}
		}
		return result;
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
		// TODO Auto-generated method stub
		throw new Error(Errors.NOT_IMPLEMENTED, "Unknown function " + getClass().getSimpleName());
	}
}

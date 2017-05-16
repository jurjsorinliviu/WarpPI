package org.warp.picalculator.math.rules;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Negative;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.functions.SumSubtraction;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Expand rule<br>
 * <b>-(+a+b) = -a-b</b><br>
 * <b>-(+a-b) = -a+b</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class ExpandRule1 {

	public static boolean compare(Function f) {
		if (f instanceof Negative) {
			final Negative fnc = (Negative) f;
			if (fnc.getParameter() instanceof Expression) {
				final Expression expr = (Expression) fnc.getParameter();
				if (expr.getParametersLength() == 1) {
					if (expr.getParameter(0) instanceof Sum) {
						return true;
					} else if (expr.getParameter(0) instanceof Subtraction) {
						return true;
					} else if (expr.getParameter(0) instanceof SumSubtraction) {
						return true;
					}
				}
			}
		} else if (f instanceof Subtraction || f instanceof SumSubtraction) {
			final FunctionOperator fnc = (FunctionOperator) f;
			if (fnc.getParameter2() instanceof Expression) {
				final Expression expr = (Expression) fnc.getParameter2();
				if (expr.getParametersLength() == 1) {
					if (expr.getParameter(0) instanceof Sum) {
						return true;
					} else if (expr.getParameter(0) instanceof Subtraction) {
						return true;
					} else if (expr.getParameter(0) instanceof SumSubtraction) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(Function f) throws Error {
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		final MathContext root = f.getMathContext();

		Expression expr = null;
		int fromSubtraction = 0;
		FunctionOperator subtraction = null;
		if (f instanceof Negative) {
			expr = ((Expression) ((Negative) f).getParameter());
		} else if (f instanceof Subtraction || f instanceof SumSubtraction) {
			expr = ((Expression) ((FunctionOperator) f).getParameter2());
			if (f instanceof Subtraction) {
				fromSubtraction = 1;
			} else {
				fromSubtraction = 2;
			}
		}

		if (f instanceof SumSubtraction) {

		}

		final Function fnc = expr.getParameter(0);
		if (fnc instanceof Sum) {
			final Function a = ((Sum) fnc).getParameter1();
			final Function b = ((Sum) fnc).getParameter2();
			final Subtraction fnc2 = new Subtraction(root, new Negative(root, a), b);
			if (fromSubtraction > 0) {
				subtraction = new Subtraction(root, ((FunctionOperator) f).getParameter1(), fnc2);
				result.add(subtraction);
			} else {
				result.add(fnc2);
			}
		} else if (fnc instanceof Subtraction) {
			final Function a = ((Subtraction) fnc).getParameter1();
			final Function b = ((Subtraction) fnc).getParameter2();
			final Sum fnc2 = new Sum(root, new Negative(root, a), b);
			if (fromSubtraction > 0) {
				subtraction = new Subtraction(root, ((FunctionOperator) f).getParameter1(), fnc2);
				result.add(subtraction);
			} else {
				result.add(fnc2);
			}
		} else if (fnc instanceof SumSubtraction) {
			final Function a = ((SumSubtraction) fnc).getParameter1();
			final Function b = ((SumSubtraction) fnc).getParameter2();
			final Sum fnc2 = new Sum(root, new Negative(root, a), b);
			final Subtraction fnc3 = new Subtraction(root, new Negative(root, a), b);
			if (fromSubtraction > 0) {
				subtraction = new SumSubtraction(root, ((FunctionOperator) f).getParameter1(), fnc2);
				result.add(subtraction);
				subtraction = new SumSubtraction(root, ((FunctionOperator) f).getParameter1(), fnc3);
				result.add(subtraction);
				result.add(subtraction);
			} else {
				result.add(fnc2);
				result.add(fnc2);
			}
		}
		return result;
	}

}

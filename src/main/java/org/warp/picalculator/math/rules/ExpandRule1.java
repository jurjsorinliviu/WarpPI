package org.warp.picalculator.math.rules;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
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
		if (f instanceof Multiplication) {
			final Multiplication fnc = (Multiplication) f;
			if (fnc.getParameter1().equals(new Number(fnc.getMathContext(), -1))) {
				final Function expr = fnc.getParameter2();
				if (expr instanceof Sum) {
					return true;
				} else if (expr instanceof Subtraction) {
					return true;
				} else if (expr instanceof SumSubtraction) {
					return true;
				}
			}
		} else if (f instanceof Subtraction || f instanceof SumSubtraction) {
			final FunctionOperator fnc = (FunctionOperator) f;
			final Function expr = fnc.getParameter2();
			if (expr instanceof Sum) {
				return true;
			} else if (expr instanceof Subtraction) {
				return true;
			} else if (expr instanceof SumSubtraction) {
				return true;
			}
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(Function f) throws Error {
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		final MathContext root = f.getMathContext();

		Function expr = null;
		int fromSubtraction = 0;
		FunctionOperator subtraction = null;
		if (f instanceof Multiplication) {
			expr = ((Multiplication) f).getParameter2();
		} else if (f instanceof Subtraction || f instanceof SumSubtraction) {
			expr = ((FunctionOperator) f).getParameter2();
			if (f instanceof Subtraction) {
				fromSubtraction = 1;
			} else {
				fromSubtraction = 2;
			}
		}

		if (f instanceof SumSubtraction) {

		}

		final Function fnc = expr;
		if (fnc instanceof Sum) {
			final Function a = ((Sum) fnc).getParameter1();
			final Function b = ((Sum) fnc).getParameter2();
			final Subtraction fnc2 = new Subtraction(root, new Multiplication(root, new Number(root, -1), a), b);
			if (fromSubtraction > 0) {
				subtraction = new Subtraction(root, ((FunctionOperator) f).getParameter1(), fnc2);
				result.add(subtraction);
			} else {
				result.add(fnc2);
			}
		} else if (fnc instanceof Subtraction) {
			final Function a = ((Subtraction) fnc).getParameter1();
			final Function b = ((Subtraction) fnc).getParameter2();
			final Sum fnc2 = new Sum(root, new Multiplication(root, new Number(root, -1), a), b);
			if (fromSubtraction > 0) {
				subtraction = new Subtraction(root, ((FunctionOperator) f).getParameter1(), fnc2);
				result.add(subtraction);
			} else {
				result.add(fnc2);
			}
		} else if (fnc instanceof SumSubtraction) {
			final Function a = ((SumSubtraction) fnc).getParameter1();
			final Function b = ((SumSubtraction) fnc).getParameter2();
			final Sum fnc2 = new Sum(root, new Multiplication(root, new Number(root, -1), a), b);
			final Subtraction fnc3 = new Subtraction(root, new Multiplication(root, new Number(root, -1), a), b);
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

package org.warp.picalculator.math.rules;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
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
 * <b>a(b+c) = ab+ac</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class ExpandRule2 {

	public static boolean compare(Function f) {
		if (f instanceof Multiplication) {
			final Multiplication fnc = (Multiplication) f;
			if (fnc.getParameter1() instanceof Sum) {
				return true;
			} else if (fnc.getParameter2() instanceof Sum) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(Function f) throws Error {
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		final MathContext root = f.getMathContext();

		final Multiplication fnc = (Multiplication) f;
		final Sum sum;
		final Function a;
		if (fnc.getParameter1() instanceof Sum) {
			sum = (Sum) fnc.getParameter1();
			a = fnc.getParameter2();
		} else if (fnc.getParameter2() instanceof Sum) {
			sum = (Sum) fnc.getParameter2();
			a = fnc.getParameter1();
		} else {
			throw new Error(Errors.UNBALANCED_STACK);
		}

		final Function b = sum.getParameter1();
		final Function c = sum.getParameter2();
		final Multiplication ab = new Multiplication(root, a, b);
		final Multiplication ac = new Multiplication(root, a, c);
		result.add(new Sum(root, ab, ac));
		return result;
	}

}

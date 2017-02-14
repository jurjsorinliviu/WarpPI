package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Negative;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.functions.SumSubtraction;

/**
 * Number rule<br>
 * <b>a - a = 0</b><br>
 * <b>-a + a = 0</b><br>
 * <b>a ± a = {0, 2a}</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class NumberRule3 {

	public static boolean compare(Function f) {
		if (f instanceof Subtraction) {
			final Subtraction sub = (Subtraction) f;
			if (sub.getParameter1().equals(sub.getParameter2())) {
				return true;
			}
		} else if (f instanceof Sum) {
			final Sum sub = (Sum) f;
			if (sub.getParameter1() instanceof Negative) {
				final Negative neg = (Negative) sub.getParameter1();
				if (neg.getParameter().equals(sub.getParameter2())) {
					return true;
				}
			}
		} else if (f instanceof SumSubtraction) {
			final SumSubtraction sub = (SumSubtraction) f;
			if (sub.getParameter1().equals(sub.getParameter2())) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		final MathContext root = f.getMathContext();
		final ArrayList<Function> result = new ArrayList<>();
		if (f instanceof SumSubtraction) {
			final Multiplication mul = new Multiplication(root, new Number(root, 2), f);
			result.add(mul);
		}
		result.add(new Number(root, 0));
		return result;
	}

}

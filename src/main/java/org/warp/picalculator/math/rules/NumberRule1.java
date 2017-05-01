package org.warp.picalculator.math.rules;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;

/**
 * Number rule<br>
 * <b>a * 0 = 0</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class NumberRule1 {

	public static boolean compare(Function f) {
		final MathContext root = f.getMathContext();
		final Multiplication mult = (Multiplication) f;
		if (mult.getParameter1() instanceof Number) {
			final Number numb = (Number) mult.getParameter1();
			if (numb.equals(new Number(root, 0))) {
				return true;
			}
		}
		if (mult.getParameter2() instanceof Number) {
			final Number numb = (Number) mult.getParameter2();
			if (numb.equals(new Number(root, 0))) {
				return true;
			}
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(Function f) throws Error {
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		result.add(new Number(f.getMathContext(), "0"));
		return result;
	}

}

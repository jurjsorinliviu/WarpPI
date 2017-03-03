package org.warp.picalculator.math.rules;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.functions.SumSubtraction;

/**
 * Number rule<br>
 * <b>a ± b = {a+b, a-b}</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class NumberRule4 {

	public static boolean compare(Function f) {
		if (f instanceof SumSubtraction) {
			return true;
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(Function f) throws Error {
		final MathContext root = f.getMathContext();
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		final SumSubtraction ss = (SumSubtraction) f;
		result.add(new Sum(root, ss.getParameter1(), ss.getParameter2()));
		result.add(new Subtraction(root, ss.getParameter1(), ss.getParameter2()));
		return result;
	}

}

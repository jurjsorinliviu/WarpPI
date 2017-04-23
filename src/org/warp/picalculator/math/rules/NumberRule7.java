package org.warp.picalculator.math.rules;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Sum;

/**
 * Number rule<br>
 * <b>a + a = 2a</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class NumberRule7 {

	public static boolean compare(Sum f) {
		return f.getParameter1().equals(f.getParameter2());
	}

	public static ObjectArrayList<Function> execute(Sum f) throws Error {
		final MathContext root = f.getMathContext();
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		final Multiplication mult = new Multiplication(root, new Number(root, 2), f.getParameter1());
		result.add(mult);
		return result;
	}

}

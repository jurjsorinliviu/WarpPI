package org.warp.picalculator.math.rules;

import java.util.ArrayList;

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

	public static ArrayList<Function> execute(Sum f) throws Error {
		final MathContext root = f.getMathContext();
		final ArrayList<Function> result = new ArrayList<>();
		final Multiplication mult = new Multiplication(root, new Number(root, 2), f.getParameter1());
		result.add(mult);
		return result;
	}

}

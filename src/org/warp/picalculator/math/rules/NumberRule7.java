package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Sum;

/**
 * Number rule<br>
 * <b>a + a = 2a</b>
 * @author Andrea Cavalli
 *
 */
public class NumberRule7 {

	public static boolean compare(Sum f) {
		return f.getVariable1().equals(f.getVariable2());
	}

	public static ArrayList<Function> execute(Sum f) throws Error {
		ArrayList<Function> result = new ArrayList<>();
		Multiplication mult = new Multiplication(f.getParent(), null, null);
		mult.setVariable1(new Number(null, 2));
		mult.setVariable2(f.getVariable1());
		result.add(mult);
		return result;
	}

}

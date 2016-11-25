package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.functions.SumSubtraction;

/**
 * Number rule<br>
 * <b>a ± b = {a+b, a-b}</b>
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

	public static ArrayList<Function> execute(Function f) throws Error {
		ArrayList<Function> result = new ArrayList<>();
		SumSubtraction ss = (SumSubtraction) f;
		result.add(new Sum(f.getParent(), ss.getVariable1(), ss.getVariable2()));
		result.add(new Subtraction(f.getParent(), ss.getVariable1(), ss.getVariable2()));
		return result;
	}

}

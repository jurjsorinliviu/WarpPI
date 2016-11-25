package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.FunctionTwoValues;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Sum;

/**
 * Number rule<br>
 * <b>a + 0 = a</b><br>
 * <b>0 + a = a</b><br>
 * <b>a - 0 = a</b><br>
 * <b>0 - a = a</b><br>
 * <b>a ± 0 = a</b><br>
 * <b>0 ± a = a</b>
 * @author Andrea Cavalli
 *
 */
public class NumberRule5 {

	public static boolean compare(Function f) {
		FunctionTwoValues fnc = (FunctionTwoValues) f;
		if (fnc.getVariable1().equals(new Number(null, 0)) || fnc.getVariable2().equals(new Number(null, 0))) {
			return true;
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		ArrayList<Function> result = new ArrayList<>();
		FunctionTwoValues fnc = (FunctionTwoValues) f;
		Function a = fnc.getVariable1();
		if (a.equals(new Number(null, 0))) {
			a = fnc.getVariable2();
		}
		result.add(a.setParent(f.getParent()));
		return result;
	}

}

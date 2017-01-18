package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.FunctionTwoValues;
import org.warp.picalculator.math.functions.Number;

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
		Calculator root = f.getRoot();
		FunctionTwoValues fnc = (FunctionTwoValues) f;
		if (fnc.getVariable1().equals(new Number(root, 0)) || fnc.getVariable2().equals(new Number(root, 0))) {
			return true;
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		Calculator root = f.getRoot();
		ArrayList<Function> result = new ArrayList<>();
		FunctionTwoValues fnc = (FunctionTwoValues) f;
		Function a = fnc.getVariable1();
		if (a.equals(new Number(root, 0))) {
			a = fnc.getVariable2();
		}
		result.add(a);
		return result;
	}

}

package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Power;
import org.warp.picalculator.math.functions.Number;

/**
 * Exponent rule<br>
 * <b>a^1=a</b>
 * @author Andrea Cavalli
 *
 */
public class ExponentRule2 {

	public static boolean compare(Function f) {
		Power fnc = (Power) f;
		if (fnc.getVariable2().equals(new Number(null, 1))) {
			return true;
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		ArrayList<Function> result = new ArrayList<>();
		result.add(((Power)f).getVariable1().setParent(f.getParent()));
		return result;
	}

}

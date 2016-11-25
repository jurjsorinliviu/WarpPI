package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Power;
import org.warp.picalculator.math.functions.Undefined;
import org.warp.picalculator.math.functions.Number;

/**
 * Undefined rule<br>
 * <b>0^0=undefined</b>
 * @author Andrea Cavalli
 *
 */
public class UndefinedRule1 {

	public static boolean compare(Function f) {
		Power fnc = (Power) f;
		if (fnc.getVariable1().equals(new Number(null, 0)) && fnc.getVariable2().equals(new Number(null, 0))) {
			return true;
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		ArrayList<Function> result = new ArrayList<>();
		result.add(new Undefined(f.getParent()));
		return result;
	}

}

package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Number;

/**
 * Fractions rule<br>
 * <b>a / 1 = a</b>
 * @author Andrea Cavalli
 *
 */
public class FractionsRule2 {

	public static boolean compare(Function f) {
		Division fnc = (Division) f;
		if (fnc.getVariable2() instanceof Number) {
			Number numb = (Number) fnc.getVariable2();
			if (numb.equals(new Number(null, 1))) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		ArrayList<Function> result = new ArrayList<>();
		Division fnc = (Division) f;
		result.add(fnc.getVariable1().setParent(f.getParent()));
		return result;
	}

}

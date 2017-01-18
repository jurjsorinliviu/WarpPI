package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Number;

/**
 * Fractions rule<br>
 * <b>a / a = 1</b>
 * @author Andrea Cavalli
 *
 */
public class FractionsRule3 {

	public static boolean compare(Function f) {
		Division fnc = (Division) f;
		if (fnc.getVariable1().equals(fnc.getVariable2())) {
			return true;
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		ArrayList<Function> result = new ArrayList<>();
		result.add(new Number(f.getRoot(), 1));
		return result;
	}

}

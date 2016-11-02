package org.warp.picalculator.math.rules;

import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Undefined;

import java.util.ArrayList;

import org.warp.picalculator.Error;

/**
 * Undefined rule<br>
 * <b>a / 0 = undefined</b>
 * @author Andrea Cavalli
 *
 */
public class UndefinedRule2 {

	public static boolean compare(Function f) {
		try {
			if (f instanceof Division) {
				Division fnc = (Division) f;
				if (fnc.getVariable2() instanceof Number) {
					Number numb = (Number) fnc.getVariable2();
					if (numb.equals(new Number(null, "0"))) {
						return true;
					}
				}
			}
		} catch (Error e) {
			e.printStackTrace();
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		ArrayList<Function> result = new ArrayList<>();
		result.add(new Undefined(f.getParent()));
		return result;
	}

}

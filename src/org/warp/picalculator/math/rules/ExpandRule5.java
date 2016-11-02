package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Negative;
import org.warp.picalculator.math.functions.Subtraction;

/**
 * Expand rule<br>
 * <b>-(-a) = a</b>
 * @author Andrea Cavalli
 *
 */
public class ExpandRule5 {

	public static boolean compare(Function f) {
		if (f instanceof Negative) {
			Negative fnc = (Negative) f;
			if (fnc.getVariable() instanceof Negative) {
				return true;
			}
		} else if (f instanceof Subtraction) {
			Subtraction fnc = (Subtraction) f;
			if (fnc.getVariable2() instanceof Negative) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		ArrayList<Function> result = new ArrayList<>();
		Function a = null;

		if (f instanceof Negative) {
			Negative fnc = (Negative) f;
			result.add(((Negative)fnc.getVariable()).getVariable().setParent(f.getParent()));
		} else if (f instanceof Subtraction) {
			Subtraction fnc = (Subtraction) f;
			result.add(((Negative)fnc.getVariable2()).getVariable().setParent(f.getParent()));
		}
		return result;
	}

}

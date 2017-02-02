package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Number;

/**
 * Fractions rule<br>
 * <b>0 / a = 0</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class FractionsRule1 {

	public static boolean compare(Function f) {
		final Calculator root = f.getRoot();
		final Division fnc = (Division) f;
		if (fnc.getVariable1() instanceof Number) {
			final Number numb1 = (Number) fnc.getVariable1();
			if (numb1.equals(new Number(root, 0))) {
				if (fnc.getVariable2() instanceof Number) {
					final Number numb2 = (Number) fnc.getVariable2();
					if (numb2.equals(new Number(root, 0))) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		final ArrayList<Function> result = new ArrayList<>();
		result.add(new Number(f.getRoot(), 0));
		return result;
	}

}
package org.warp.picalculator.math.rules;

import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;

import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.Error;

/**
 * Fractions rule<br>
 * <b>0 / a = 0</b>
 * @author Andrea Cavalli
 *
 */
public class FractionsRule1 {

	public static boolean compare(Function f) {
		try {
			if (f instanceof Division) {
				Division fnc = (Division) f;
				if (fnc.getVariable1() instanceof Number) {
					Number numb1 = (Number) fnc.getVariable1();
					if (numb1.equals(new Number(null, "0"))) {
						if (fnc.getVariable2() instanceof Number) {
							Number numb2 = (Number) fnc.getVariable2();
							if (numb2.equals(new Number(null, "0"))) {
								return false;
							}
						}
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
		result.add(new Number(f.getParent(), "0"));
		return result;
	}

}

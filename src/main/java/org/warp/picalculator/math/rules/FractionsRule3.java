package org.warp.picalculator.math.rules;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Number;

/**
 * Fractions rule<br>
 * <b>a / a = 1</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class FractionsRule3 {

	public static boolean compare(Function f) {
		final Division fnc = (Division) f;
		if (fnc.getParameter1().equals(fnc.getParameter2())) {
			return true;
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(Function f) throws Error {
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		result.add(new Number(f.getMathContext(), 1));
		return result;
	}

}

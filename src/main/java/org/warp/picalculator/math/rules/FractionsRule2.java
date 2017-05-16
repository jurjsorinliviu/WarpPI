package org.warp.picalculator.math.rules;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Number;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Fractions rule<br>
 * <b>a / 1 = a</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class FractionsRule2 {

	public static boolean compare(Function f) {
		final Division fnc = (Division) f;
		if (fnc.getParameter2() instanceof Number) {
			final Number numb = (Number) fnc.getParameter2();
			if (numb.equals(new Number(f.getMathContext(), 1))) {
				return true;
			}
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(Function f) throws Error {
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		final Division fnc = (Division) f;
		result.add(fnc.getParameter1());
		return result;
	}

}

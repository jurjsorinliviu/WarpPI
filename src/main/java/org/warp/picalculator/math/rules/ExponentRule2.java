package org.warp.picalculator.math.rules;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Exponent rule<br>
 * <b>a^1=a</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class ExponentRule2 {

	public static boolean compare(Function f) {
		final Power fnc = (Power) f;
		if (fnc.getParameter2().equals(new Number(f.getMathContext(), 1))) {
			return true;
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(Function f) throws Error {
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		result.add(((Power) f).getParameter1());
		return result;
	}

}

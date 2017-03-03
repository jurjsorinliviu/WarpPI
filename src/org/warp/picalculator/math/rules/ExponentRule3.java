package org.warp.picalculator.math.rules;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;

/**
 * Exponent rule<br>
 * <b>a^0=1</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class ExponentRule3 {

	public static boolean compare(Function f) {
		final Power fnc = (Power) f;
		if (fnc.getParameter2().equals(new Number(f.getMathContext(), 0))) {
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

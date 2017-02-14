package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;

/**
 * Fractions rule<br>
 * <b>(a / b) ^ -1 = b / a</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class FractionsRule4 {

	public static boolean compare(Function f) {
		final Power fnc = (Power) f;
		if (fnc.getParameter1() instanceof Division && fnc.getParameter2() instanceof Number) {
			final Number n2 = (Number) fnc.getParameter2();
			if (n2.equals(new Number(f.getMathContext(), -1))) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		final ArrayList<Function> result = new ArrayList<>();
		final Power fnc = (Power) f;
		final Function a = ((Division) fnc.getParameter1()).getParameter1();
		final Function b = ((Division) fnc.getParameter1()).getParameter2();
		final Division res = new Division(f.getMathContext(), b, a);
		result.add(res);
		return result;
	}

}

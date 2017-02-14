package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;

/**
 * Exponent rule<br>
 * <b>1^a=1</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class ExponentRule1 {

	public static boolean compare(Function f) {
		final Power fnc = (Power) f;
		final MathContext root = f.getMathContext();
		if (fnc.getParameter1().equals(new Number(root, 1))) {
			return true;
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		final MathContext root = f.getMathContext();
		final ArrayList<Function> result = new ArrayList<>();
		result.add(new Number(root, 1));
		return result;
	}

}

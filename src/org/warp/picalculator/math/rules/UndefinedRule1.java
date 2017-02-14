package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;
import org.warp.picalculator.math.functions.Undefined;

/**
 * Undefined rule<br>
 * <b>0^0=undefined</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class UndefinedRule1 {

	public static boolean compare(Function f) {
		final MathContext root = f.getMathContext();
		final Power fnc = (Power) f;
		if (fnc.getParameter1().equals(new Number(root, 0)) && fnc.getParameter2().equals(new Number(root, 0))) {
			return true;
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		final MathContext root = f.getMathContext();
		final ArrayList<Function> result = new ArrayList<>();
		result.add(new Undefined(root));
		return result;
	}

}

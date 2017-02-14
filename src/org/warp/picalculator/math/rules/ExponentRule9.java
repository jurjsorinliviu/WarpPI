package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;

/**
 * Exponent rule<br>
 * <b>(a ^ b) ^ c = a ^ (b * c)</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class ExponentRule9 {

	public static boolean compare(Function f) {
		final Power fnc = (Power) f;
		if (fnc.getParameter1() instanceof Power) {
			return true;
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		final MathContext root = f.getMathContext();
		final ArrayList<Function> result = new ArrayList<>();
		final Power powC = (Power) f;
		final Power powB = (Power) powC.getParameter1();
		final Power p = new Power(root, powB.getParameter1(), new Multiplication(root, new Expression(root, powB.getParameter2()), new Expression(root, powC.getParameter2())));
		result.add(p);
		return result;
	}

}

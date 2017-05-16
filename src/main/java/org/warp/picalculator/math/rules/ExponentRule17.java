package org.warp.picalculator.math.rules;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;
import org.warp.picalculator.math.functions.Root;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Exponent rule<br>
 * <b>a√x=x^1/a</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class ExponentRule17 {

	public static boolean compare(Function f) {
		if (f instanceof Root) {
			final Root fnc = (Root) f;
			if (fnc.getParameter1().equals(fnc.getParameter2())) {
				return true;
			}
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(Function f) throws Error {
		final MathContext root = f.getMathContext();
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		final Multiplication fnc = (Multiplication) f;
		final Function a = fnc.getParameter1();
		final Expression expr = new Expression(root, a);
		final Number two = new Number(root, 2);
		final Power p = new Power(fnc.getMathContext(), expr, two);
		result.add(p);
		return result;
	}

}

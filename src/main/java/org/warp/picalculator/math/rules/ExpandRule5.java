package org.warp.picalculator.math.rules;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Negative;
import org.warp.picalculator.math.functions.Subtraction;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Expand rule<br>
 * <b>-(-a) = a</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class ExpandRule5 {

	public static boolean compare(Function f) {
		if (f instanceof Negative) {
			final Negative fnc = (Negative) f;
			if (fnc.getParameter() instanceof Expression) {
				final Expression e = (Expression) fnc.getParameter();
				return e.getParameter() instanceof Negative;
			}
		} else if (f instanceof Subtraction) {
			final Subtraction fnc = (Subtraction) f;
			if (fnc.getParameter2() instanceof Expression) {
				final Expression e = (Expression) fnc.getParameter2();
				return e.getParameter() instanceof Negative;
			}
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(Function f) throws Error {
		final ObjectArrayList<Function> result = new ObjectArrayList<>();

		if (f instanceof Negative) {
			final Negative fnc = (Negative) f;
			result.add(((Negative) ((Expression) fnc.getParameter()).getParameter()).getParameter());
		} else if (f instanceof Subtraction) {
			final Subtraction fnc = (Subtraction) f;
			result.add(((Negative) ((Expression) fnc.getParameter2()).getParameter()).getParameter());
		}
		return result;
	}

}

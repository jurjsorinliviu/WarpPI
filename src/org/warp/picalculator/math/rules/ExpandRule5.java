package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Negative;
import org.warp.picalculator.math.functions.Subtraction;

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
			if (fnc.getVariable() instanceof Expression) {
				final Expression e = (Expression) fnc.getVariable();
				return e.getVariablesLength() == 1 && e.getVariable(0) instanceof Negative;
			}
		} else if (f instanceof Subtraction) {
			final Subtraction fnc = (Subtraction) f;
			if (fnc.getVariable2() instanceof Expression) {
				final Expression e = (Expression) fnc.getVariable2();
				return e.getVariablesLength() == 1 && e.getVariable(0) instanceof Negative;
			}
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		final ArrayList<Function> result = new ArrayList<>();
		final Function a = null;

		if (f instanceof Negative) {
			final Negative fnc = (Negative) f;
			result.add(((Negative) ((Expression) fnc.getVariable()).getVariable(0)).getVariable());
		} else if (f instanceof Subtraction) {
			final Subtraction fnc = (Subtraction) f;
			result.add(((Negative) ((Expression) fnc.getVariable2()).getVariable(0)).getVariable());
		}
		return result;
	}

}
package org.warp.picalculator.math.rules;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Undefined;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Undefined rule<br>
 * <b>a / 0 = undefined</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class UndefinedRule2 {

	public static boolean compare(Function f) {
		final MathContext root = f.getMathContext();
		final Division fnc = (Division) f;
		if (fnc.getParameter2() instanceof Number) {
			final Number numb = (Number) fnc.getParameter2();
			if (numb.equals(new Number(root, 0))) {
				return true;
			}
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(Function f) throws Error {
		final MathContext root = f.getMathContext();
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		result.add(new Undefined(root));
		return result;
	}

}

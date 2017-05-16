package org.warp.picalculator.math.rules;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Number;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Number rule<br>
 * <b>a + 0 = a</b><br>
 * <b>0 + a = a</b><br>
 * <b>a - 0 = a</b><br>
 * <b>0 - a = a</b><br>
 * <b>a ± 0 = a</b><br>
 * <b>0 ± a = a</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class NumberRule5 {

	public static boolean compare(Function f) {
		final MathContext root = f.getMathContext();
		final FunctionOperator fnc = (FunctionOperator) f;
		if (fnc.getParameter1().equals(new Number(root, 0)) || fnc.getParameter2().equals(new Number(root, 0))) {
			return true;
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(Function f) throws Error {
		final MathContext root = f.getMathContext();
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		final FunctionOperator fnc = (FunctionOperator) f;
		Function a = fnc.getParameter1();
		if (a.equals(new Number(root, 0))) {
			a = fnc.getParameter2();
		}
		result.add(a);
		return result;
	}

}

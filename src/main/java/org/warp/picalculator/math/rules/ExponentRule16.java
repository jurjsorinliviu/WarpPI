package org.warp.picalculator.math.rules;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;
import org.warp.picalculator.math.functions.Sum;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Exponent rule<br>
 * <b>(a ^ b) * (a ^ c) = a ^ (b + c)</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class ExponentRule16 {

	public static boolean compare(Function f) {
		final Multiplication fnc = (Multiplication) f;
		if (fnc.getParameter1() instanceof Power && fnc.getParameter2() instanceof Power) {
			return ((Power) fnc.getParameter1()).getParameter1().equals(((Power) fnc.getParameter2()).getParameter1());
		} else if (fnc.getParameter1() instanceof Power) {
			return ((Power) fnc.getParameter1()).getParameter1().equals(fnc.getParameter2());
		} else if (fnc.getParameter2() instanceof Power) {
			return ((Power) fnc.getParameter2()).getParameter1().equals(fnc.getParameter1());
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(Function f) throws Error {
		final MathContext root = f.getMathContext();
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		final Multiplication fnc = (Multiplication) f;
		if (fnc.getParameter1() instanceof Power && fnc.getParameter2() instanceof Power) {
			result.add(new Power(root, ((Power) fnc.getParameter1()).getParameter1(), new Sum(root, new Expression(root, ((Power) fnc.getParameter1()).getParameter2()), new Expression(root, ((Power) fnc.getParameter2()).getParameter2()))));
		} else if (fnc.getParameter1() instanceof Power) {
			result.add(new Power(root, ((Power) fnc.getParameter1()).getParameter1(), new Sum(root, new Expression(root, ((Power) fnc.getParameter1()).getParameter2()), new Number(root, 1))));
		} else if (fnc.getParameter2() instanceof Power) {
			result.add(new Power(root, ((Power) fnc.getParameter1()).getParameter1(), new Sum(root, new Number(root, 1), new Expression(root, ((Power) fnc.getParameter2()).getParameter2()))));
		}
		return result;
	}

}

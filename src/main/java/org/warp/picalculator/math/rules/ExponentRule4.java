package org.warp.picalculator.math.rules;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Exponent rule<br>
 * <b>(a*b)^n=a^n*b^n</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class ExponentRule4 {

	public static boolean compare(Function f) {
		final Power fnc = (Power) f;
		if (fnc.getParameter1() instanceof Expression && ((Expression) fnc.getParameter1()).getParametersLength() == 1 && ((Expression) fnc.getParameter1()).getParameter(0) instanceof Multiplication && fnc.getParameter2() instanceof Number) {
			return true;
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(Function f) throws Error {
		final MathContext root = f.getMathContext();
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		final Power fnc = (Power) f;
		final Expression expr = (Expression) fnc.getParameter1();
		final Multiplication mult = (Multiplication) expr.getParameter(0);
		final Function a = mult.getParameter1();
		final Function b = mult.getParameter2();
		final Number n = (Number) fnc.getParameter2();
		final Expression e1 = new Expression(root, a);
		final Power p1 = new Power(root, e1, n);
		final Expression e2 = new Expression(root, b);
		final Power p2 = new Power(root, e2, n);
		final Multiplication retMult = new Multiplication(root, p1, p2);
		result.add(retMult);
		return result;
	}

}

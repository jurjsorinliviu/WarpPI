package org.warp.picalculator.math.rules;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Variable rule<br>
 * <b>ax+bx=(a+b)*x (a,b NUMBER; x VARIABLE|MULTIPLICATION)</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class VariableRule1 {

	public static boolean compare(FunctionOperator fnc) {
		if (fnc.getParameter1() instanceof Multiplication & fnc.getParameter2() instanceof Multiplication) {
			final Multiplication m1 = (Multiplication) fnc.getParameter1();
			final Multiplication m2 = (Multiplication) fnc.getParameter2();
			if (m1.getParameter1().equals(m2.getParameter1()) || m1.getParameter2().equals(m2.getParameter2())) {
				return true;
			}
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(FunctionOperator fnc) throws Error {
		final MathContext root = fnc.getMathContext();
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		final Multiplication m1 = (Multiplication) fnc.getParameter1();
		final Multiplication m2 = (Multiplication) fnc.getParameter2();
		final Function a;
		final Function b;
		final Function x;
		if (m1.getParameter2().equals(m2.getParameter2())) {
			x = m1.getParameter2();
			a = m1.getParameter1();
			b = m2.getParameter1();
		} else {
			x = m1.getParameter1();
			a = m1.getParameter2();
			b = m2.getParameter2();
		}

		FunctionOperator rets;
		if (fnc instanceof Sum) {
			rets = new Sum(root, a, b);
		} else {
			rets = new Subtraction(root, a, b);
		}
		final Multiplication retm = new Multiplication(root, rets, x);
		result.add(retm);
		return result;
	}

}

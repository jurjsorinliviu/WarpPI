package org.warp.picalculator.math.rules;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;

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
			if (m1.getParameter2().equals(m2.getParameter2())) {
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
		final Function a = m1.getParameter1();
		final Function b = m2.getParameter1();
		final Function x = m1.getParameter2();

		FunctionOperator rets;
		if (fnc instanceof Sum) {
			rets = new Sum(root, a, b);
		} else {
			rets = new Subtraction(root, a, b);
		}
		final Expression rete = new Expression(root, rets);
		final Multiplication retm = new Multiplication(root, rete, x);
		result.add(retm);
		return result;
	}

}

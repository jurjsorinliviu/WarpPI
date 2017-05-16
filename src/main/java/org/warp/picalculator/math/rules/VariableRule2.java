package org.warp.picalculator.math.rules;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Variable rule<br>
 * <b>ax+x=(a+1)*x (a,b NUMBER; x VARIABLES)</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class VariableRule2 {

	public static boolean compare(FunctionOperator fnc) {
		if (fnc.getParameter1() instanceof Multiplication) {
			final Multiplication m1 = (Multiplication) fnc.getParameter1();
			if (m1.getParameter2().equals(fnc.getParameter2())) {
				return true;
			}
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(FunctionOperator fnc) throws Error {
		final MathContext root = fnc.getMathContext();
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		final Multiplication m1 = (Multiplication) fnc.getParameter1();
		final Function a = m1.getParameter1();
		final Function x = fnc.getParameter2();

		FunctionOperator rets;
		if (fnc instanceof Sum) {
			rets = new Sum(root, a, new Number(root, 1));
		} else {
			rets = new Subtraction(root, a, new Number(root, 1));
		}
		final Expression rete = new Expression(root, rets);
		final Multiplication retm = new Multiplication(root, rete, x);
		result.add(retm);
		return result;
	}

}

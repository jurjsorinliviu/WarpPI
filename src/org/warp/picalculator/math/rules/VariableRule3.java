package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;

/**
 * Variable rule<br>
 * <b>x+ax=(a+1)*x (a,b NUMBER; x VARIABLES)</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class VariableRule3 {

	public static boolean compare(FunctionOperator fnc) {
		if (fnc.getParameter2() instanceof Multiplication) {
			final Multiplication m2 = (Multiplication) fnc.getParameter2();
			if (m2.getParameter2().equals(fnc.getParameter1())) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<Function> execute(FunctionOperator fnc) throws Error {
		final MathContext root = fnc.getMathContext();
		final ArrayList<Function> result = new ArrayList<>();
		final Multiplication m2 = (Multiplication) fnc.getParameter2();
		final Function a = m2.getParameter1();
		final Function x = fnc.getParameter1();

		FunctionOperator rets;
		if (fnc instanceof Sum) {
			rets = new Sum(root, new Number(root, 1), a);
		} else {
			rets = new Subtraction(root, new Number(root, 1), a);
		}

		final Expression rete = new Expression(root, rets);
		final Multiplication retm = new Multiplication(root, rete, x);
		result.add(retm);
		return result;
	}

}

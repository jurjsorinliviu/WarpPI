package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Power;
import org.warp.picalculator.math.functions.Number;

/**
 * Exponent rule<br>
 * <b>a*a=a^2</b>
 * @author Andrea Cavalli
 *
 */
public class ExponentRule15 {

	public static boolean compare(Function f) {
		Multiplication fnc = (Multiplication) f;
		if (fnc.getVariable1().equals(fnc.getVariable2())) {
			return true;
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		ArrayList<Function> result = new ArrayList<>();
		Multiplication fnc = (Multiplication) f;
		Power p = new Power(fnc.getParent(), null, null);
		Expression expr = new Expression(p);
		Function a = fnc.getVariable1().setParent(expr);
		expr.addFunctionToEnd(a);
		Number two = new Number(p, 2);
		p.setVariable1(expr);
		p.setVariable2(two);
		result.add(p);
		return result;
	}

}

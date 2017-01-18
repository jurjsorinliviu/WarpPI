package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;

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
		Calculator root = f.getRoot();
		ArrayList<Function> result = new ArrayList<>();
		Multiplication fnc = (Multiplication) f;
		Power p = new Power(root, null, null);
		Expression expr = new Expression(root);
		Function a = fnc.getVariable1();
		expr.addFunctionToEnd(a);
		Number two = new Number(root, 2);
		p.setVariable1(expr);
		p.setVariable2(two);
		result.add(p);
		return result;
	}

}

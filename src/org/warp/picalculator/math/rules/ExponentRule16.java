package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;
import org.warp.picalculator.math.functions.Sum;

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
		if (fnc.getVariable1() instanceof Power && fnc.getVariable2() instanceof Power) {
			return ((Power)fnc.getVariable1()).getVariable1().equals(((Power)fnc.getVariable2()).getVariable1());
		} else if (fnc.getVariable1() instanceof Power) {
			return ((Power)fnc.getVariable1()).getVariable1().equals(fnc.getVariable2());
		} else if (fnc.getVariable2() instanceof Power) {
			return ((Power)fnc.getVariable2()).getVariable1().equals(fnc.getVariable1());
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		final Calculator root = f.getRoot();
		final ArrayList<Function> result = new ArrayList<>();
		final Multiplication fnc = (Multiplication) f;
		if (fnc.getVariable1() instanceof Power && fnc.getVariable2() instanceof Power) {
			result.add(new Power(root, ((Power)fnc.getVariable1()).getVariable1(), new Sum(root, new Expression(root, ((Power)fnc.getVariable1()).getVariable2()), new Expression(root, ((Power)fnc.getVariable2()).getVariable2()))));
		} else if (fnc.getVariable1() instanceof Power) {
			result.add(new Power(root, ((Power)fnc.getVariable1()).getVariable1(), new Sum(root, new Expression(root, ((Power)fnc.getVariable1()).getVariable2()), new Number(root, 1))));
		} else if (fnc.getVariable2() instanceof Power) {
			result.add(new Power(root, ((Power)fnc.getVariable1()).getVariable1(), new Sum(root, new Number(root, 1), new Expression(root, ((Power)fnc.getVariable2()).getVariable2()))));
		}
		return result;
	}

}

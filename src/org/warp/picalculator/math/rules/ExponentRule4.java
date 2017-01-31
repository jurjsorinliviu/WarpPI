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
 * <b>(a*b)^n=a^n*b^n</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class ExponentRule4 {

	public static boolean compare(Function f) {
		final Power fnc = (Power) f;
		if (fnc.getVariable1() instanceof Expression && ((Expression) fnc.getVariable1()).getVariablesLength() == 1 && ((Expression) fnc.getVariable1()).getVariable(0) instanceof Multiplication && fnc.getVariable2() instanceof Number) {
			return true;
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		final Calculator root = f.getRoot();
		final ArrayList<Function> result = new ArrayList<>();
		final Power fnc = (Power) f;
		final Expression expr = (Expression) fnc.getVariable1();
		final Multiplication mult = (Multiplication) expr.getVariable(0);
		final Function a = mult.getVariable1();
		final Function b = mult.getVariable2();
		final Number n = (Number) fnc.getVariable2();
		final Multiplication retMult = new Multiplication(root, null, null);
		final Power p1 = new Power(root, null, null);
		final Expression e1 = new Expression(root);
		e1.addFunctionToEnd(a);
		p1.setVariable1(e1);
		p1.setVariable2(n);
		final Power p2 = new Power(root, null, null);
		final Expression e2 = new Expression(root);
		e2.addFunctionToEnd(b);
		p2.setVariable1(e2);
		p2.setVariable2(n);
		retMult.setVariable1(p1);
		retMult.setVariable2(p2);
		result.add(retMult);
		return result;
	}

}

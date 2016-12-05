package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;

/**
 * Exponent rule<br>
 * <b>(a*b)^n=a^n*b^n</b>
 * @author Andrea Cavalli
 *
 */
public class ExponentRule4 {

	public static boolean compare(Function f) {
		Power fnc = (Power) f;
		if (fnc.getVariable1() instanceof Expression && ((Expression) fnc.getVariable1()).getVariablesLength() == 1 && ((Expression) fnc.getVariable1()).getVariable(0) instanceof Multiplication && fnc.getVariable2() instanceof Number) {
			return true;
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		ArrayList<Function> result = new ArrayList<>();
		Power fnc = (Power) f;
		Expression expr = (Expression) fnc.getVariable1();
		Multiplication mult = (Multiplication) expr.getVariable(0);
		Function a = mult.getVariable1();
		Function b = mult.getVariable2();
		Number n = (Number) fnc.getVariable2();
		Multiplication retMult = new Multiplication(f.getParent(), null, null);
		Power p1 = new Power(retMult, null, null);
		Expression e1 = new Expression(p1);
		e1.addFunctionToEnd(a);
		p1.setVariable1(e1);
		p1.setVariable2(n);
		Power p2 = new Power(retMult, null, null);
		Expression e2 = new Expression(p2);
		e2.addFunctionToEnd(b);
		p2.setVariable1(e2);
		p2.setVariable2(n);
		retMult.setVariable1(p1);
		retMult.setVariable2(p2);
		result.add(retMult);
		return result;
	}

}

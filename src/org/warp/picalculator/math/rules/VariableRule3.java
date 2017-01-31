package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.FunctionTwoValues;
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

	public static boolean compare(FunctionTwoValues fnc) {
		if (fnc.getVariable2() instanceof Multiplication) {
			final Multiplication m2 = (Multiplication) fnc.getVariable2();
			if (m2.getVariable2().equals(fnc.getVariable1())) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<Function> execute(FunctionTwoValues fnc) throws Error {
		final Calculator root = fnc.getRoot();
		final ArrayList<Function> result = new ArrayList<>();
		final Multiplication m2 = (Multiplication) fnc.getVariable2();
		final Function a = m2.getVariable1();
		final Function x = fnc.getVariable1();

		final Multiplication retm = new Multiplication(root, null, null);
		final Expression rete = new Expression(root);
		FunctionTwoValues rets;
		if (fnc instanceof Sum) {
			rets = new Sum(root, null, null);
		} else {
			rets = new Subtraction(root, null, null);
		}

		rets.setVariable1(new Number(root, 1));
		rets.setVariable2(a);
		rete.addFunctionToEnd(rets);
		retm.setVariable1(rete);
		retm.setVariable2(x);
		result.add(retm);
		return result;
	}

}

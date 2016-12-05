package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.FunctionTwoValues;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;

/**
 * Variable rule<br>
 * <b>ax+bx=(a+b)*x (a,b NUMBER; x VARIABLE|MULTIPLICATION)</b>
 * @author Andrea Cavalli
 *
 */
public class VariableRule1 {

	public static boolean compare(FunctionTwoValues fnc) {
		if (fnc.getVariable1() instanceof Multiplication & fnc.getVariable2() instanceof Multiplication) {
			Multiplication m1 = (Multiplication) fnc.getVariable1();
			Multiplication m2 = (Multiplication) fnc.getVariable2();
			if (m1.getVariable2().equals(m2.getVariable2())) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<Function> execute(FunctionTwoValues fnc) throws Error {
		ArrayList<Function> result = new ArrayList<>();
		Multiplication m1 = (Multiplication) fnc.getVariable1();
		Multiplication m2 = (Multiplication) fnc.getVariable2();
		Function a = m1.getVariable1();
		Function b = m2.getVariable1();
		Function x = m1.getVariable2();
		
		Multiplication retm = new Multiplication(fnc.getParent(), null, null);
		Expression rete = new Expression(retm);
		FunctionTwoValues rets;
		if (fnc instanceof Sum){
			rets = new Sum(rete, null, null);
		} else {
			rets = new Subtraction(rete, null, null);
		}
		rets.setVariable1(a);
		rets.setVariable2(b);
		rete.addFunctionToEnd(rets);
		retm.setVariable1(rete);
		retm.setVariable2(x);
		result.add(retm);
		return result;
	}

}

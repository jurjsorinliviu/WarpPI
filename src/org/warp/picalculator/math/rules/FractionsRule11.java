package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Multiplication;

/**
 * Fractions rule<br>
 * <b>a / (b / c) = (a * c) / b</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class FractionsRule11 {

	public static boolean compare(Function f) {
		final Division fnc = (Division) f;
		Function a;
		Function c;
		Division div2;
		if (fnc.getVariable2() instanceof Division) {
			div2 = (Division) fnc.getVariable2();
		} else {
			return false;
		}
		a = fnc.getVariable1();
		c = div2.getVariable2();
		return new Multiplication(fnc.getRoot(), a, c).isSolved() == false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		final ArrayList<Function> result = new ArrayList<>();
		final Division fnc = (Division) f;
		Function a;
		Function b;
		Function c;

		Division div2 = (Division) fnc.getVariable2();
		
		a = fnc.getVariable1();
		b = div2.getVariable1();
		c = div2.getVariable2();
		result.add(new Division(fnc.getRoot(), new Multiplication(fnc.getRoot(), new Expression(fnc.getRoot(), a), new Expression(fnc.getRoot(), c)), b));

		return result;
	}

}

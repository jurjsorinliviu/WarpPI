package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Multiplication;

/**
 * Fractions rule<br>
 * <b>(a / b) * (c / d) = (a * c) / (b * d)</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class FractionsRule14 {

	public static boolean compare(Function f) {
		final Multiplication fnc = (Multiplication) f;
		Function a;
		Function b;
		Function c;
		Function d;
		if (fnc.getVariable1() instanceof Division && fnc.getVariable2() instanceof Division) {
			final Division div1 = (Division) fnc.getVariable1();
			final Division div2 = (Division) fnc.getVariable2();
			a = div1.getVariable1();
			b = div1.getVariable2();
			c = div2.getVariable1();
			d = div2.getVariable2();
			return new Multiplication(f.getRoot(), a, c).isSolved() == false || new Multiplication(f.getRoot(), b, d).isSolved() == false;
		} else if (fnc.getVariable1() instanceof Division) {
			final Division div1 = (Division) fnc.getVariable1();
			a = div1.getVariable1();
			b = div1.getVariable2();
			c = fnc.getVariable2();
			return new Multiplication(f.getRoot(), a, c).isSolved() == false;
		} else if (fnc.getVariable2() instanceof Division) {
			final Division div2 = (Division) fnc.getVariable2();
			a = fnc.getVariable1();
			c = div2.getVariable1();
			d = div2.getVariable2();
			return new Multiplication(f.getRoot(), a, c).isSolved() == false;
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		final ArrayList<Function> result = new ArrayList<>();
		final Multiplication fnc = (Multiplication) f;
		Function a;
		Function b;
		Function c;
		Function d;

		if (fnc.getVariable1() instanceof Division && fnc.getVariable2() instanceof Division) {
			final Division div1 = (Division) fnc.getVariable1();
			final Division div2 = (Division) fnc.getVariable2();
			a = div1.getVariable1();
			b = div1.getVariable2();
			c = div2.getVariable1();
			d = div2.getVariable2();
			final Division div = new Division(fnc.getRoot(), new Multiplication(fnc.getRoot(), new Expression(fnc.getRoot(), a), new Expression(fnc.getRoot(), c)), new Multiplication(fnc.getRoot(), new Expression(fnc.getRoot(), b), new Expression(fnc.getRoot(), d)));
			result.add(div);
		} else if (fnc.getVariable1() instanceof Division) {
			final Division div1 = (Division) fnc.getVariable1();
			a = div1.getVariable1();
			b = div1.getVariable2();
			c = fnc.getVariable2();
			final Division div = new Division(fnc.getRoot(), new Multiplication(fnc.getRoot(), new Expression(fnc.getRoot(), a), new Expression(fnc.getRoot(), c)), b);
			result.add(div);
		} else if (fnc.getVariable2() instanceof Division) {
			final Division div2 = (Division) fnc.getVariable2();
			a = fnc.getVariable1();
			c = div2.getVariable1();
			d = div2.getVariable2();
			final Division div = new Division(fnc.getRoot(), new Multiplication(fnc.getRoot(), new Expression(fnc.getRoot(), a), new Expression(fnc.getRoot(), c)), d);
			result.add(div);
		}
		return result;
	}

}

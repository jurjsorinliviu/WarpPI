package org.warp.picalculator.math.rules;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Multiplication;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Fractions rule<br>
 * <b>(b / c) / a = b / (a * c)</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class FractionsRule12 {

	public static boolean compare(Function f) {
		final Division fnc = (Division) f;
		Function a;
		Function c;
		if (fnc.getParameter1() instanceof Division) {
			final Division div2 = (Division) fnc.getParameter1();
			a = fnc.getParameter1();
			c = div2.getParameter2();
			return new Multiplication(fnc.getMathContext(), a, c).isSimplified() == false;
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(Function f) throws Error {
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		final Division fnc = (Division) f;
		Function a;
		Function b;
		Function c;

		final Division div2 = (Division) fnc.getParameter1();
		a = fnc.getParameter2();
		b = div2.getParameter1();
		c = div2.getParameter2();
		result.add(new Division(fnc.getMathContext(), new Multiplication(fnc.getMathContext(), new Expression(fnc.getMathContext(), a), new Expression(fnc.getMathContext(), c)), b));

		return result;
	}

}

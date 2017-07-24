package org.warp.picalculator.math.rules;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Multiplication;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

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
		if (fnc.getParameter1() instanceof Division && fnc.getParameter2() instanceof Division) {
			final Division div1 = (Division) fnc.getParameter1();
			final Division div2 = (Division) fnc.getParameter2();
			a = div1.getParameter1();
			b = div1.getParameter2();
			c = div2.getParameter1();
			d = div2.getParameter2();
			return new Multiplication(f.getMathContext(), a, c).isSimplified() == false || new Multiplication(f.getMathContext(), b, d).isSimplified() == false;
		} else if (fnc.getParameter1() instanceof Division) {
			final Division div1 = (Division) fnc.getParameter1();
			a = div1.getParameter1();
			b = div1.getParameter2();
			c = fnc.getParameter2();
			return new Multiplication(f.getMathContext(), a, c).isSimplified() == false;
		} else if (fnc.getParameter2() instanceof Division) {
			final Division div2 = (Division) fnc.getParameter2();
			a = fnc.getParameter1();
			c = div2.getParameter1();
			d = div2.getParameter2();
			return new Multiplication(f.getMathContext(), a, c).isSimplified() == false;
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(Function f) throws Error {
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		final Multiplication fnc = (Multiplication) f;
		Function a;
		Function b;
		Function c;
		Function d;

		if (fnc.getParameter1() instanceof Division && fnc.getParameter2() instanceof Division) {
			final Division div1 = (Division) fnc.getParameter1();
			final Division div2 = (Division) fnc.getParameter2();
			a = div1.getParameter1();
			b = div1.getParameter2();
			c = div2.getParameter1();
			d = div2.getParameter2();
			final Division div = new Division(fnc.getMathContext(), new Multiplication(fnc.getMathContext(), a, c), new Multiplication(fnc.getMathContext(), b, d));
			result.add(div);
		} else if (fnc.getParameter1() instanceof Division) {
			final Division div1 = (Division) fnc.getParameter1();
			a = div1.getParameter1();
			b = div1.getParameter2();
			c = fnc.getParameter2();
			final Division div = new Division(fnc.getMathContext(), new Multiplication(fnc.getMathContext(), a, c), b);
			result.add(div);
		} else if (fnc.getParameter2() instanceof Division) {
			final Division div2 = (Division) fnc.getParameter2();
			a = fnc.getParameter1();
			c = div2.getParameter1();
			d = div2.getParameter2();
			final Division div = new Division(fnc.getMathContext(), new Multiplication(fnc.getMathContext(), a, c), d);
			result.add(div);
		}
		return result;
	}

}

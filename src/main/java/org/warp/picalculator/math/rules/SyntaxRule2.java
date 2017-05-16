package org.warp.picalculator.math.rules;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Sum;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Syntax rule<br>
 * <b>a+(b+c)=(a+b)+c</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class SyntaxRule2 {

	public static boolean compare(Sum f) {
		if (f.getParameter2() instanceof Sum) {
			return true;
		}
		if (f.getParameter2() instanceof Expression) {
			final Expression e = (Expression) f.getParameter2();
			if (e.getParametersLength() == 1 && e.getParameter(0) instanceof Sum) {
				return true;
			}
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(Sum f) throws Error {
		final MathContext root = f.getMathContext();
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		final Function a = f.getParameter1();
		Function b, c;
		if (f.getParameter2() instanceof Sum) {
			b = ((Sum) f.getParameter2()).getParameter1();
			c = ((Sum) f.getParameter2()).getParameter2();
		} else {
			b = ((Sum) ((Expression) f.getParameter2()).getParameter(0)).getParameter1();
			c = ((Sum) ((Expression) f.getParameter2()).getParameter(0)).getParameter2();
		}
		final Sum mIn = new Sum(root, a, b);
		f.setParameter1(mIn);
		f.setParameter2(c);
		result.add(f);
		return result;
	}

}

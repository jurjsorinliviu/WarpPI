package org.warp.picalculator.math.rules;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Sum;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Syntax rule<br>
 * <b>(a*b)*c=a*(b*c)</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class SyntaxRule1 {

	public static boolean compare(Function f) {
		final FunctionOperator m = (FunctionOperator) f;
		if (m instanceof Multiplication & m.getParameter1() instanceof Multiplication) {
			return true;
		} else if (m instanceof Sum & m.getParameter1() instanceof Sum) {
			return true;
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(Function f) throws Error {
		final MathContext root = f.getMathContext();
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		FunctionOperator mOut = (FunctionOperator) f;
		final Function a = ((FunctionOperator) mOut.getParameter1()).getParameter1();
		final Function b = ((FunctionOperator) mOut.getParameter1()).getParameter2();
		final Function c = mOut.getParameter2();
		FunctionOperator mIn;
		if (f instanceof Multiplication) {
			mIn = new Multiplication(root, b, c);
		} else {
			mIn = new Sum(root, b, c);
		}
		mOut = mOut.setParameter1(a);
		mOut = mOut.setParameter2(mIn);
		result.add(mOut);
		return result;
	}

}

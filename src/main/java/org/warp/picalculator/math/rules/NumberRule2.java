package org.warp.picalculator.math.rules;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Number rule<br>
 * <b>a * 1 = a</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class NumberRule2 {

	public static boolean compare(Function f) {
		final MathContext root = f.getMathContext();
		final Multiplication mult = (Multiplication) f;
		if (mult.getParameter1() instanceof Number) {
			final Number numb = (Number) mult.getParameter1();
			if (numb.equals(new Number(root, 1))) {
				return true;
			}
		}
		if (mult.getParameter2() instanceof Number) {
			final Number numb = (Number) mult.getParameter2();
			if (numb.equals(new Number(root, 1))) {
				return true;
			}
		}
		return false;
	}

	public static ObjectArrayList<Function> execute(Function f) throws Error {
		final MathContext root = f.getMathContext();
		final ObjectArrayList<Function> result = new ObjectArrayList<>();
		Function a = null;
		boolean aFound = false;
		final Multiplication mult = (Multiplication) f;
		if (aFound == false & mult.getParameter1() instanceof Number) {
			final Number numb = (Number) mult.getParameter1();
			if (numb.equals(new Number(root, 1))) {
				a = mult.getParameter2();
				aFound = true;
			}
		}
		if (aFound == false && mult.getParameter2() instanceof Number) {
			final Number numb = (Number) mult.getParameter2();
			if (numb.equals(new Number(root, 1))) {
				a = mult.getParameter1();
				aFound = true;
			}
		}

		result.add(a);
		return result;
	}

}

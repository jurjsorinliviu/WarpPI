package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Negative;
import org.warp.picalculator.math.functions.Number;

/**
 * Number rule<br>
 * <b>a * -1 = -a</b>
 * @author Andrea Cavalli
 *
 */
public class NumberRule6 {

	public static boolean compare(Function f) {
		Multiplication mult = (Multiplication) f;
		if (mult.getVariable1() instanceof Number) {
			Number numb = (Number) mult.getVariable1();
			if (numb.equals(new Number(root, -1))) {
				return true;
			}
		}
		if (mult.getVariable2() instanceof Number) {
			Number numb = (Number) mult.getVariable2();
			if (numb.equals(new Number(root, -1))) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		ArrayList<Function> result = new ArrayList<>();
		Function a = null;
		boolean aFound = false;
		Multiplication mult = (Multiplication) f;
		if (aFound == false & mult.getVariable1() instanceof Number) {
			Number numb = (Number) mult.getVariable1();
			if (numb.equals(new Number(root, -1))) {
				a = mult.getVariable2();
				aFound = true;
			}
		}
		if (aFound == false && mult.getVariable2() instanceof Number) {
			Number numb = (Number) mult.getVariable2();
			if (numb.equals(new Number(root, -1))) {
				a = mult.getVariable1();
				aFound = true;
			}
		}
		
		Negative minus = new Negative(f.getParent(), null);
		minus.setVariable(a);
		
		result.add(minus);
		return result;
	}

}

package org.warp.picalculator.math.rules;

import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;

import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.Error;

/**
 * Number rule<br>
 * <b>a * 1 = a</b>
 * @author Andrea Cavalli
 *
 */
public class NumberRule2 {

	public static boolean compare(Function f) {
		try {
			if (f instanceof Multiplication) {
				Multiplication mult = (Multiplication) f;
				if (mult.getVariable1() instanceof Number) {
					Number numb = (Number) mult.getVariable1();
					if (numb.equals(new Number(null, "1"))) {
						return true;
					}
				}
				if (mult.getVariable2() instanceof Number) {
					Number numb = (Number) mult.getVariable2();
					if (numb.equals(new Number(null, "1"))) {
						return true;
					}
				}
			}
		} catch (Error e) {
			e.printStackTrace();
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
			if (numb.equals(new Number(null, "1"))) {
				a = mult.getVariable2();
				aFound = true;
			}
		}
		if (aFound == false && mult.getVariable2() instanceof Number) {
			Number numb = (Number) mult.getVariable2();
			if (numb.equals(new Number(null, "1"))) {
				a = mult.getVariable1();
				aFound = true;
			}
		}
		
		result.add(a.setParent(f.getParent()));
		return result;
	}

}

package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Sum;

/**
 * Syntax rule<br>
 * <b>a+(b+c)=(a+b)+c</b>
 * @author Andrea Cavalli
 *
 */
public class SyntaxRule2 {

	public static boolean compare(Sum f) {
		if (f.getVariable2() instanceof Sum) {
			return true;
		}
		if (f.getVariable2() instanceof Expression) {
			Expression e = (Expression) f.getVariable2();
			if (e.getVariablesLength() == 1 && e.getVariable(0) instanceof Sum) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<Function> execute(Sum f) throws Error {
		Calculator root = f.getRoot();
		ArrayList<Function> result = new ArrayList<>();
		Function a = f.getVariable1();
		Function b, c;
		if (f.getVariable2() instanceof Sum) {
			b = ((Sum)f.getVariable2()).getVariable1();
			c = ((Sum)f.getVariable2()).getVariable2();
		} else {
			b = ((Sum)((Expression)f.getVariable2()).getVariable(0)).getVariable1();
			c = ((Sum)((Expression)f.getVariable2()).getVariable(0)).getVariable2();
		}
		Sum mIn = new Sum(root, null, null);
		f.setVariable1(mIn);
		mIn.setVariable1(a);
		mIn.setVariable2(b);
		f.setVariable2(c);
		result.add(f);
		return result;
	}

}

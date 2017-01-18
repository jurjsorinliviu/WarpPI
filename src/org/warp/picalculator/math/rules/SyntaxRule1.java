package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.FunctionTwoValues;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Sum;

/**
 * Syntax rule<br>
 * <b>(a*b)*c=a*(b*c)</b>
 * @author Andrea Cavalli
 *
 */
public class SyntaxRule1 {

	public static boolean compare(Function f) {
		FunctionTwoValues m = (FunctionTwoValues) f;
		if (m instanceof Multiplication & m.getVariable1() instanceof Multiplication) {
			return true;
		} else if (m instanceof Sum & m.getVariable1() instanceof Sum) {
			return true;
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		Calculator root = f.getRoot();
		ArrayList<Function> result = new ArrayList<>();
		FunctionTwoValues mOut = (FunctionTwoValues) f;
		Function a = ((FunctionTwoValues)mOut.getVariable1()).getVariable1();
		Function b = ((FunctionTwoValues)mOut.getVariable1()).getVariable2();
		Function c = mOut.getVariable2();
		FunctionTwoValues mIn;
		if (f instanceof Multiplication) {
			mIn = new Multiplication(root, null, null);
		} else {
			mIn = new Sum(root, null, null);
		}
		mOut.setVariable1(a);
		mIn.setVariable1(b);
		mIn.setVariable2(c);
		mOut.setVariable2(mIn);
		result.add(mOut);
		return result;
	}

}

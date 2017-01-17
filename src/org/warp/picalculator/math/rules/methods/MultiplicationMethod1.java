package org.warp.picalculator.math.rules.methods;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;

/**
 * Multiplication method<br>
 * <b>Example: X*3*X*2 = 6*X^2</b>
 * @author Andrea Cavalli
 *
 */
public class MultiplicationMethod1 {

	public static boolean compare(Function f) {
		return ((Multiplication)f).getVariable1().isSolved() && ((Multiplication)f).getVariable2().isSolved() && !(((Multiplication)f).getVariable1() instanceof Number && ((Multiplication)f).getVariable2() instanceof Number) && getFirstWorkingMultiplicationCouple(getMultiplicationElements(f)) != null;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		Function result;
		ArrayList<Function> elements = getMultiplicationElements(f);
		int[] workingElementCouple = getFirstWorkingMultiplicationCouple(elements);
		Function elem1 = elements.get(workingElementCouple[0]);
		Function elem2 = elements.get(workingElementCouple[1]);
		
		final int size = elements.size();
		Function prec = new Multiplication(root, elem1, elem2);
		elem1.setParent(prec);
		elem2.setParent(prec);
		for (int i = size-1; i >= 0; i--) {
			if (i != workingElementCouple[0] & i != workingElementCouple[1]) {
				Function a = prec;
				Function b = elements.get(i);
				prec = new Multiplication(root, a, b);
				a.setParent(prec);
				b.setParent(prec);
			}
		}
		prec.setParent(root);
		
		result = prec;
		
		ArrayList<Function> results = new ArrayList<>();
		results.add(result);
		return results;
	}
	
	private static ArrayList<Function> getMultiplicationElements(Function mult) {
		ArrayList<Function> elements = new ArrayList<>();
		while (mult instanceof Multiplication) {
			elements.add(((Multiplication) mult).getVariable1());
			mult = ((Multiplication) mult).getVariable2();
		}
		elements.add(mult);
		return elements;
	}
	
	private static int[] getFirstWorkingMultiplicationCouple(ArrayList<Function> elements) {
		final int size = elements.size();
		Function a;
		Function b;
		if (elements.size() == 2) {
			return null;
		}
		for (int i = 0; i < size; i++) {
			a = elements.get(i);
			for (int j = 0; j < size; j++) {
				b = elements.get(j);
				if (i != j) {
					Function testFunc;
					testFunc = new Multiplication(root, a, b);
					if (!testFunc.isSolved()) {
						return new int[]{i, j};
					}
				}
			}
		}
		return null;
	}

}

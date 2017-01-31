package org.warp.picalculator.math.rules.methods;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Number;

/**
 * Division method<br>
 * <b>Example: (XY)/(YZ) = X/Z</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class DivisionRule1 {

	public static boolean compare(Division f) {
		return f.getVariable1().isSolved() && f.getVariable2().isSolved() && !(f.getVariable1() instanceof Number && f.getVariable2() instanceof Number) && getFirstWorkingDivisionCouple(getDivisionElements(f)) != null;
	}

	public static ArrayList<Function> execute(Division f) throws Error {
		final Calculator root = f.getRoot();
		Function result;
		final ArrayList<Function> elements = getDivisionElements(f);
		final int[] workingElementCouple = getFirstWorkingDivisionCouple(elements);
		final Function elem1 = elements.get(workingElementCouple[0]);
		final Function elem2 = elements.get(workingElementCouple[1]);

		final int size = elements.size();
		Function prec = new Multiplication(root, elem1, elem2);
		for (int i = size - 1; i >= 0; i--) {
			if (i != workingElementCouple[0] & i != workingElementCouple[1]) {
				final Function a = prec;
				final Function b = elements.get(i);
				prec = new Multiplication(root, a, b);
			}
		}

		result = prec;

		final ArrayList<Function> results = new ArrayList<>();
		results.add(result);
		return results;
	}

	private static ArrayList<Function> getDivisionElements(Division division) {
		final ArrayList<Function> elementsNumerator = new ArrayList<>();
		Function numMult = division.getVariable1();
		while (numMult instanceof Multiplication) {
			elementsNumerator.add(((Multiplication) numMult).getVariable1());
			numMult = ((Multiplication) numMult).getVariable2();
		}
		elementsNumerator.add(numMult);

		final ArrayList<Function> elementsDenominator = new ArrayList<>();
		Function denomMult = division.getVariable1();
		while (denomMult instanceof Multiplication) {
			elementsDenominator.add(((Multiplication) denomMult).getVariable1());
			denomMult = ((Multiplication) denomMult).getVariable2();
		}
		elementsDenominator.add(denomMult);

		return elements;
	}

	private static int[] getFirstWorkingDivisionCouple(ArrayList<Function> elements) {
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
						return new int[] { i, j };
					}
				}
			}
		}
		return null;
	}

}

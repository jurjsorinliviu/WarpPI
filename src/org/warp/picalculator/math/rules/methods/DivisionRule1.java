package org.warp.picalculator.math.rules.methods;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Number;

/**
 * Division method<br>
 * <b>Example: (XY)/(YZ) = X/Z</b>
 * @author Andrea Cavalli
 *
 */
public class DivisionRule1 {

	public static boolean compare(Function f) {
		return ((Division)f).getVariable1().isSolved() && ((Division)f).getVariable2().isSolved() && !(((Division)f).getVariable1() instanceof Number && ((Division)f).getVariable2() instanceof Number) && getFirstWorkingDivisionCouple(getDivisionElements(f)) != null;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		Function result;
		ArrayList<Function> elements = getDivisionElements(f);
		int[] workingElementCouple = getFirstWorkingDivisionCouple(elements);
		Function elem1 = elements.get(workingElementCouple[0]);
		Function elem2 = elements.get(workingElementCouple[1]);
		
		final int size = elements.size();
		Function prec = new Multiplication(null, elem1, elem2);
		elem1.setParent(prec);
		elem2.setParent(prec);
		for (int i = size-1; i >= 0; i--) {
			if (i != workingElementCouple[0] & i != workingElementCouple[1]) {
				Function a = prec;
				Function b = elements.get(i);
				prec = new Multiplication(null, a, b);
				a.setParent(prec);
				b.setParent(prec);
			}
		}
		prec.setParent(f.getParent());
		
		result = prec;
		
		ArrayList<Function> results = new ArrayList<>();
		results.add(result);
		return results;
	}
	
	private static ArrayList<Function> getDivisionElements(Division division) {
		ArrayList<Function> elementsNumerator = new ArrayList<>();
		Function numMult = division.getVariable1();
		while (numMult instanceof Multiplication) {
			elementsNumerator.add(((Multiplication) numMult).getVariable1());
			numMult = ((Multiplication) numMult).getVariable2();
		}
		elementsNumerator.add(numMult);

		ArrayList<Function> elementsDenominator = new ArrayList<>();
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
					testFunc = new Multiplication(null, a, b);
					if (!testFunc.isSolved()) {
						return new int[]{i, j};
					}
				}
			}
		}
		return null;
	}

}

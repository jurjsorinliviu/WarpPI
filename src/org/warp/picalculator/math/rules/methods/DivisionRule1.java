package org.warp.picalculator.math.rules.methods;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
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
		return f.getParameter1().isSimplified() && f.getParameter2().isSimplified() && !(f.getParameter1() instanceof Number && f.getParameter2() instanceof Number) && getFirstWorkingDivisionCouple(getDivisionElements(f)) != null;
	}

	public static ObjectArrayList<Function> execute(Division f) throws Error {
		final MathContext root = f.getMathContext();
		Function result;
		final ObjectArrayList<Function> elements = getDivisionElements(f);
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

		final ObjectArrayList<Function> results = new ObjectArrayList<>();
		results.add(result);
		return results;
	}

	private static ObjectArrayList<Function> getDivisionElements(Division division) {
		final ObjectArrayList<Function> elementsNumerator = new ObjectArrayList<>();
		Function numMult = division.getParameter1();
		while (numMult instanceof Multiplication) {
			elementsNumerator.add(((Multiplication) numMult).getParameter1());
			numMult = ((Multiplication) numMult).getParameter2();
		}
		elementsNumerator.add(numMult);

		final ObjectArrayList<Function> elementsDenominator = new ObjectArrayList<>();
		Function denomMult = division.getParameter1();
		while (denomMult instanceof Multiplication) {
			elementsDenominator.add(((Multiplication) denomMult).getParameter1());
			denomMult = ((Multiplication) denomMult).getParameter2();
		}
		elementsDenominator.add(denomMult);

		return elements;
	}

	private static int[] getFirstWorkingDivisionCouple(ObjectArrayList<Function> elements) {
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
					if (!testFunc.isSimplified()) {
						return new int[] { i, j };
					}
				}
			}
		}
		return null;
	}

}

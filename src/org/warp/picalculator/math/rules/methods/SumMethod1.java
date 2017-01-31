package org.warp.picalculator.math.rules.methods;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.FunctionTwoValues;
import org.warp.picalculator.math.functions.Negative;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;

/**
 * Sum method<br>
 * <b>13+sqrt(2)+5X+1 = 14+sqrt(2)+5X</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class SumMethod1 {

	public static boolean compare(Function f) {
		final Calculator root = f.getRoot();
		return (f instanceof Sum || f instanceof Subtraction) && ((FunctionTwoValues) f).getVariable1().isSolved() && ((FunctionTwoValues) f).getVariable2().isSolved() && !(((FunctionTwoValues) f).getVariable1() instanceof Number && ((FunctionTwoValues) f).getVariable2() instanceof Number) && getFirstWorkingSumCouple(root, getSumElements(f)) != null;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		Function result;
		final Calculator root = f.getRoot();
		final ArrayList<Function> elements = getSumElements(f);
		final int[] workingElementCouple = getFirstWorkingSumCouple(root, elements);
		final Function elem1 = elements.get(workingElementCouple[0]);
		final Function elem2 = elements.get(workingElementCouple[1]);

		final int size = elements.size();
		Function prec = new Sum(root, elem1, elem2);
		for (int i = size - 1; i >= 0; i--) {
			if (i != workingElementCouple[0] & i != workingElementCouple[1]) {
				final Function a = prec;
				final Function b = elements.get(i);
				if (b instanceof Negative) {
					prec = new Subtraction(root, a, ((Negative) b).getVariable());
					((FunctionTwoValues) prec).getVariable2();
				} else if (b instanceof Number && ((Number) b).getTerm().compareTo(BigDecimal.ZERO) < 0) {
					prec = new Subtraction(root, a, ((Number) b).multiply(new Number(root, -1)));
					((FunctionTwoValues) prec).getVariable2();
				} else {
					prec = new Sum(root, a, b);
				}
			}
		}

		result = prec;

		final ArrayList<Function> results = new ArrayList<>();
		results.add(result);
		return results;
	}

	private static ArrayList<Function> getSumElements(Function sum) {
		final Calculator root = sum.getRoot();
		final ArrayList<Function> elements = new ArrayList<>();
		while (sum instanceof Sum || sum instanceof Subtraction) {
			if (sum instanceof Sum) {
				elements.add(((FunctionTwoValues) sum).getVariable2());
			} else {
				elements.add(new Negative(root, ((FunctionTwoValues) sum).getVariable2()));
			}
			sum = ((FunctionTwoValues) sum).getVariable1();
		}
		elements.add(sum);
		return elements;
	}

	private static int[] getFirstWorkingSumCouple(Calculator root, ArrayList<Function> elements) {
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
					if (b instanceof Negative) {
						testFunc = new Subtraction(root, a, ((Negative) b).getVariable());
					} else if (b instanceof Number && ((Number) b).getTerm().compareTo(BigDecimal.ZERO) < 0) {
						testFunc = new Subtraction(root, a, ((Number) b).multiply(new Number(root, -1)));
					} else if (a instanceof Negative) {
						testFunc = new Subtraction(root, b, ((Negative) a).getVariable());
					} else if (a instanceof Number && ((Number) a).getTerm().compareTo(BigDecimal.ZERO) < 0) {
						testFunc = new Subtraction(root, b, ((Number) a).multiply(new Number(root, -1)));
					} else {
						testFunc = new Sum(root, a, b);
					}
					if (!testFunc.isSolved()) {
						return new int[] { i, j };
					}
				}
			}
		}
		return null;
	}

}

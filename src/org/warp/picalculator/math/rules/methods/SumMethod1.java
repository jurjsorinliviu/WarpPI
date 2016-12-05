package org.warp.picalculator.math.rules.methods;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.FunctionTwoValues;
import org.warp.picalculator.math.functions.Negative;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;

/**
 * Sum method<br>
 * <b>13+sqrt(2)+5X+1 = 14+sqrt(2)+5X</b>
 * @author Andrea Cavalli
 *
 */
public class SumMethod1 {

	public static boolean compare(Function f) {
		return (f instanceof Sum || f instanceof Subtraction) && ((FunctionTwoValues)f).getVariable1().isSolved() && ((FunctionTwoValues)f).getVariable2().isSolved() && !(((FunctionTwoValues)f).getVariable1() instanceof Number && ((FunctionTwoValues)f).getVariable2() instanceof Number) && getFirstWorkingSumCouple(getSumElements(f)) != null;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		Function result;
		ArrayList<Function> elements = getSumElements(f);
		int[] workingElementCouple = getFirstWorkingSumCouple(elements);
		Function elem1 = elements.get(workingElementCouple[0]);
		Function elem2 = elements.get(workingElementCouple[1]);
		
		final int size = elements.size();
		Function prec = new Sum(null, elem1, elem2);
		elem1.setParent(prec);
		elem2.setParent(prec);
		for (int i = size-1; i >= 0; i--) {
			if (i != workingElementCouple[0] & i != workingElementCouple[1]) {
				Function a = prec;
				Function b = elements.get(i);
				if (b instanceof Negative) {
					prec = new Subtraction(null, a, ((Negative)b).getVariable());
					a.setParent(prec);
					((FunctionTwoValues)prec).getVariable2().setParent(prec);
				} else if (b instanceof Number && ((Number) b).getTerm().compareTo(BigDecimal.ZERO) < 0) {
					prec = new Subtraction(null, a, ((Number)b).multiply(new Number(null, -1)));
					a.setParent(prec);
					((FunctionTwoValues)prec).getVariable2().setParent(prec);
				} else {
					prec = new Sum(null, a, b);
					a.setParent(prec);
					b.setParent(prec);
				}
			}
		}
		prec.setParent(f.getParent());
		
		result = prec;
		
		ArrayList<Function> results = new ArrayList<>();
		results.add(result);
		return results;
	}
	
	private static ArrayList<Function> getSumElements(Function sum) {
		ArrayList<Function> elements = new ArrayList<>();
		while (sum instanceof Sum || sum instanceof Subtraction) {
			if (sum instanceof Sum) {
				elements.add(((FunctionTwoValues) sum).getVariable2());
			} else {
				elements.add(new Negative(null, ((FunctionTwoValues) sum).getVariable2()));
			}
			sum = ((FunctionTwoValues) sum).getVariable1();
		}
		elements.add(sum);
		return elements;
	}
	
	private static int[] getFirstWorkingSumCouple(ArrayList<Function> elements) {
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
						testFunc = new Subtraction(null, a, ((Negative)b).getVariable());
					} else if (b instanceof Number && ((Number) b).getTerm().compareTo(BigDecimal.ZERO) < 0) {
						testFunc = new Subtraction(null, a, ((Number)b).multiply(new Number(null, -1)));
					} else if (a instanceof Negative) {
						testFunc = new Subtraction(null, b, ((Negative)a).getVariable());
					} else if (a instanceof Number && ((Number) a).getTerm().compareTo(BigDecimal.ZERO) < 0) {
						testFunc = new Subtraction(null, b, ((Number)a).multiply(new Number(null, -1)));
					} else {
						testFunc = new Sum(null, a, b);
					}
					if (!testFunc.isSolved()) {
						return new int[]{i, j};
					}
				}
			}
		}
		return null;
	}

}

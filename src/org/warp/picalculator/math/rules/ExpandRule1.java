package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.FunctionTwoValues;
import org.warp.picalculator.math.functions.Negative;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.functions.SumSubtraction;

/**
 * Expand rule<br>
 * <b>-(+a+b) = -a-b</b><br>
 * <b>-(+a-b) = -a+b</b>
 * 
 * @author Andrea Cavalli
 *
 */
public class ExpandRule1 {

	public static boolean compare(Function f) {
		if (f instanceof Negative) {
			final Negative fnc = (Negative) f;
			if (fnc.getVariable() instanceof Expression) {
				final Expression expr = (Expression) fnc.getVariable();
				if (expr.getVariablesLength() == 1) {
					if (expr.getVariable(0) instanceof Sum) {
						return true;
					} else if (expr.getVariable(0) instanceof Subtraction) {
						return true;
					} else if (expr.getVariable(0) instanceof SumSubtraction) {
						return true;
					}
				}
			}
		} else if (f instanceof Subtraction || f instanceof SumSubtraction) {
			final FunctionTwoValues fnc = (FunctionTwoValues) f;
			if (fnc.getVariable2() instanceof Expression) {
				final Expression expr = (Expression) fnc.getVariable2();
				if (expr.getVariablesLength() == 1) {
					if (expr.getVariable(0) instanceof Sum) {
						return true;
					} else if (expr.getVariable(0) instanceof Subtraction) {
						return true;
					} else if (expr.getVariable(0) instanceof SumSubtraction) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static ArrayList<Function> execute(Function f) throws Error {
		final ArrayList<Function> result = new ArrayList<>();
		final Calculator root = f.getRoot();

		Expression expr = null;
		int fromSubtraction = 0;
		FunctionTwoValues subtraction = null;
		if (f instanceof Negative) {
			expr = ((Expression) ((Negative) f).getVariable());
		} else if (f instanceof Subtraction || f instanceof SumSubtraction) {
			expr = ((Expression) ((FunctionTwoValues) f).getVariable2());
			if (f instanceof Subtraction) {
				fromSubtraction = 1;
			} else {
				fromSubtraction = 2;
			}
		}

		if (f instanceof SumSubtraction) {

		}

		final Function fnc = expr.getVariable(0);
		if (fnc instanceof Sum) {
			final Function a = ((Sum) fnc).getVariable1();
			final Function b = ((Sum) fnc).getVariable2();
			final Subtraction fnc2 = new Subtraction(root, null, b);
			fnc2.setVariable1(new Negative(root, a));
			if (fromSubtraction > 0) {
				subtraction = new Subtraction(root, null, null);
				subtraction.setVariable1(((FunctionTwoValues) f).getVariable1());
				subtraction.setVariable2(fnc2);
				result.add(subtraction);
			} else {
				result.add(fnc2);
			}
		} else if (fnc instanceof Subtraction) {
			final Function a = ((Subtraction) fnc).getVariable1();
			final Function b = ((Subtraction) fnc).getVariable2();
			final Sum fnc2 = new Sum(root, null, b);
			fnc2.setVariable1(new Negative(root, a));
			if (fromSubtraction > 0) {
				subtraction = new Subtraction(root, null, null);
				subtraction.setVariable1(((FunctionTwoValues) f).getVariable1());
				subtraction.setVariable2(fnc2);
				result.add(subtraction);
			} else {
				result.add(fnc2);
			}
		} else if (fnc instanceof SumSubtraction) {
			final Function a = ((SumSubtraction) fnc).getVariable1();
			final Function b = ((SumSubtraction) fnc).getVariable2();
			final Sum fnc2 = new Sum(root, null, b);
			fnc2.setVariable1(new Negative(root, a));
			final Subtraction fnc3 = new Subtraction(root, null, b);
			fnc3.setVariable1(new Negative(root, a));
			if (fromSubtraction > 0) {
				subtraction = new SumSubtraction(root, null, null);
				subtraction.setVariable1(((FunctionTwoValues) f).getVariable1());
				subtraction.setVariable2(fnc2);
				result.add(subtraction);
				subtraction = new SumSubtraction(root, null, null);
				subtraction.setVariable1(((FunctionTwoValues) f).getVariable1());
				subtraction.setVariable2(fnc3);
				result.add(subtraction);
				result.add(subtraction);
			} else {
				result.add(fnc2);
				result.add(fnc2);
			}
		}
		return result;
	}

}

package org.warp.picalculator.math.rules;

import java.util.ArrayList;

import org.warp.picalculator.Error;
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
 * @author Andrea Cavalli
 *
 */
public class ExpandRule1 {

	public static boolean compare(Function f) {
		if (f instanceof Negative) {
			Negative fnc = (Negative) f;
			if (fnc.getVariable() instanceof Expression) {
				Expression expr = (Expression) fnc.getVariable();
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
			FunctionTwoValues fnc = (FunctionTwoValues) f;
			if (fnc.getVariable2() instanceof Expression) {
				Expression expr = (Expression) fnc.getVariable2();
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
		ArrayList<Function> result = new ArrayList<>();
		
		Expression expr = null;
		int fromSubtraction = 0;
		FunctionTwoValues subtraction = null;
		if (f instanceof Negative) {
			expr = ((Expression)((Negative) f).getVariable());
		} else if (f instanceof Subtraction || f instanceof SumSubtraction) {
			expr = ((Expression)((FunctionTwoValues) f).getVariable2());
			if (f instanceof Subtraction) {
				fromSubtraction = 1;
			} else {
				fromSubtraction = 2;
			}
		}
		
		if (f instanceof SumSubtraction) {
			
		}

		Function fnc = expr.getVariable(0);
		if (fnc instanceof Sum) {
			Function a = ((Sum) fnc).getVariable1();
			Function b = ((Sum) fnc).getVariable2();
			Subtraction fnc2 = new Subtraction(f.getParent(), null, b);
			fnc2.setVariable1(new Negative(fnc2, a));
			if (fromSubtraction > 0) {
				subtraction = new Subtraction(f.getParent(), null, null);
				subtraction.setVariable1(((FunctionTwoValues)f).getVariable1());
				subtraction.setVariable2(fnc2);
				result.add(subtraction);
			} else {
				result.add(fnc2);
			}
		} else if (fnc instanceof Subtraction) {
			Function a = ((Subtraction) fnc).getVariable1();
			Function b = ((Subtraction) fnc).getVariable2();
			Sum fnc2 = new Sum(((Negative) f).getParent(), null, b);
			fnc2.setVariable1(new Negative(fnc2, a));
			if (fromSubtraction > 0) {
				subtraction = new Subtraction(f.getParent(), null, null);
				subtraction.setVariable1(((FunctionTwoValues)f).getVariable1());
				subtraction.setVariable2(fnc2);
				result.add(subtraction);
			} else {
				result.add(fnc2);
			}
		} else if (fnc instanceof SumSubtraction) {
			Function a = ((SumSubtraction) fnc).getVariable1();
			Function b = ((SumSubtraction) fnc).getVariable2();
			Sum fnc2 = new Sum(f.getParent(), null, b);
			fnc2.setVariable1(new Negative(fnc2, a));
			Subtraction fnc3 = new Subtraction(f.getParent(), null, b);
			fnc3.setVariable1(new Negative(fnc2, a));
			if (fromSubtraction > 0) {
				subtraction = new SumSubtraction(f.getParent(), null, null);
				subtraction.setVariable1(((FunctionTwoValues)f).getVariable1());
				subtraction.setVariable2(fnc2);
				result.add(subtraction);
				subtraction = new SumSubtraction(f.getParent(), null, null);
				subtraction.setVariable1(((FunctionTwoValues)f).getVariable1());
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

package rules;
/*
SETTINGS: (please don't move this part)
 PATH=NumberRule5
*/

import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.FunctionDynamic;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathContext;
//Imports

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.functions.SumSubtraction;
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Number rule
 * a + 0 = a
 * 0 + a = a
 * a - 0 = a
 * 0 - a = -a
 * a ± 0 = a
 * 
 * @author Andrea Cavalli
 *
 */
public class NumberRule5 implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "NumberRule5";
	}

	// Rule type
	@Override
	public RuleType getRuleType() {
		return RuleType.CALCULATION;
	}

	/* Rule function
	   Returns:
	     - null if it's not executable on the function "f"
	     - An ObjectArrayList<Function> if it did something
	*/

	@Override
	public ObjectArrayList<Function> execute(Function f) {
		boolean isExecutable = false;
		if (f instanceof Sum || f instanceof Subtraction || f instanceof SumSubtraction) {
			MathContext root = f.getMathContext();
			FunctionOperator fnc = (FunctionOperator) f;
			if (fnc.getParameter1().equals(new Number(root, 0)) || fnc.getParameter2().equals(new Number(root, 0))) {
				if (!(fnc.getParameter1().equals(new Number(root, 0)) && f instanceof SumSubtraction)) {
					isExecutable = true;
				}
			}
		}

		if (isExecutable) {
			MathContext root = f.getMathContext();
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			FunctionOperator fnc = (FunctionOperator) f;
			Function a = fnc.getParameter1();
			if (a.equals(new Number(root, 0))) {
				if (f instanceof Subtraction) {
					a = new Multiplication(root, new Number(root, -1), fnc.getParameter2());
				} else {
					a = fnc.getParameter2();
				}
			}
			result.add(a);
			return result;
		} else {
			return null;
		}
	}
}

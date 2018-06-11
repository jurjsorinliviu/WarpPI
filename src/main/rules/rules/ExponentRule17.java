package rules;
/*
SETTINGS: (please don't move this part)
 PATH=ExponentRule17
*/

import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.FunctionDynamic;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathContext;
//Imports


import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;
import org.warp.picalculator.math.functions.Root;
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Exponent rule
 * a√x=x^1/a
 * 
 * @author Andrea Cavalli
 *
 */
public class ExponentRule17 implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "ExponentRule17";
	}

	// Rule type
	@Override
	public RuleType getRuleType() {
		return RuleType.EXPANSION;
	}

	/* Rule function
	   Returns:
	     - null if it's not executable on the function "f"
	     - An ObjectArrayList<Function> if it did something
	*/

	@Override
	public ObjectArrayList<Function> execute(Function f) {
		boolean isExecutable = false;
		if (f instanceof Root) {
			FunctionOperator fnc = (FunctionOperator) f;
			if (fnc.getParameter1().equals(fnc.getParameter2())) {
				isExecutable = true;
			}
		}
	
		if (isExecutable) {
			MathContext root = f.getMathContext();
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			FunctionOperator fnc = (FunctionOperator) f;
			Function a = fnc.getParameter1();
			Function two = new Number(root, 2);
			Function p = new Power(root, a, two);
			result.add(p);
			return result;
		} else {
			return null;
		}
	}
}

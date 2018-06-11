package rules;
/*
SETTINGS: (please don't move this part)
 PATH=ExponentRule8
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
import org.warp.picalculator.math.functions.Power;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Exponent rule
 * (a) ^ (b+c) = a ^ b + a ^ c
 * 
 * @author Andrea Cavalli
 *
 */
public class ExponentRule8 implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "ExponentRule8";
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
		if (f instanceof Power) {
			FunctionOperator fnc = (FunctionOperator) f;
			if (fnc.getParameter2() instanceof Sum) {
				Sum sum = (Sum) fnc.getParameter2();
				MathContext root = f.getMathContext();
				ObjectArrayList<Function> result = new ObjectArrayList<>();
				Function a = fnc.getParameter1();
				Function b = sum.getParameter1();
				Function c = sum.getParameter2();
				result.add(new Multiplication(root, new Power(root, a, b), new Power(root, a, c)));
				return result;
			}
		}
	
		return null;
	}
}

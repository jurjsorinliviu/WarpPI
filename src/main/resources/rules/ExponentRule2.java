/*
SETTINGS: (please don't move this part)
 PATH=ExponentRule2
*/

import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.FunctionDynamic;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathContext;
//Imports


import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Exponent rule
 * a^1=a
 * 
 * @author Andrea Cavalli
 *
 */
public class ExponentRule2 implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "ExponentRule2";
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
		if (f instanceof Power) {
			FunctionOperator fnc = (FunctionOperator) f;
			if (fnc.getParameter2().equals(new Number(f.getMathContext(), 1))) {
				isExecutable = true;
			}
		}
	
		if (isExecutable) {
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			result.add(((FunctionOperator)f).getParameter1());
			return result;
		} else {
			return null;
		}
	}
}

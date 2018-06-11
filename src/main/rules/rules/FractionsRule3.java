package rules;
/*
SETTINGS: (please don't move this part)
 PATH=FractionsRule3
*/

import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.FunctionDynamic;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathContext;
//Imports


import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Fractions rule
 * a / a = 1
 * 
 * @author Andrea Cavalli
 *
 */
public class FractionsRule3 implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "FractionsRule3";
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
		if (f instanceof Division) {
			FunctionOperator fnc = (FunctionOperator) f;
			if (fnc.getParameter1().equals(fnc.getParameter2())) {
				isExecutable = true;
			}
		}
	
		if (isExecutable) {
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			result.add(new Number(f.getMathContext(), 1));
			return result;
		} else {
			return null;
		}
	}
}

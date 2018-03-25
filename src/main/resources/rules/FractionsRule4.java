/*
SETTINGS: (please don't move this part)
 PATH=FractionsRule4
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
import org.warp.picalculator.math.functions.Power;
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Fractions rule
 * (a / b) ^ -1 = b / a
 * 
 * @author Andrea Cavalli
 *
 */
public class FractionsRule4 implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "FractionsRule4";
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
			if (fnc.getParameter1() instanceof Division && fnc.getParameter2() instanceof Number) {
				Function n2 = fnc.getParameter2();
				if (n2.equals(new Number(f.getMathContext(), -1))) {
					isExecutable = true;
				}
			}
		}
	
		if (isExecutable) {
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			FunctionOperator fnc = (FunctionOperator) f;
			Function a = ((FunctionOperator) fnc.getParameter1()).getParameter1();
			Function b = ((FunctionOperator) fnc.getParameter1()).getParameter2();
			Function res = new Division(f.getMathContext(), b, a);
			result.add(res);
			return result;
		} else {
			return null;
		}
	}
}

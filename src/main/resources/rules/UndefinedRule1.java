/*
SETTINGS: (please don't move this part)
 PATH=__INSERT_PACKAGE_WITH_CLASS_NAME__
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
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;
import org.warp.picalculator.math.functions.Undefined;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Undefined rule
 * 0^0=undefined
 * 
 * @author Andrea Cavalli
 *
 */
public class __INSERT_CLASS_NAME__ implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "UndefinedRule1";
	}

	// Rule type
	@Override
	public RuleType getRuleType() {
		return RuleType.EXISTENCE;
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
			var root = f.getMathContext();
			Function fnc = f;
			if (fnc.getParameter1().equals(new Number(root, 0)) && fnc.getParameter2().equals(new Number(root, 0))) {
				isExecutable = true;
			}
		}
	
		if (isExecutable) {
			var root = f.getMathContext();
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			result.add(new Undefined(root));
			return result;
		} else {
			return null;
		}
	}
}

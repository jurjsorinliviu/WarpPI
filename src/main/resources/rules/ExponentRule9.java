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
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Power;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Exponent rule
 * (a ^ b) ^ c = a ^ (b * c)
 * 
 * @author Andrea Cavalli
 *
 */
public class __INSERT_CLASS_NAME__ implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "ExponentRule9";
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
			Function fnc = f;
			if (fnc.getParameter1() instanceof Power) {
				isExecutable = true;
			}
		}
	
		if (isExecutable) {
		var root = f.getMathContext();
		ObjectArrayList<Function> result = new ObjectArrayList<>();
		var powC = f;
		var powB = powC.getParameter1();
		var p = new Power(root, powB.getParameter1(), new Multiplication(root, powB.getParameter2(), powC.getParameter2()));
		result.add(p);
		return result;
		} else {
			return null;
		}
	}
}

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
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Exponent rule
 * (a*b)^n=a^n*b^n
 * 
 * @author Andrea Cavalli
 *
 */
public class __INSERT_CLASS_NAME__ implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "ExponentRule4";
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
			if (fnc.getParameter1() instanceof Multiplication && fnc.getParameter2() instanceof Number) {
				isExecutable = true;
			}
		}
	
		if (isExecutable) {
		var root = f.getMathContext();
		ObjectArrayList<Function> result = new ObjectArrayList<>();
		Function fnc = f;
		var mult = fnc.getParameter1();
		var a = mult.getParameter1();
		var b = mult.getParameter2();
		var n = fnc.getParameter2();
		var p1 = new Power(root, a, n);
		var p2 = new Power(root, b, n);
		var retMult = new Multiplication(root, p1, p2);
		result.add(retMult);
		return result;
		} else {
			return null;
		}
	}
}

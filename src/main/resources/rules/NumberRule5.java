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
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Number;

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
public class __INSERT_CLASS_NAME__ implements Rule {
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
			var root = f.getMathContext();
			Function fnc = f;
			if (fnc.getParameter1().equals(new Number(root, 0)) || fnc.getParameter2().equals(new Number(root, 0))) {
				if (!(fnc.getParameter1().equals(new Number(root, 0)) && f instanceof SumSubtraction)) {
					isExecutable = true;
				}
			}
		}
	
		if (isExecutable) {
			var root = f.getMathContext();
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			Function fnc = f;
			var a = fnc.getParameter1();
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

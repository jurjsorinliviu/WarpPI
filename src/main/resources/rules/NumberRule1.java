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
import org.warp.picalculator.math.functions.Number;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Number rule
 * a * 0 = 0
 * 
 * @author Andrea Cavalli
 *
 */
public class __INSERT_CLASS_NAME__ implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "NumberRule1";
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
		if (f instanceof Multiplication) {
			var root = f.getMathContext();
			var mult = f;
			if (mult.getParameter1() instanceof Number) {
				var numb = mult.getParameter1();
				if (numb.equals(new Number(root, 0))) {
					isExecutable = true;
				}
			}
			if (mult.getParameter2() instanceof Number) {
				var numb = mult.getParameter2();
				if (numb.equals(new Number(root, 0))) {
					isExecutable = true;
				}
			}
		}
	
		if (isExecutable) {
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			result.add(new Number(f.getMathContext(), "0"));
			return result;
		} else {
			return null;
		}
	}
}

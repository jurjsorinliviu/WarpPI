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
import org.warp.picalculator.math.functions.Negative;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.functions.SumSubtraction;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Number rule
 * a - a = 0
 * -a + a = 0
 * a ± a = {0, 2a}
 * 
 * @author Andrea Cavalli
 *
 */
public class __INSERT_CLASS_NAME__ implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "NumberRule3";
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
		if (f instanceof Subtraction) {
			var sub = f;
			if (sub.getParameter1().equals(sub.getParameter2())) {
				isExecutable = true;
			}
		} else if (f instanceof Sum) {
			var sub = f;
			if (sub.getParameter1() instanceof Multiplication) {
				if (sub.getParameter1().getParameter1() instanceof Number && sub.getParameter1().getParameter1().equals(new Number(f.getMathContext(), -1))) {
					var neg = sub.getParameter1().getParameter2();
					if (neg.equals(sub.getParameter2())) {
						isExecutable = true;
					}
				}
			}
		} else if (f instanceof SumSubtraction) {
			var sub = f;
			if (sub.getParameter1().equals(sub.getParameter2())) {
				isExecutable = true;
			}
		}
	
		if (isExecutable) {
			var root = f.getMathContext();
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			if (f instanceof SumSubtraction) {
				var mul = new Multiplication(root, new Number(root, 2), f.getParameter1());
				result.add(mul);
			}
			result.add(new Number(root, 0));
			return result;
		} else {
			return null;
		}
	}
}

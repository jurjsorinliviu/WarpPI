/*
SETTINGS: (please don't move this part)
 PATH=NumberRule1
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
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Number rule
 * a * 0 = 0
 * 
 * @author Andrea Cavalli
 *
 */
public class NumberRule1 implements Rule {
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
			MathContext root = f.getMathContext();
			FunctionOperator mult = (FunctionOperator) f;
			if (mult.getParameter1() instanceof Number) {
				Function numb = mult.getParameter1();
				if (numb.equals(new Number(root, 0))) {
					isExecutable = true;
				}
			}
			if (mult.getParameter2() instanceof Number) {
				Function numb = mult.getParameter2();
				if (numb.equals(new Number(root, 0))) {
					isExecutable = true;
				}
			}
		}
	
		if (isExecutable) {
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			result.add(new Number(f.getMathContext(), 0));
			return result;
		} else {
			return null;
		}
	}
}

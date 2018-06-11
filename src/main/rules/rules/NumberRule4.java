package rules;
/*
SETTINGS: (please don't move this part)
 PATH=NumberRule4
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
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.functions.SumSubtraction;
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Number rule
 * a ± b = {a+b, a-b}
 * 
 * @author Andrea Cavalli
 *
 */
public class NumberRule4 implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "NumberRule4";
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
		if (f instanceof SumSubtraction) {
			isExecutable = true;
		}

		if (isExecutable) {
			MathContext root = f.getMathContext();
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			FunctionOperator ss = (FunctionOperator) f;
			result.add(new Sum(root, ss.getParameter1(), ss.getParameter2()));
			result.add(new Subtraction(root, ss.getParameter1(), ss.getParameter2()));
			return result;
		} else {
			return null;
		}
	}
}

package rules;
/*
SETTINGS: (please don't move this part)
 PATH=NumberRule7
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
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Number rule
 * a + a = 2a
 * 
 * @author Andrea Cavalli
 *
 */
public class NumberRule7 implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "NumberRule7";
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
		if (f instanceof Sum) {
			isExecutable = ((FunctionOperator) f).getParameter1().equals(((FunctionOperator) f).getParameter2());
		}

		if (isExecutable) {
			MathContext root = f.getMathContext();
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			Function mult = new Multiplication(root, new Number(root, 2), ((FunctionOperator) f).getParameter1());
			result.add(mult);
			return result;
		} else {
			return null;
		}
	}
}

package rules.functions;
/*
SETTINGS: (please don't move this part)
 PATH=functions.SumRule
*/

import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.FunctionDynamic;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathContext;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.warp.picalculator.ScriptUtils;
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;
import org.warp.picalculator.math.rules.RulesManager;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.SumSubtraction;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Division;

/**
 * Sum
 * a+b = c
 * 
 * @author Andrea Cavalli
 *
 */
public class SumRule implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "Sum";
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
		if (f instanceof Sum) {
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			Function variable1 = ((FunctionOperator) f).getParameter1();
			Function variable2 = ((FunctionOperator) f).getParameter2();
			MathContext mathContext = f.getMathContext();
			if (variable1 instanceof Number && variable2 instanceof Number) {
				//a+b = c
				result.add(((Number) variable1).add(((Number) variable2)));
				return result;
			}
		}
		return null;
	}
}

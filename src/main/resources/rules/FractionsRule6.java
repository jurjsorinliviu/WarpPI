/*
SETTINGS: (please don't move this part)
 PATH=FractionsRule6
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
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Number rule
 * a ^ -1 = 1/a
 * 
 * @author Andrea Cavalli
 *
 */
public class FractionsRule6 implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "FractionsRule6";
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
			MathContext root = f.getMathContext();
			Power pow = (Power) f;
			if (pow.getParameter2() instanceof Number) {
				Function numb = pow.getParameter2();
				if (numb.equals(new Number(root, -1))) {
					isExecutable = true;
				}
			}
		}
	
		if (isExecutable) {
			MathContext root = f.getMathContext();
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			Function a = new Division(root, new Number(root, 1), ((Power) f).getParameter1());
			result.add(a);
			return result;
		} else {
			return null;
		}
	}
}

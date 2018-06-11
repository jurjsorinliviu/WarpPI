package rules;
/*
SETTINGS: (please don't move this part)
 PATH=FractionsRule12
*/

import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.FunctionDynamic;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathContext;
//Imports


import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Fractions rule
 * (b / c) / a = b / (a * c)
 * 
 * @author Andrea Cavalli
 *
 */
public class FractionsRule12 implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "FractionsRule12";
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
		if (f instanceof Division) {
			FunctionOperator fnc = (FunctionOperator) f;
			Function a;
			Function c;
			if (fnc.getParameter1() instanceof Division) {
				FunctionOperator div2 = (FunctionOperator) fnc.getParameter1();
				a = fnc.getParameter1();
				c = div2.getParameter2();
				isExecutable = true;
			}
		}
	
		if (isExecutable) {
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			FunctionOperator fnc = (FunctionOperator) f;
			Function a;
			Function b;
			Function c;
	
			FunctionOperator div2 = (FunctionOperator) fnc.getParameter1();
			a = fnc.getParameter2();
			b = div2.getParameter1();
			c = div2.getParameter2();
			result.add(new Division(fnc.getMathContext(), b, new Multiplication(fnc.getMathContext(), c, a)));
	
			return result;
		} else {
			return null;
		}
	}
}

/*
SETTINGS: (please don't move this part)
 PATH=ExponentRule16
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
import org.warp.picalculator.math.functions.Power;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Exponent rule
 * (a ^ b) * (a ^ c) = a ^ (b + c)
 * 
 * @author Andrea Cavalli
 *
 */
public class ExponentRule16 implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "ExponentRule16";
	}

	// Rule type
	@Override
	public RuleType getRuleType() {
		return RuleType.REDUCTION;
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
			FunctionOperator fnc = (FunctionOperator) f;
			if (fnc.getParameter1() instanceof Power && fnc.getParameter2() instanceof Power) {
				isExecutable = ((FunctionOperator)fnc.getParameter1()).getParameter1().equals(((FunctionOperator)fnc.getParameter2()).getParameter1());
			} else if (fnc.getParameter1() instanceof Power) {
				isExecutable = ((FunctionOperator)fnc.getParameter1()).getParameter1().equals(fnc.getParameter2());
			} else if (fnc.getParameter2() instanceof Power) {
				isExecutable = ((FunctionOperator)fnc.getParameter2()).getParameter1().equals(fnc.getParameter1());
			}
		}
	
		if (isExecutable) {
			MathContext root = f.getMathContext();
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			FunctionOperator fnc = (FunctionOperator) f;
			if (fnc.getParameter1() instanceof Power && fnc.getParameter2() instanceof Power) {
				result.add(new Power(root, ((FunctionOperator)fnc.getParameter1()).getParameter1(), new Sum(root, ((FunctionOperator)fnc.getParameter1()).getParameter2(), ((FunctionOperator)fnc.getParameter2()).getParameter2())));
			} else if (fnc.getParameter1() instanceof Power) {
				result.add(new Power(root, fnc.getParameter2(), new Sum(root, ((FunctionOperator)fnc.getParameter1()).getParameter2(), new Number(root, 1))));
			} else if (fnc.getParameter2() instanceof Power) {
				result.add(new Power(root, fnc.getParameter1(), new Sum(root, new Number(root, 1), ((FunctionOperator)fnc.getParameter2()).getParameter2())));
			}
			return result;
		} else {
			return null;
		}
	}
}

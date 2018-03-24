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
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Variable rule
 * ax+x=(a+1)*x (a,b NUMBER; x VARIABLES)
 * 
 * @author Andrea Cavalli
 *
 */
public class __INSERT_CLASS_NAME__ implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "VariableRule2";
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
		Function fnc = f;
		if (f instanceof Sum || f instanceof Subtraction) {
			if (fnc.getParameter1() instanceof Multiplication) {
				var m1 = fnc.getParameter1();
				if (m1.getParameter2().equals(fnc.getParameter2())) {
					isExecutable = true;
				}
			}
		}
		
		if (isExecutable) {
			var root = fnc.getMathContext();
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			var m1 = fnc.getParameter1();
			var a = m1.getParameter1();
			var x = fnc.getParameter2();
	
			var rets;
			if (fnc instanceof Sum) {
				rets = new Sum(root, a, new Number(root, 1));
			} else {
				rets = new Subtraction(root, a, new Number(root, 1));
			}
			var retm = new Multiplication(root, rets, x);
			result.add(retm);
			return result;
		} else {
			return null;
		}
	}
}

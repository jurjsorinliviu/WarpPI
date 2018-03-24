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
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Variable rule
 * ax+bx=(a+b)*x (a,b NUMBER; x VARIABLE|MULTIPLICATION)
 * 
 * @author Andrea Cavalli
 *
 */
public class __INSERT_CLASS_NAME__ implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "VariableRule1";
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
		if (f instanceof Subtraction || f instanceof Sum) {
			if (fnc.getParameter1() instanceof Multiplication & fnc.getParameter2() instanceof Multiplication) {
				var m1 = fnc.getParameter1();
				var m2 = fnc.getParameter2();
				if (m1.getParameter1().equals(m2.getParameter1()) || m1.getParameter2().equals(m2.getParameter2())) {
					isExecutable = true;
				}
			}
		}
		
		if (isExecutable) {
			var root = fnc.getMathContext();
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			var m1 = fnc.getParameter1();
			var m2 = fnc.getParameter2();
			var a;
			var b;
			var x;
			if (m1.getParameter2().equals(m2.getParameter2())) {
				x = m1.getParameter2();
				a = m1.getParameter1();
				b = m2.getParameter1();
			} else {
				x = m1.getParameter1();
				a = m1.getParameter2();
				b = m2.getParameter2();
			}
	
			var rets;
			if (fnc instanceof Sum) {
				rets = new Sum(root, a, b);
			} else {
				rets = new Subtraction(root, a, b);
			}
			var retm = new Multiplication(root, rets, x);
			result.add(retm);
			return result;
		} else {
			return null;
		}
	}
}

package rules;
/*
SETTINGS: (please don't move this part)
 PATH=VariableRule1
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
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Variable rule
 * ax+bx=(a+b)*x (a,b NUMBER; x VARIABLE|MULTIPLICATION)
 * 
 * @author Andrea Cavalli
 *
 */
public class VariableRule1 implements Rule {
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
		if (f instanceof Subtraction || f instanceof Sum) {
			FunctionOperator fnc = (FunctionOperator) f;
			if (fnc.getParameter1() instanceof Multiplication & fnc.getParameter2() instanceof Multiplication) {
				FunctionOperator m1 = (FunctionOperator) fnc.getParameter1();
				FunctionOperator m2 = (FunctionOperator) fnc.getParameter2();
				if (m1.getParameter1().equals(m2.getParameter1()) || m1.getParameter2().equals(m2.getParameter2())) {
					isExecutable = true;
				}
			}
		}

		if (isExecutable) {
			FunctionOperator fnc = (FunctionOperator) f;
			MathContext root = fnc.getMathContext();
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			FunctionOperator m1 = (FunctionOperator) fnc.getParameter1();
			FunctionOperator m2 = (FunctionOperator) fnc.getParameter2();
			Function a;
			Function b;
			Function x;
			if (m1.getParameter2().equals(m2.getParameter2())) {
				x = m1.getParameter2();
				a = m1.getParameter1();
				b = m2.getParameter1();
			} else {
				x = m1.getParameter1();
				a = m1.getParameter2();
				b = m2.getParameter2();
			}

			Function rets;
			if (fnc instanceof Sum) {
				rets = new Sum(root, a, b);
			} else {
				rets = new Subtraction(root, a, b);
			}
			Function retm = new Multiplication(root, rets, x);
			result.add(retm);
			return result;
		} else {
			return null;
		}
	}
}

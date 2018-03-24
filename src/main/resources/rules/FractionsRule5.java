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


import java.math.BigDecimal;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Fractions rule
 * (a / b) ^ -c = (b / a) ^ c
 * 
 * @author Andrea Cavalli
 *
 */
public class __INSERT_CLASS_NAME__ implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "FractionsRule5";
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
			Function fnc = f;
			if (fnc.getParameter1() instanceof Division) {
				if (fnc.getParameter2() instanceof Multiplication && fnc.getParameter2().getParameter1().equals(new Number(f.getMathContext(), -1))) {
					isExecutable = true;
				} else if (fnc.getParameter2() instanceof Number) {
					var n2 = fnc.getParameter2();
					if (n2.getTerm().compareTo(BigDecimal.ZERO) < 0) {
						isExecutable = true;
					}
				}
			}
		}
	
		if (isExecutable) {
			var root = f.getMathContext();
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			Function fnc = f;
			var a = (fnc.getParameter1()).getParameter1();
			var b = (fnc.getParameter1()).getParameter2();
			var c;
			if (fnc.getParameter2() instanceof Multiplication) {
				c = fnc.getParameter2().getParameter2();
			} else {
				c = fnc.getParameter2().multiply(new Number(root, "-1"));
			}
			var dv = new Division(root, b, a);
			var pow = new Power(root, dv, c);
			result.add(pow);
			return result;
		} else {
			return null;
		}
	}
}

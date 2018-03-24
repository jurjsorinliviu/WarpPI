/*
SETTINGS: (please don't move this part)
 PATH=__INSERT_PACKAGE_WITH_CLASS_NAME__
*/

import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.FunctionDynamic;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathContext;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Negative;
import org.warp.picalculator.math.functions.Subtraction;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Expand rule
 * -(-a) = a
 * 
 * @author Andrea Cavalli
 *
 */
public class __INSERT_CLASS_NAME__ implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "ExpandRule5";
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
		if (f instanceof Negative) {
			isExecutable = f.getParameter() instanceof Negative;
		} else if (f instanceof Multiplication) {
			if (f.getParameter1().equals(new Number(f.getMathContext(), -1)) && f.getParameter2() instanceof Multiplication) {
				isExecutable = f.getParameter2().getParameter1().equals(f.getParameter1());
			}
		}
	
		if (isExecutable) {
			ObjectArrayList<Function> result = new ObjectArrayList<>();
	
			if (f instanceof Negative) {
				Function fnc = f;
				result.add(((fnc.getParameter()).getParameter()).getParameter());
			} else if (f instanceof Multiplication) {
				Function fnc = f;
				result.add(fnc.getParameter2().getParameter2());
			}
			return result;
		} else {
			return null;
		}
	}

}

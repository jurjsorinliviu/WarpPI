/*
SETTINGS: (please don't move this part)
 PATH=__INSERT_PACKAGE_WITH_CLASS_NAME__
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
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Negative;
import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import java.lang.NullPointerException;
import java.lang.NumberFormatException;
import java.lang.ArithmeticException;

/**
 * Negative
 * -a = b
 * 
 * @author Andrea Cavalli
 *
 */
public class __INSERT_CLASS_NAME__ implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "Negative";
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
		if (f instanceof Negative) {
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			var variable = f.getParameter();
			var mathContext = f.getMathContext();
			if (variable instanceof Number) {
				//-a = a*-1 = b
				try {
					result.add(variable.multiply(new Number(mathContext, -1)));
				} catch (ex) {
					if (ex instanceof NullPointerException) {
						throw new Error(Errors.ERROR);
					} else if (ex instanceof NumberFormatException) {
						throw new Error(Errors.SYNTAX_ERROR);
					} else if (ex instanceof ArithmeticException) {
						throw new Error(Errors.NUMBER_TOO_SMALL);
					}
				}
				return result;
			}
		}
		return null;
	}
}

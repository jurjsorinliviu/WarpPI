/*
SETTINGS: (please don't move this part)
 PATH=functions.NumberRule
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
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Number;
import java.math.BigInteger;

/**
 * Number
 *
 * 
 * @author Andrea Cavalli
 *
 */
public class NumberRule implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "Number";
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
		if (f instanceof Number) {
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			MathContext mathContext = f.getMathContext();
			if (mathContext.exactMode) {
				if (((Number)f).isInteger() == false) {
					Number divisor = new Number(mathContext, BigInteger.TEN.pow(((Number)f).getNumberOfDecimalPlaces()));
					Function number = new Number(mathContext, ((Number)f).getTerm().multiply(divisor.getTerm()));
					Function div = new Division(mathContext, number, divisor);
					result.add(div);
					return result;
				}
			}
		}
		return null;
	}
}

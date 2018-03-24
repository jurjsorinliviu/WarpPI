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
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.SumSubtraction;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Division;

/**
 * Division
 * a/b = c
 * 
 * @author Andrea Cavalli
 *
 */
public class __INSERT_CLASS_NAME__ implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "Division";
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
		if (f instanceof Division) {
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			var variable1 = f.getParameter1();
			var variable2 = f.getParameter2();
			var mathContext = f.getMathContext();
			if (variable1 instanceof Number && variable2 instanceof Number) {
				if (mathContext.exactMode) {
					if (variable1.isInteger() && variable2.isInteger()) {
						var factors1, factors2, mcm;
						try {
							factors1 = variable1.getFactors();
							factors2 = variable2.getFactors();
							mcm = ScriptUtils.mcm(factors1, factors2);
						} catch (error) {
							return null;
						}
						if (mcm.size() > 0) { //true if there is at least one common factor
							//divide by the common factor (ab/cb = a/c)
							var nmb1 = variable1.term.toBigIntegerExact();
							var nmb2 = variable2.term.toBigIntegerExact();
							mcm.forEach(function(integerNumber) {
								nmb1 = nmb1.divide(integerNumber);
								nmb2 = nmb2.divide(integerNumber);
							});
							result.add(new Division(mathContext, new Number(mathContext, nmb1), new Number(mathContext, nmb2)));
							return result;
						}
					}
				} else {
					//divide a by b (a/b = c)
					result.add(variable1.divide(variable2));
					return result;
				}
			}
		}
		return null;
	}
}

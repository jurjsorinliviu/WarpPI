package rules.functions;
/*
SETTINGS: (please don't move this part)
 PATH=functions.RootRule
*/

import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.FunctionDynamic;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathContext;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
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
import org.warp.picalculator.math.functions.Root;
import org.warp.picalculator.math.functions.RootSquare;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Root
 * a√b = c
 * 
 * @author Andrea Cavalli
 *
 */
public class RootRule implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "Root";
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
	public ObjectArrayList<Function> execute(Function f) throws Error, InterruptedException {
		boolean isSquare = false;
		if ((isSquare = f instanceof RootSquare) || f instanceof Root) {
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			MathContext mathContext = f.getMathContext();
			Function variable1 = ((FunctionOperator) f).getParameter1();
			Function variable2 = ((FunctionOperator) f).getParameter2();
			boolean isSolvable = false, canBePorted = false;
			if (variable1 instanceof Number && variable2 instanceof Number) {
				if (mathContext.exactMode) {
					result.add(((Number) variable1).pow(new Number(mathContext, BigDecimal.ONE).divide(((Number) variable1))));
					return result;
				}
				isSolvable = isSolvable | !mathContext.exactMode;
				if (!isSolvable) {
					try {
						Function resultVar = ((Number) variable2).pow(new Number(mathContext, BigDecimal.ONE).divide(((Number) variable1)));
						Function originalVariable = ((Number) resultVar).pow(new Number(mathContext, 2));
						if ((originalVariable).equals(((FunctionOperator) f).getParameter2())) {
							isSolvable = true;
						}
					} catch (Exception ex) {
						throw (Error) new Error(Errors.ERROR, ex.getMessage()).initCause(ex);
					}
				}
			}
			if (!isSquare && !isSolvable && variable1 instanceof Number && variable1.equals(new Number(mathContext, 2))) {
				canBePorted = true;
			}

			if (isSolvable) {
				result.add(((Number) variable2).pow(new Number(mathContext, BigInteger.ONE).divide((Number) variable1)));
				return result;
			}
			if (canBePorted) {
				result.add(new RootSquare(mathContext, variable2));
			}
		}
		return null;
	}
}

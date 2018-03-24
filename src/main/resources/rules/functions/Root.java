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
import org.warp.picalculator.math.functions.Root;
import org.warp.picalculator.math.functions.RootSquare;
import java.math.BigDecimal;

/**
 * Root
 * a√b = c
 * 
 * @author Andrea Cavalli
 *
 */
public class __INSERT_CLASS_NAME__ implements Rule {
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
	public ObjectArrayList<Function> execute(Function f) {
		var isSquare = false;
		if ((isSquare = f instanceof RootSquare) || f instanceof Root) {
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			var mathContext = f.getMathContext();
			var variable1 = f.getParameter1();
			var variable2 = f.getParameter2();
			var isSolvable = false;
			var canBePorted = false;
			if (variable1 instanceof Number && variable2 instanceof Number) {
				isSolvable = isSolvable|!mathContext.exactMode;
				if (!isSolvable) {
					try {
						var resultVar = variable2.pow(new Number(mathContext, BigDecimal.ONE).divide(variable1));
						var originalVariable = resultVar.pow(new Number(mathContext, 2));
						if (originalVariable.equals(f.getParameter2())) {
							isSolvable = true;
						}
					} catch (ex) {
						ex.printStackTrace();
					}
				}
			}
			if (!isSquare && !isSolvable && variable1 instanceof Number && variable1.equals(new Number(mathContext, 2))) {
				canBePorted = true;
			}
			
			if (isSolvable) {
				result.add(variable2.pow(new Number(mathContext, BigInteger.ONE).divide(variable1)));
				return result;
			}
			if (canBePorted) {
				result.add(new RootSquare(mathContext, variable2));
			}
		}
		return null;
	}
}

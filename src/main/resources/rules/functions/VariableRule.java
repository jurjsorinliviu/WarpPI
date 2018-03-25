/*
SETTINGS: (please don't move this part)
 PATH=functions.VariableRule
*/

import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.FunctionDynamic;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathContext;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.nevec.rjm.BigDecimalMath;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.Error;
import org.warp.picalculator.ScriptUtils;
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;
import org.warp.picalculator.math.rules.RulesManager;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Variable;
import org.warp.picalculator.math.functions.Number;

/**
 * Variable
 * a = n
 * 
 * @author Andrea Cavalli
 *
 */
public class VariableRule implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "Variable";
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
	public ObjectArrayList<Function> execute(Function f) throws Error {
		if (f instanceof Variable) {
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			Character variable = ((Variable)f).getChar();
			MathContext mathContext = f.getMathContext();
			if (mathContext.exactMode == false) {
				if (variable.equals(MathematicalSymbols.PI)) {
					//a = n
					result.add(new Number(mathContext, BigDecimalMath.pi(new java.math.MathContext(Utils.scale, Utils.scaleMode2))));
					return result;
				}
			}
		}
		return null;
	}
}

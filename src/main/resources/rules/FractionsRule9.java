/*
SETTINGS: (please don't move this part)
 PATH=FractionsRule9
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
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Power;
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Number rule
 * (-a)/b = -(a/b)
 * 
 * @author Andrea Cavalli
 *
 */
public class FractionsRule9 implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "FractionsRule9";
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
		if (f instanceof Division) {
			MathContext root = f.getMathContext();
			Division div = (Division) f;
			if (div.getParameter1() instanceof Multiplication && ((Multiplication)div.getParameter1()).isNegative()) {
				ObjectArrayList<Function> result = new ObjectArrayList<>();
				result.add(Multiplication.newNegative(root, new Division(root, ((Multiplication)div.getParameter1()).toPositive(), div.getParameter2())));
				return result;
			}
		}
		
		return null;
	}
}

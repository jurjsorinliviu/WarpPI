/*
SETTINGS: (please don't move this part)
 PATH=FractionsRule7
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
 * a ^ -b = 1/(a^b)
 * 
 * @author Andrea Cavalli
 *
 */
public class FractionsRule7 implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "FractionsRule7";
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
			MathContext root = f.getMathContext();
			Power pow = (Power) f;
			if (pow.getParameter2() instanceof Number) {
				Number numb = (Number) pow.getParameter2();
				if (numb.getTerm().compareTo(BigDecimal.ZERO) < 0) {
					ObjectArrayList<Function> result = new ObjectArrayList<>();
					Function a = new Division(root, new Number(root, 1), new Power(root, ((Power) f).getParameter1(), ((Number)((Power)f).getParameter2()).multiply(new Number(root, -1))));
					result.add(a);
					return result;
				}
			} else if (pow.getParameter2() instanceof Multiplication && ((Multiplication)pow.getParameter2()).getParameter1().equals(new Number(root, -1))) {
				ObjectArrayList<Function> result = new ObjectArrayList<>();
				Function a = new Division(root, new Number(root, 1), new Power(root, ((Power) f).getParameter1(), ((Multiplication)((Power)f).getParameter2()).getParameter2()));
				result.add(a);
				return result;
			}
		}
		
		return null;
	}
}

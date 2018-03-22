/*
SETTINGS: (please don't move this part)
 PATH=FractionsRule2
*/

//Imports
import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Division;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;
import org.warp.picalculator.ScriptUtils;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Fractions rule
 * 0 / a = 0
 * 
 * @author Andrea Cavalli
 *
 */
public class FractionsRule2 implements Rule {

	public static final long serialVersionUID = 11239034890L;
	
	@Override
	public String getRuleName() {
		return "FractionsRule2";
	}

	@Override
	public RuleType getRuleType() {
		return RuleType.CALCULATION;
	}
	
	@Override
	public ObjectArrayList<Function> execute(Function f) {
		boolean isExecutable = false;
		MathContext root = f.getMathContext();
		if (ScriptUtils.instanceOf(f, Division.class)) {
			Division fnc = (Division) f;
			if (ScriptUtils.instanceOf(fnc.getParameter1(), Number.class)) {
				Number numb1 = (Number) fnc.getParameter1();
				if (numb1.equals(new Number(root, 0))) {
					if (ScriptUtils.instanceOf(fnc.getParameter2(), Number.class)) {
						Number numb2 = (Number) fnc.getParameter2();
						if (numb2.equals(new Number(root, 0)) == false) {
							isExecutable = true;
						}
					} else {
						isExecutable = true;
					}
				}
			}
		}
	
		if (isExecutable) {
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			result.add(new Number(f.getMathContext(), 0));
			return result;
		} else {
			return null;
		}
	}
}

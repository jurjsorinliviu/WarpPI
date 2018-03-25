/*
SETTINGS: (please don't move this part)
 PATH=ExpandRule2
*/

import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionOperator;
import org.warp.picalculator.math.MathContext;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.warp.picalculator.math.rules.Rule;
import org.warp.picalculator.math.rules.RuleType;
import org.warp.picalculator.math.functions.Multiplication;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.SumSubtraction;
import org.warp.picalculator.math.functions.Number;

/**
 * Expand rule
 * -(-a) = a
 * 
 * @author Andrea Cavalli
 *
 */
public class ExpandRule2 implements Rule {
	// Rule name
	@Override
	public String getRuleName() {
		return "ExpandRule2";
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
		if (f instanceof Multiplication) {
			FunctionOperator fnc = (FunctionOperator) f;
			if (fnc.getParameter1().equals(new Number(fnc.getMathContext(), -1))) {
				Function expr = fnc.getParameter2();
				if (expr instanceof Sum) {
					isExecutable = true;
				} else if (expr instanceof Subtraction) {
					isExecutable = true;
				} else if (expr instanceof SumSubtraction) {
					isExecutable = true;
				}
			}
		} else if (f instanceof Subtraction || f instanceof SumSubtraction) {
			FunctionOperator fnc = (FunctionOperator) f;
			Function expr = fnc.getParameter2();
			if (expr instanceof Sum) {
				isExecutable = true;
			} else if (expr instanceof Subtraction) {
				isExecutable = true;
			} else if (expr instanceof SumSubtraction) {
				isExecutable = true;
			}
		}
		if (isExecutable) {
			ObjectArrayList<Function> result = new ObjectArrayList<>();
			MathContext root = f.getMathContext();

			Function expr = null;
			int fromSubtraction = 0;
			Function subtraction = null;
			if (f instanceof Multiplication) {
				expr = ((FunctionOperator) f).getParameter2();
			} else if (f instanceof Subtraction || f instanceof SumSubtraction) {
				expr = ((FunctionOperator) f).getParameter2();
				if (f instanceof Subtraction) {
					fromSubtraction = 1;
				} else {
					fromSubtraction = 2;
				}
			}

			if (f instanceof SumSubtraction) {
				
			}

			FunctionOperator fnc = (FunctionOperator) expr;
			if (fnc instanceof Sum) {
				Function a = fnc.getParameter1();
				Function b = fnc.getParameter2();
				Function fnc2 = new Subtraction(root, new Multiplication(root, new Number(root, -1), a), b);
				if (fromSubtraction > 0) {
					subtraction = new Subtraction(root, ((FunctionOperator)f).getParameter1(), fnc2);
					result.add(subtraction);
				} else {
					result.add(fnc2);
				}
			} else if (fnc instanceof Subtraction) {
				Function a = fnc.getParameter1();
				Function b = fnc.getParameter2();
				Function fnc2 = new Sum(root, new Multiplication(root, new Number(root, -1), a), b);
				if (fromSubtraction > 0) {
					subtraction = new Subtraction(root, ((FunctionOperator)f).getParameter1(), fnc2);
					result.add(subtraction);
				} else {
					result.add(fnc2);
				}
			} else if (fnc instanceof SumSubtraction) {
				Function a = fnc.getParameter1();
				Function b = fnc.getParameter2();
				Function fnc2 = new Sum(root, new Multiplication(root, new Number(root, -1), a), b);
				Function fnc3 = new Subtraction(root, new Multiplication(root, new Number(root, -1), a), b);
				if (fromSubtraction > 0) {
					subtraction = new SumSubtraction(root, ((FunctionOperator)f).getParameter1(), fnc2);
					result.add(subtraction);
					subtraction = new SumSubtraction(root, ((FunctionOperator)f).getParameter1(), fnc3);
					result.add(subtraction);
					result.add(subtraction);
				} else {
					result.add(fnc2);
					result.add(fnc2);
				}
			}
			return result;
		} else {
			return null;
		}
		
	}
}

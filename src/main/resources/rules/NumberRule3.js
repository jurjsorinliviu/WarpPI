//Imports


var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var MathContext = org.warp.picalculator.math.MathContext;
var Multiplication = org.warp.picalculator.math.functions.Multiplication;
var Negative = org.warp.picalculator.math.functions.Negative;
var Number = org.warp.picalculator.math.functions.Number;
var Subtraction = org.warp.picalculator.math.functions.Subtraction;
var Sum = org.warp.picalculator.math.functions.Sum;
var SumSubtraction = org.warp.picalculator.math.functions.SumSubtraction;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Number rule
 * a - a = 0
 * -a + a = 0
 * a ± a = {0, 2a}
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "NumberRule3";
	},
	// Rule type
	getRuleType: function() {
		return RuleType.CALCULATION;
	},
	/* Rule function
	   Returns:
	     - null if it's not executable on the function "f"
	     - An ObjectArrayList<Function> if it did something
	*/

	execute: function(f) {
		var isExecutable;
		if (ScriptUtils.instanceOf(f, Subtraction.class)) {
			var sub = f;
			if (sub.getParameter1().equals(sub.getParameter2())) {
				isExecutable = true;
			}
		} else if (ScriptUtils.instanceOf(f, Sum.class)) {
			var sub = f;
			if (ScriptUtils.instanceOf(sub.getParameter1(), Multiplication.class)) {
				if (ScriptUtils.instanceOf(sub.getParameter1().getParameter1(), Number.class) && sub.getParameter1().getParameter1().equals(new Number(f.getMathContext(), -1))) {
					var neg = sub.getParameter1().getParameter2();
					if (neg.equals(sub.getParameter2())) {
						isExecutable = true;
					}
				}
			}
		} else if (ScriptUtils.instanceOf(f, SumSubtraction.class)) {
			var sub = f;
			if (sub.getParameter1().equals(sub.getParameter2())) {
				isExecutable = true;
			}
		}
	
		if (isExecutable) {
			var root = f.getMathContext();
			var result = new ObjectArrayList();
			if (ScriptUtils.instanceOf(f, SumSubtraction.class)) {
				var mul = new Multiplication(root, new Number(root, 2), f.getParameter1());
				result.add(mul);
			}
			result.add(new Number(root, 0));
			return result;
		} else {
			return null;
		}
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
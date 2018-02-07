//Imports


var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var MathContext = org.warp.picalculator.math.MathContext;
var Multiplication = org.warp.picalculator.math.functions.Multiplication;
var Number = org.warp.picalculator.math.functions.Number;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Number rule
 * a * 0 = 0
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "NumberRule1";
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
		if (ScriptUtils.instanceOf(f, Multiplication.class)) {
			var root = f.getMathContext();
			var mult = f;
			if (ScriptUtils.instanceOf(mult.getParameter1(), Number.class)) {
				var numb = mult.getParameter1();
				if (numb.equals(new Number(root, 0))) {
					isExecutable = true;
				}
			}
			if (ScriptUtils.instanceOf(mult.getParameter2(), Number.class)) {
				var numb = mult.getParameter2();
				if (numb.equals(new Number(root, 0))) {
					isExecutable = true;
				}
			}
		}
	
		if (isExecutable) {
			var result = new ObjectArrayList();
			result.add(new Number(f.getMathContext(), "0"));
			return result;
		} else {
			return null;
		}
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
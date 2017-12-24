// Imports
var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");
var ScriptUtils = org.warp.picalculator.ScriptUtils;
var Rule = org.warp.picalculator.math.rules.Rule;
var RuleType = org.warp.picalculator.math.rules.RuleType;
var RulesManager = org.warp.picalculator.math.rules.RulesManager;
var Multiplication = org.warp.picalculator.math.functions.Multiplication;
var Sum = org.warp.picalculator.math.functions.Sum;
var Subtraction = org.warp.picalculator.math.functions.Subtraction;
var SumSubtraction = org.warp.picalculator.math.functions.SumSubtraction;
var Number = org.warp.picalculator.math.functions.Number;
var Division = org.warp.picalculator.math.functions.Division;
var Power = org.warp.picalculator.math.functions.Power;

/**
 * Power
 * a^b = c
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "Power";
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
		if (ScriptUtils.instanceOf(f, Power.class)) {
			var result = new ObjectArrayList();
			var variable1 = f.getParameter1();
			var variable2 = f.getParameter2();
			var mathContext = f.getMathContext();
			if (ScriptUtils.instanceOf(variable1, Number.class) && ScriptUtils.instanceOf(variable2, Number.class)) {
				//a^b = c
				result.add(variable1.pow(variable2));
				return result;
			}
		}
		return null;
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
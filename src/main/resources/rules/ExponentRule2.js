//Imports


var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var Number = org.warp.picalculator.math.functions.Number;
var Power = org.warp.picalculator.math.functions.Power;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Exponent rule
 * a^1=a
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "ExponentRule2";
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
		if (ScriptUtils.instanceOf(f, Power.class)) {
			var fnc = f;
			if (fnc.getParameter2().equals(new Number(f.getMathContext(), 1))) {
				isExecutable = true;
			}
		}
	
		if (isExecutable) {
		var result = new ObjectArrayList();
		result.add((f).getParameter1());
		return result;
		} else {
			return null;
		}
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
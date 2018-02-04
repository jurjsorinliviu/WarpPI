//Imports


var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var MathContext = org.warp.picalculator.math.MathContext;
var Number = org.warp.picalculator.math.functions.Number;
var Power = org.warp.picalculator.math.functions.Power;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Exponent rule
 * 1^a=1
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "ExponentRule1";
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
		var root = f.getMathContext();
		if (ScriptUtils.instanceOf(f, Power.class)) {
			if (f.getParameter1().equals(new Number(root, 1))) {
				isExecutable = true;
			}
		}
	
		if (isExecutable) {
			var root = f.getMathContext();
			var result = new ObjectArrayList();
			result.add(new Number(root, 1));
			return result;
		} else {
			return null;
		}
	}

}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));

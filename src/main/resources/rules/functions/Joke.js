// Imports
var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");
var ScriptUtils = org.warp.picalculator.ScriptUtils;
var Rule = org.warp.picalculator.math.rules.Rule;
var RuleType = org.warp.picalculator.math.rules.RuleType;
var RulesManager = org.warp.picalculator.math.rules.RulesManager;

/**
 * Joke
 *
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "Joke";
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
		return null;
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
// Imports
var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");
var ScriptUtils = org.warp.picalculator.ScriptUtils;
var Rule = org.warp.picalculator.math.rules.Rule;
var RuleType = org.warp.picalculator.math.rules.RuleType;
var RulesManager = org.warp.picalculator.math.rules.RulesManager;
var BigInteger = java.math.BigInteger;

/**
 * Number
 *
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "Number";
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
		if (ScriptUtils.instanceOf(f, Number.class)) {
			var result = new ObjectArrayList();
			var mathContext = f.getMathContext();
			if (mathContext.exactMode) {
				if (f.isInteger() == false) {
					var divisor = new Number(mathContext, BigInteger.TEN.pow(f.getNumberOfDecimalPlaces()));
					var number = new Number(mathContext, f.getTerm().multiply(divisor.term));
					var div = new Division(mathContext, number, divisor);
					result.add(div);
					return result;
				}
			}
		}
		return null;
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
// Imports
var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");
var ScriptUtils = org.warp.picalculator.ScriptUtils;
var Rule = org.warp.picalculator.math.rules.Rule;
var RuleType = org.warp.picalculator.math.rules.RuleType;
var RulesManager = org.warp.picalculator.math.rules.RulesManager;
var Number = org.warp.picalculator.math.functions.Number;
var Negative = org.warp.picalculator.math.functions.Negative;
var Error = org.warp.picalculator.Error;
var Errors = org.warp.picalculator.Errors;
var NullPointerException = java.lang.NullPointerException;
var NumberFormatException = java.lang.NumberFormatException;
var ArithmeticException = java.lang.ArithmeticException;

/**
 * Negative
 * -a = b
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "Negative";
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
		if (ScriptUtils.instanceOf(f, Negative.class)) {
			var result = new ObjectArrayList();
			var variable = f.getParameter();
			var mathContext = f.getMathContext();
			if (ScriptUtils.instanceOf(variable, Number.class)) {
				//-a = a*-1 = b
				try {
					result.add(variable.multiply(new Number(mathContext, -1)));
				} catch (ex) {
					if (ScriptUtils.instanceOf(ex, NullPointerException.class)) {
						throw new Error(Errors.ERROR);
					} else if (ScriptUtils.instanceOf(ex, NumberFormatException.class)) {
						throw new Error(Errors.SYNTAX_ERROR);
					} else if (ScriptUtils.instanceOf(ex, ArithmeticException.class)) {
						throw new Error(Errors.NUMBER_TOO_SMALL);
					}
				}
				return result;
			}
		}
		return null;
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
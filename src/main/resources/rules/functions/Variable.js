// Imports
var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");
var BigDecimalMath = org.nevec.rjm.BigDecimalMath;
var Utils = org.warp.picalculator.Utils;
var MathematicalSymbols = org.warp.picalculator.math.MathematicalSymbols;
var ScriptUtils = org.warp.picalculator.ScriptUtils;
var Rule = org.warp.picalculator.math.rules.Rule;
var RuleType = org.warp.picalculator.math.rules.RuleType;
var RulesManager = org.warp.picalculator.math.rules.RulesManager;
var Multiplication = org.warp.picalculator.math.functions.Multiplication;
var Sum = org.warp.picalculator.math.functions.Sum;
var Subtraction = org.warp.picalculator.math.functions.Subtraction;
var Variable = org.warp.picalculator.math.functions.Variable;
var Number = org.warp.picalculator.math.functions.Number;

/**
 * Variable
 * a = n
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "Variable";
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
		if (ScriptUtils.instanceOf(f, Variable.class)) {
			var result = new ObjectArrayList();
			var variable = f.getChar();
			var mathContext = f.getMathContext();
			if (mathContext.exactMode == false) {
				if (variable.equals(MathematicalSymbols.PI)) {
					//a = n
					result.add(new Number(mathContext, BigDecimalMath.pi(new java.math.MathContext(Utils.scale, Utils.scaleMode2))));
					return result;
				}
			}
		}
		return null;
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
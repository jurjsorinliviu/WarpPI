//Imports


var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var MathContext = org.warp.picalculator.math.MathContext;
var Multiplication = org.warp.picalculator.math.functions.Multiplication;
var Power = org.warp.picalculator.math.functions.Power;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Exponent rule
 * (a ^ b) ^ c = a ^ (b * c)
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "ExponentRule9";
	},
	// Rule type
	getRuleType: function() {
		return RuleType.EXPANSION;
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
			if (ScriptUtils.instanceOf(fnc.getParameter1(), Power.class)) {
				isExecutable = true;
			}
		}
	
		if (isExecutable) {
		var root = f.getMathContext();
		var result = new ObjectArrayList();
		var powC = f;
		var powB = powC.getParameter1();
		var p = new Power(root, powB.getParameter1(), new Multiplication(root, powB.getParameter2(), powC.getParameter2()));
		result.add(p);
		return result;
		} else {
			return null;
		}
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
//Imports


var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var MathContext = org.warp.picalculator.math.MathContext;
var Multiplication = org.warp.picalculator.math.functions.Multiplication;
var Number = org.warp.picalculator.math.functions.Number;
var Power = org.warp.picalculator.math.functions.Power;
var Root = org.warp.picalculator.math.functions.Root;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Exponent rule
 * a√x=x^1/a
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "ExponentRule17";
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
		if (ScriptUtils.instanceOf(f, Root.class)) {
			var fnc = f;
			if (fnc.getParameter1().equals(fnc.getParameter2())) {
				isExecutable = true;
			}
		}
	
		if (isExecutable) {
			var root = f.getMathContext();
			var result = new ObjectArrayList();
			var fnc = f;
			var a = fnc.getParameter1();
			var two = new Number(root, 2);
			var p = new Power(fnc.getMathContext(), a, two);
			result.add(p);
			return result;
		} else {
			return null;
		}
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
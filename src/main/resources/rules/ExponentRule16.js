//Imports


var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var MathContext = org.warp.picalculator.math.MathContext;
var Multiplication = org.warp.picalculator.math.functions.Multiplication;
var Number = org.warp.picalculator.math.functions.Number;
var Power = org.warp.picalculator.math.functions.Power;
var Sum = org.warp.picalculator.math.functions.Sum;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Exponent rule
 * (a ^ b) * (a ^ c) = a ^ (b + c)
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "ExponentRule16";
	},
	// Rule type
	getRuleType: function() {
		return RuleType.REDUCTION;
	},
	/* Rule function
	   Returns:
	     - null if it's not executable on the function "f"
	     - An ObjectArrayList<Function> if it did something
	*/

	execute: function(f) {
		var isExecutable;
		if (ScriptUtils.instanceOf(f, Multiplication.class)) {
			var fnc = f;
			if (ScriptUtils.instanceOf(fnc.getParameter1(), Power.class) && ScriptUtils.instanceOf(fnc.getParameter2(), Power.class)) {
				isExecutable = (fnc.getParameter1()).getParameter1().equals((fnc.getParameter2()).getParameter1());
			} else if (ScriptUtils.instanceOf(fnc.getParameter1(), Power.class)) {
				isExecutable = (fnc.getParameter1()).getParameter1().equals(fnc.getParameter2());
			} else if (ScriptUtils.instanceOf(fnc.getParameter2(), Power.class)) {
				isExecutable = (fnc.getParameter2()).getParameter1().equals(fnc.getParameter1());
			}
		}
	
		if (isExecutable) {
			var root = f.getMathContext();
			var result = new ObjectArrayList();
			var fnc = f;
			if (ScriptUtils.instanceOf(fnc.getParameter1(), Power.class) && ScriptUtils.instanceOf(fnc.getParameter2(), Power.class)) {
				result.add(new Power(root, (fnc.getParameter1()).getParameter1(), new Sum(root, fnc.getParameter1().getParameter2(), fnc.getParameter2().getParameter2())));
			} else if (ScriptUtils.instanceOf(fnc.getParameter1(), Power.class)) {
				result.add(new Power(root, fnc.getParameter2(), new Sum(root, fnc.getParameter1().getParameter2(), new Number(root, 1))));
			} else if (ScriptUtils.instanceOf(fnc.getParameter2(), Power.class)) {
				result.add(new Power(root, fnc.getParameter1(), new Sum(root, new Number(root, 1), fnc.getParameter2().getParameter2())));
			}
			return result;
		} else {
			return null;
		}
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
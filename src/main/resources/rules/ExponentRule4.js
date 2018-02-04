//Imports


var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var MathContext = org.warp.picalculator.math.MathContext;
var Expression = org.warp.picalculator.math.functions.Expression;
var Multiplication = org.warp.picalculator.math.functions.Multiplication;
var Number = org.warp.picalculator.math.functions.Number;
var Power = org.warp.picalculator.math.functions.Power;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Exponent rule
 * (a*b)^n=a^n*b^n
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "ExponentRule4";
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
			if (fnc.getParameter1() instanceof Multiplication && ScriptUtils.instanceOf(fnc.getParameter2(), Number.class)) {
				isExecutable = true;
			}
		}
	
		if (isExecutable) {
		var root = f.getMathContext();
		var result = new ObjectArrayList();
		var fnc = f;
		var mult = fnc.getParameter1();
		var a = mult.getParameter1();
		var b = mult.getParameter2();
		var n = fnc.getParameter2();
		var p1 = new Power(root, a, n);
		var p2 = new Power(root, b, n);
		var retMult = new Multiplication(root, p1, p2);
		result.add(retMult);
		return result;
		} else {
			return null;
		}
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
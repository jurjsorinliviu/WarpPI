//Imports


var BigDecimal = Java.type("java.math.BigDecimal");

var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var MathContext = org.warp.picalculator.math.MathContext;
var Division = org.warp.picalculator.math.functions.Division;
var Number = org.warp.picalculator.math.functions.Number;
var Power = org.warp.picalculator.math.functions.Power;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Fractions rule
 * (a / b) ^ -c = (b / a) ^ c
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "FractionsRule5";
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
			if (ScriptUtils.instanceOf(fnc.getParameter1(), Division.class)) {
				if (ScriptUtils.instanceOf(fnc.getParameter2(), Multiplication.class) && fnc.getParameter2().getParameter1().equals(new Number(f.getMathContext(), -1))) {
					isExecutable = true;
				} else if (ScriptUtils.instanceOf(fnc.getParameter2(), Number.class)) {
					var n2 = fnc.getParameter2();
					if (n2.getTerm().compareTo(BigDecimal.ZERO) < 0) {
						isExecutable = true;
					}
				}
			}
		}
	
		if (isExecutable) {
			var root = f.getMathContext();
			var result = new ObjectArrayList();
			var fnc = f;
			var a = (fnc.getParameter1()).getParameter1();
			var b = (fnc.getParameter1()).getParameter2();
			var c;
			if (ScriptUtils.instanceOf(fnc.getParameter2(), Multiplication.class)) {
				c = fnc.getParameter2().getParameter2();
			} else {
				c = fnc.getParameter2().multiply(new Number(root, "-1"));
			}
			var dv = new Division(root, b, a);
			var pow = new Power(root, dv, c);
			result.add(pow);
			return result;
		} else {
			return null;
		}
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
//Imports


var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var Division = org.warp.picalculator.math.functions.Division;
var Number = org.warp.picalculator.math.functions.Number;
var Power = org.warp.picalculator.math.functions.Power;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Fractions rule
 * (a / b) ^ -1 = b / a
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "FractionsRule4";
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
			if (ScriptUtils.instanceOf(fnc.getParameter1(), Division.class) && ScriptUtils.instanceOf(fnc.getParameter2(), Number.class)) {
				var n2 = fnc.getParameter2();
				if (n2.equals(new Number(f.getMathContext(), -1))) {
					isExecutable = true;
				}
			}
		}
	
		if (isExecutable) {
			var result = new ObjectArrayList();
			var fnc = f;
			var a = (fnc.getParameter1()).getParameter1();
			var b = (fnc.getParameter1()).getParameter2();
			var res = new Division(f.getMathContext(), b, a);
			result.add(res);
			return result;
		} else {
			return null;
		}
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
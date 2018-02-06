//Imports


var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var Division = org.warp.picalculator.math.functions.Division;
var Number = org.warp.picalculator.math.functions.Number;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Fractions rule
 * a / 1 = a
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "FractionsRule2";
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
		var isExecutable;
		if (ScriptUtils.instanceOf(f, Division.class)) {
			var fnc = f;
			if (ScriptUtils.instanceOf(fnc.getParameter2(), Number.class)) {
				var numb = fnc.getParameter2();
				if (numb.equals(new Number(f.getMathContext(), 1))) {
					isExecutable = true;
				}
			}
		}
	
		if (isExecutable) {
			var result = new ObjectArrayList();
			var fnc = f;
			result.add(fnc.getParameter1());
			return result;
		} else {
			return null;
		}
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
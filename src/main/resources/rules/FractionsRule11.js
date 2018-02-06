//Imports


var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var Division = org.warp.picalculator.math.functions.Division;
var Multiplication = org.warp.picalculator.math.functions.Multiplication;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Fractions rule
 * a / (b / c) = (a * c) / b
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "FractionsRule11";
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
		if (ScriptUtils.instanceOf(f, Division.class)) {
			var fnc = f;
			var a;
			var c;
			var div2;
			if (ScriptUtils.instanceOf(fnc.getParameter2(), Division.class)) {
				div2 = fnc.getParameter2();
				a = fnc.getParameter1();
				c = div2.getParameter2();
				isExecutable = true;
			} else {
				isExecutable = false;
			}
		}
		if (isExecutable) {
			var result = new ObjectArrayList();
			var fnc = f;
			var a;
			var b;
			var c;
	
			var div2 = fnc.getParameter2();
	
			a = fnc.getParameter1();
			b = div2.getParameter1();
			c = div2.getParameter2();
			result.add(new Division(fnc.getMathContext(), new Multiplication(fnc.getMathContext(), a, c), b));
	
			return result;
		} else {
			return null;
		}
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
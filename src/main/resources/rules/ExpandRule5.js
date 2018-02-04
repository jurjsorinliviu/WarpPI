// Imports
var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var Expression = org.warp.picalculator.math.functions.Expression;
var Negative = org.warp.picalculator.math.functions.Negative;
var Subtraction = org.warp.picalculator.math.functions.Subtraction;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Expand rule
 * -(-a) = a
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "ExpandRule5";
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
		if (ScriptUtils.instanceOf(f, Negative.class)) {
			isExecutable = ScriptUtils.instanceOf(f.getParameter(), Negative.class);
		} else if (ScriptUtils.instanceOf(f, Multiplication.class)) {
			if (f.getParameter1().equals(new Number(f.getMathContext(), -1)) && ScriptUtils.instanceOf(f.getParameter2(), Multiplication.class)) {
				isExecutable = f.getParameter2().getParameter1().equals(f.getParameter1());
			}
		}
	
		if (isExecutable) {
			var result = new ObjectArrayList();
	
			if (ScriptUtils.instanceOf(f, Negative.class)) {
				var fnc = f;
				result.add(((fnc.getParameter()).getParameter()).getParameter());
			} else if (ScriptUtils.instanceOf(f, Multiplication.class)) {
				var fnc = f;
				result.add(fnc.getParameter2().getParameter2());
			}
			return result;
		} else {
			return null;
		}
	}

}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
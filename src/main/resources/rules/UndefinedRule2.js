//Imports


var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var MathContext = org.warp.picalculator.math.MathContext;
var Division = org.warp.picalculator.math.functions.Division;
var Number = org.warp.picalculator.math.functions.Number;
var Undefined = org.warp.picalculator.math.functions.Undefined;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Undefined rule
 * a / 0 = undefined
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "UndefinedRule2";
	},
	// Rule type
	getRuleType: function() {
		return RuleType.EXISTENCE;
	},
	/* Rule function
	   Returns:
	     - null if it's not executable on the function "f"
	     - An ObjectArrayList<Function> if it did something
	*/

	execute: function(f) {
		var isExecutable;
		if (ScriptUtils.instanceOf(f, Division.class)) {
			var root = f.getMathContext();
			var fnc = f;
			if (ScriptUtils.instanceOf(fnc.getParameter2(), Number.class)) {
				var numb = fnc.getParameter2();
				if (numb.equals(new Number(root, 0))) {
					isExecutable = true;
				}
			}
		}
	
		if (isExecutable) {
			var root = f.getMathContext();
			var result = new ObjectArrayList();
			result.add(new Undefined(root));
			return result;
		} else {
			return null;
		}
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
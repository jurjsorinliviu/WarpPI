//Imports


var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var FunctionOperator = org.warp.picalculator.math.FunctionOperator;
var MathContext = org.warp.picalculator.math.MathContext;
var Number = org.warp.picalculator.math.functions.Number;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Number rule
 * a + 0 = a
 * 0 + a = a
 * a - 0 = a
 * 0 - a = -a
 * a ± 0 = a
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "NumberRule5";
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
		if (ScriptUtils.instanceOf(f, Sum.class) || ScriptUtils.instanceOf(f, Subtraction.class) || ScriptUtils.instanceOf(f, SumSubtraction.class)) {
			var root = f.getMathContext();
			var fnc = f;
			if (fnc.getParameter1().equals(new Number(root, 0)) || fnc.getParameter2().equals(new Number(root, 0))) {
				if (!(fnc.getParameter1().equals(new Number(root, 0)) && ScriptUtils.instanceOf(f, SumSubtraction.class))) {
					isExecutable = true;
				}
			}
		}
	
		if (isExecutable) {
			var root = f.getMathContext();
			var result = new ObjectArrayList();
			var fnc = f;
			var a = fnc.getParameter1();
			if (a.equals(new Number(root, 0))) {
				if (ScriptUtils.instanceOf(f, Subtraction.class)) {
					a = new Multiplication(root, new Number(root, -1), fnc.getParameter2());
				} else {
					a = fnc.getParameter2();
				}
			}
			result.add(a);
			return result;
		} else {
			return null;
		}
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
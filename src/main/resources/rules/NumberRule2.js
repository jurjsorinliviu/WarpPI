//Imports


var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var MathContext = org.warp.picalculator.math.MathContext;
var Multiplication = org.warp.picalculator.math.functions.Multiplication;
var Number = org.warp.picalculator.math.functions.Number;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Number rule
 * a * 1 = a
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "NumberRule2";
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
		if (ScriptUtils.instanceOf(f, Multiplication.class)) {
			var root = f.getMathContext();
			var mult = f;
			if (ScriptUtils.instanceOf(mult.getParameter1(), Number.class)) {
				var numb = mult.getParameter1();
				if (numb.equals(new Number(root, 1))) {
					isExecutable = true;
				}
			}
			if (ScriptUtils.instanceOf(mult.getParameter2(), Number.class)) {
				var numb = mult.getParameter2();
				if (numb.equals(new Number(root, 1))) {
					isExecutable = true;
				}
			}
		}
	
		if (isExecutable) {
			var root = f.getMathContext();
			var result = new ObjectArrayList();
			var a = null;
			var aFound = false;
			var mult = f;
			if (aFound == false & ScriptUtils.instanceOf(mult.getParameter1(), Number.class)) {
				var numb = mult.getParameter1();
				if (numb.equals(new Number(root, 1))) {
					a = mult.getParameter2();
					aFound = true;
				}
			}
			if (aFound == false && ScriptUtils.instanceOf(mult.getParameter2(), Number.class)) {
				var numb = mult.getParameter2();
				if (numb.equals(new Number(root, 1))) {
					a = mult.getParameter1();
					aFound = true;
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
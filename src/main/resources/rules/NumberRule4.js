//Imports


var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var MathContext = org.warp.picalculator.math.MathContext;
var Subtraction = org.warp.picalculator.math.functions.Subtraction;
var Sum = org.warp.picalculator.math.functions.Sum;
var SumSubtraction = org.warp.picalculator.math.functions.SumSubtraction;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Number rule
 * a ± b = {a+b, a-b}
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "NumberRule4";
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
		if (ScriptUtils.instanceOf(f, SumSubtraction.class)) {
			isExecutable = true;
		}
	
		if (isExecutable) {
			var root = f.getMathContext();
			var result = new ObjectArrayList();
			var ss = f;
			result.add(new Sum(root, ss.getParameter1(), ss.getParameter2()));
			result.add(new Subtraction(root, ss.getParameter1(), ss.getParameter2()));
			return result;
		} else {
			return null;
		}
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
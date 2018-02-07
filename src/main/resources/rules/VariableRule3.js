//Imports


var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var FunctionOperator = org.warp.picalculator.math.FunctionOperator;
var MathContext = org.warp.picalculator.math.MathContext;
var Multiplication = org.warp.picalculator.math.functions.Multiplication;
var Number = org.warp.picalculator.math.functions.Number;
var Subtraction = org.warp.picalculator.math.functions.Subtraction;
var Sum = org.warp.picalculator.math.functions.Sum;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Variable rule
 * x+ax=(a+1)*x (a,b NUMBER; x VARIABLES)
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "VariableRule3";
	},
	// Rule type
	getRuleType: function() {
		return RuleType.REDUCTION;
	},
	/* Rule function
	   Returns:
	     - null if it's not executable on the function "f"
	     - An ObjectArrayList<Function> if it did something
	*/

	execute: function(f) {
		var isExecutable;
		var fnc = f;
		if (ScriptUtils.instanceOf(f, Sum.class) || ScriptUtils.instanceOf(f, Subtraction.class)) {
			if (ScriptUtils.instanceOf(fnc.getParameter2(), Multiplication.class)) {
				var m2 = fnc.getParameter2();
				if (m2.getParameter2().equals(fnc.getParameter1())) {
					isExecutable = true;
				}
			}
		}
		
		if (isExecutable) {
			var root = fnc.getMathContext();
			var result = new ObjectArrayList();
			var m2 = fnc.getParameter2();
			var a = m2.getParameter1();
			var x = fnc.getParameter1();
	
			var rets;
			if (ScriptUtils.instanceOf(fnc, Sum.class)) {
				rets = new Sum(root, new Number(root, 1), a);
			} else {
				rets = new Subtraction(root, new Number(root, 1), a);
			}
	
			var retm = new Multiplication(root, rets, x);
			result.add(retm);
			return result;
		} else {
			return null;
		}
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
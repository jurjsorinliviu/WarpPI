//Imports


var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var FunctionOperator = org.warp.picalculator.math.FunctionOperator;
var MathContext = org.warp.picalculator.math.MathContext;
var Multiplication = org.warp.picalculator.math.functions.Multiplication;
var Subtraction = org.warp.picalculator.math.functions.Subtraction;
var Sum = org.warp.picalculator.math.functions.Sum;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Variable rule
 * ax+bx=(a+b)*x (a,b NUMBER; x VARIABLE|MULTIPLICATION)
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "VariableRule1";
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
		if (ScriptUtils.instanceOf(f, Subtraction.class) || ScriptUtils.instanceOf(f, Sum.class)) {
			if (ScriptUtils.instanceOf(fnc.getParameter1(), Multiplication.class) & ScriptUtils.instanceOf(fnc.getParameter2(), Multiplication.class)) {
				var m1 = fnc.getParameter1();
				var m2 = fnc.getParameter2();
				if (m1.getParameter1().equals(m2.getParameter1()) || m1.getParameter2().equals(m2.getParameter2())) {
					isExecutable = true;
				}
			}
		}
		
		if (isExecutable) {
			var root = fnc.getMathContext();
			var result = new ObjectArrayList();
			var m1 = fnc.getParameter1();
			var m2 = fnc.getParameter2();
			var a;
			var b;
			var x;
			if (m1.getParameter2().equals(m2.getParameter2())) {
				x = m1.getParameter2();
				a = m1.getParameter1();
				b = m2.getParameter1();
			} else {
				x = m1.getParameter1();
				a = m1.getParameter2();
				b = m2.getParameter2();
			}
	
			var rets;
			if (ScriptUtils.instanceOf(fnc, Sum.class)) {
				rets = new Sum(root, a, b);
			} else {
				rets = new Subtraction(root, a, b);
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
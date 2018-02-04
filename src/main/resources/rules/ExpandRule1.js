// Imports
var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");
var ScriptUtils = org.warp.picalculator.ScriptUtils;
var Rule = org.warp.picalculator.math.rules.Rule;
var RuleType = org.warp.picalculator.math.rules.RuleType;
var RulesManager = org.warp.picalculator.math.rules.RulesManager;
var Multiplication = org.warp.picalculator.math.functions.Multiplication;
var Sum = org.warp.picalculator.math.functions.Sum;
var Subtraction = org.warp.picalculator.math.functions.Subtraction;
var SumSubtraction = org.warp.picalculator.math.functions.SumSubtraction;
var Number = org.warp.picalculator.math.functions.Number;

/**
 * Expand rule
 * -(+a+b) = -a-b
 * -(+a-b) = -a+b
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "ExpandRule1";
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
		if (ScriptUtils.instanceOf(f, Multiplication.class)) {
			var fnc = f;
			if (fnc.getParameter1().equals(new Number(fnc.getMathContext(), -1))) {
				var expr = fnc.getParameter2();
				if (ScriptUtils.instanceOf(expr, Sum.class)) {
					isExecutable = true;
				} else if (ScriptUtils.instanceOf(expr, Subtraction.class)) {
					isExecutable = true;
				} else if (ScriptUtils.instanceOf(expr, SumSubtraction.class)) {
					isExecutable = true;
				}
			}
		} else if (ScriptUtils.instanceOf(f,Subtraction.class) || ScriptUtils.instanceOf(f,SumSubtraction.class)) {
			var fnc = f;
			var expr = fnc.getParameter2();
			if (ScriptUtils.instanceOf(expr,Sum.class)) {
				isExecutable = true;
			} else if (ScriptUtils.instanceOf(expr,Subtraction.class)) {
				isExecutable = true;
			} else if (ScriptUtils.instanceOf(expr,SumSubtraction.class)) {
				isExecutable = true;
			}
		}
		if (isExecutable) {
			var result = new ObjectArrayList();
			var root = f.getMathContext();

			var expr = null;
			var fromSubtraction = 0;
			var subtraction = null;
			if (ScriptUtils.instanceOf(f,Multiplication.class)) {
				expr = f.getParameter2();
			} else if (ScriptUtils.instanceOf(f, Subtraction.class) || f instanceof SumSubtraction) {
				expr = f.getParameter2();
				if (ScriptUtils.instanceOf(f, Subtraction.class)) {
					fromSubtraction = 1;
				} else {
					fromSubtraction = 2;
				}
			}

			if (ScriptUtils.instanceOf(f, SumSubtraction.class)) {
				
			}

			var fnc = expr;
			if (ScriptUtils.instanceOf(fnc, Sum.class)) {
				var a = fnc.getParameter1();
				var b = fnc.getParameter2();
				var fnc2 = new Subtraction(root, new Multiplication(root, new Number(root, -1), a), b);
				if (fromSubtraction > 0) {
					subtraction = new Subtraction(root, f.getParameter1(), fnc2);
					result.add(subtraction);
				} else {
					result.add(fnc2);
				}
			} else if (ScriptUtils.instanceOf(fnc, Subtraction.class)) {
				var a = fnc.getParameter1();
				var b = fnc.getParameter2();
				var fnc2 = new Sum(root, new Multiplication(root, new Number(root, -1), a), b);
				if (fromSubtraction > 0) {
					subtraction = new Subtraction(root, f.getParameter1(), fnc2);
					result.add(subtraction);
				} else {
					result.add(fnc2);
				}
			} else if (ScriptUtils.instanceOf(fnc, SumSubtraction.class)) {
				var a = fnc.getParameter1();
				var b = fnc.getParameter2();
				var fnc2 = new Sum(root, new Multiplication(root, new Number(root, -1), a), b);
				var fnc3 = new Subtraction(root, new Multiplication(root, new Number(root, -1), a), b);
				if (fromSubtraction > 0) {
					subtraction = new SumSubtraction(root, f.getParameter1(), fnc2);
					result.add(subtraction);
					subtraction = new SumSubtraction(root, f.getParameter1(), fnc3);
					result.add(subtraction);
					result.add(subtraction);
				} else {
					result.add(fnc2);
					result.add(fnc2);
				}
			}
			return result;
		} else {
			return null;
		}
		
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
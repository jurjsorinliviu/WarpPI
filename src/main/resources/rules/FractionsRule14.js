//Imports


var Error = org.warp.picalculator.Error;
var Function = org.warp.picalculator.math.Function;
var Division = org.warp.picalculator.math.functions.Division;
var Multiplication = org.warp.picalculator.math.functions.Multiplication;

var ObjectArrayList = Java.type("it.unimi.dsi.fastutil.objects.ObjectArrayList");

/**
 * Fractions rule
 * (a / b) * (c / d) = (a * c) / (b * d)
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "FractionsRule14";
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
			var a;
			var b;
			var c;
			var d;
			if (ScriptUtils.instanceOf(fnc.getParameter1(), Division.class) && ScriptUtils.instanceOf(fnc.getParameter2(), Division.class)) {
				var div1 = fnc.getParameter1();
				var div2 = fnc.getParameter2();
				a = div1.getParameter1();
				b = div1.getParameter2();
				c = div2.getParameter1();
				d = div2.getParameter2();
				isExecutable = true;
			} else if (ScriptUtils.instanceOf(fnc.getParameter1(), Division.class)) {
				var div1 = fnc.getParameter1();
				a = div1.getParameter1();
				b = div1.getParameter2();
				c = fnc.getParameter2();
				isExecutable = true;
			} else if (ScriptUtils.instanceOf(fnc.getParameter2(), Division.class)) {
				var div2 = fnc.getParameter2();
				a = fnc.getParameter1();
				c = div2.getParameter1();
				d = div2.getParameter2();
				isExecutable = true;
			}
		}
	
		if (isExecutable) {
			var result = new ObjectArrayList();
			var fnc = f;
			var a;
			var b;
			var c;
			var d;
	
			if (ScriptUtils.instanceOf(fnc.getParameter1(), Division.class) && ScriptUtils.instanceOf(fnc.getParameter2(), Division.class)) {
				var div1 = fnc.getParameter1();
				var div2 = fnc.getParameter2();
				a = div1.getParameter1();
				b = div1.getParameter2();
				c = div2.getParameter1();
				d = div2.getParameter2();
				var div = new Division(fnc.getMathContext(), new Multiplication(fnc.getMathContext(), a, c), new Multiplication(fnc.getMathContext(), b, d));
				result.add(div);
			} else if (ScriptUtils.instanceOf(fnc.getParameter1(), Division.class)) {
				var div1 = fnc.getParameter1();
				a = div1.getParameter1();
				b = div1.getParameter2();
				c = fnc.getParameter2();
				var div = new Division(fnc.getMathContext(), new Multiplication(fnc.getMathContext(), a, c), b);
				result.add(div);
			} else if (ScriptUtils.instanceOf(fnc.getParameter2(), Division.class)) {
				var div2 = fnc.getParameter2();
				a = fnc.getParameter1();
				c = div2.getParameter1();
				d = div2.getParameter2();
				var div = new Division(fnc.getMathContext(), new Multiplication(fnc.getMathContext(), a, c), d);
				result.add(div);
			}
			return result;
		} else {
			return null;
		}
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
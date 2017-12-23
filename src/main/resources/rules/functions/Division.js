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
var Division = org.warp.picalculator.math.functions.Division;

/**
 * Division
 * a/b = c
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "Division";
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
		if (ScriptUtils.instanceOf(f, Division.class)) {
			var result = new ObjectArrayList();
			var variable1 = f.getParameter1();
			var variable2 = f.getParameter2();
			var mathContext = f.getMathContext();
			if (ScriptUtils.instanceOf(variable1, Number.class) && ScriptUtils.instanceOf(variable2, Number.class)) {
				if (mathContext.exactMode) {
					if (variable1.isInteger() && variable2.isInteger()) {
						var factors1, factors2, mcm;
						try {
							factors1 = variable1.getFactors();
							factors2 = variable2.getFactors();
							mcm = ScriptUtils.mcm(factors1, factors2);
						} catch (error) {
							return null;
						}
						if (mcm.size() > 0) { //true if there is at least one common factor
							//divide by the common factor (ab/cb = a/c)
							var nmb1 = variable1.term.toBigIntegerExact();
							var nmb2 = variable2.term.toBigIntegerExact();
							mcm.forEach(function(integerNumber) {
								nmb1 = nmb1.divide(integerNumber);
								nmb2 = nmb2.divide(integerNumber);
							});
							result.add(new Division(mathContext, new Number(mathContext, nmb1), new Number(mathContext, nmb2)));
							return result;
						}
					}
				} else {
					//divide a by b (a/b = c)
					result.add(variable1.divide(variable2));
					return result;
				}
			}
		}
		return null;
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
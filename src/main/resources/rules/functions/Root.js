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
var Root = org.warp.picalculator.math.functions.Root;
var RootSquare = org.warp.picalculator.math.functions.RootSquare;
var BigDecimal = java.math.BigDecimal;

/**
 * Root
 * a√b = c
 * 
 * @author Andrea Cavalli
 *
 */
var rule = {
	// Rule name
	getRuleName: function() {
		return "Power";
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
		var isSquare = false;
		if (ScriptUtils.instanceOf(f, Root.class) || (isSquare = ScriptUtils.instanceOf(f, RootSquare.class))) {
			var result = new ObjectArrayList();
			var mathContext = f.getMathContext();
			var variable1 = isSquare?new Number(mathContext, 2):f.getParameter1();
			var variable2 = isSquare?f.getParameter():f.getParameter2();
			var isSolvable = false;
			var canBePorted = false;
			if (ScriptUtils.instanceOf(variable1, Number.class) && ScriptUtils.instanceOf(variable2, Number.class)) {
				isSolvable = isSolvable|!mathContext.exactMode;
				if (!isSolvable) {
					try {
						var resultVar = variable2.pow(new Number(mathContext, BigDecimal.ONE).divide(variable1));
						var originalVariable = resultVar.pow(new Number(mathContext, 2));
						if (originalVariable.equals(parameter2)) {
							isSolvable = true;
						}
					} catch (ex) {
						ex.printStackTrace();
					}
				}
			}
			if (!isSquare && !isSolvable && ScriptUtils.instanceOf(variable1, Number.class) && variable1.equals(new Number(mathContext, 2))) {
				canBePorted = true;
			}
			
			if (isSolvable) {
				result.add(variable2.pow(new Number(mathContext, BigInteger.ONE).divide(variable1)));
				return result;
			}
			if (canBePorted) {
				result.add(new RootSquare(mathContext, variable2));
			}
		}
		return null;
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));
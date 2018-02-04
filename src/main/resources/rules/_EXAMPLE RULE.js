
// Imports
var Rule = org.warp.picalculator.math.rules.Rule;
var RuleType = org.warp.picalculator.math.rules.RuleType;
var RulesManager = org.warp.picalculator.math.rules.RulesManager;

// Rule class
var rule = {
	// Rule name
	getRuleName: function() {
		return "ExampleRule1";
	},
	// Rule type
	getRuleType: function() {
		return RuleType.CALCULATION;
	},
	/* Rule function
	   Returns:
	     - null if it's not executable on the function "func"
	     - An ObjectArrayList<Function> if it did something
	*/
	execute: function(func) {
		return null;
	}
}

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));









CONVERSIONI:
	
	
	
	
	
	
inserire all'inizio:
//Imports

------------------
^package [^\n]+?;$

------------------
^import ((?:org|com|net)[^\n]+\.([^\n]+?));$
var $2 = $1;
------------------
^import ([^\n]+\.([^\n]+?));$
var $2 = Java.type("$1");
------------------
<br>|<b>|<\/b>

------------------
(static boolean compare\([\s\S]*?)(?:|\r\n)\treturn false;([\t\r\n}]+?public static)
$1$2
VVVV--------------
(static boolean compare\([\s\S]*?)return ([^;]+)([\s\S]+?public static)
$1isExecutable = $2$3
------------------
(?:|public)(?:|private)(?:| final) class ([^\n]+?)[ ]*\{
var rule = {\r\n\t// Rule name\r\n\tgetRuleName: function() {\r\n\t\treturn "$1";\r\n\t},\r\n\t// Rule type\r\n\tgetRuleType: function() {\r\n\t\treturn RuleType./* TODO: RULE TYPE */;\r\n\t},\r\n\t/* Rule function\r\n\t   Returns:\r\n\t     - null if it's not executable on the function "f"\r\n\t     - An ObjectArrayList<Function> if it did something\r\n\t*/
------------------
(?:|\r\n)\tpublic static boolean compare\(Function f\)[ ]*?\{
\texecute: function(f) {\r\n\t\tvar isExecutable;
------------------
(if \(|= |while \(|return |& |\| )([^ \n\r;]+?) instanceof ([^ \n\r;)]+)
$1ScriptUtils.instanceOf($2, $3.class)
------------------
\t}[\r\n]*?\tpublic static ObjectArrayList<Function> execute\(Function f\) throws Error \{
\t\tif (isExecutable) {
------------------

aggiungere tab

------------------

inserire in fondo al codice
} else {
	return null;
}

-----------------
\([A-Z][a-z]*?\)( |)

-----------------
([^.])(?:final )
$1
-----------------
[A-Z][A-z<>?]*? ([A-z]+?)(?: |)=(?: |)
var $1 = 
-----------------
(new [A-Z][A-z]+?)<[^;()+\-*\/]*?>
$1
-----------------

inserire in fondo:

//Add this rule to the list of rules
RulesManager.addRule(engine.getInterface(rule, Rule.class));

-----------------
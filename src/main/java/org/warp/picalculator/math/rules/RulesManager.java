package org.warp.picalculator.math.rules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.warp.picalculator.Error;
import org.warp.picalculator.StaticVars;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.MathSolver;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.functions.SumSubtraction;
import org.warp.picalculator.math.functions.Variable;
import org.warp.picalculator.math.functions.Variable.V_TYPE;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class RulesManager {
	
	public static ObjectArrayList<Rule>[] rules;
	
	private RulesManager() {
	}

	@SuppressWarnings("unchecked")
	public static void initialize() {
		rules = new ObjectArrayList[RuleType.values().length];
		for (RuleType val : RuleType.values()) {
			rules[val.ordinal()] = new ObjectArrayList<Rule>();
		}
		try {
			final Path rulesPath = Utils.getResource("/rules.csv");
			if (!Files.exists(rulesPath)) {
				throw new FileNotFoundException("rules.csv not found!");
			}
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
			List<String> ruleLines = Files.readAllLines(rulesPath);
			for (String rulesLine : ruleLines) {
				String[] ruleDetails = rulesLine.split(",", 1);
				String ruleName = ruleDetails[0];
				Utils.out.println("Evaluating /rules/" + ruleName + ".js");
				InputStream resourcePath = Utils.getResourceStream("/rules/" + ruleName.replace(".", "_") + ".js");
				if (resourcePath == null) {
					System.err.println(new FileNotFoundException("/rules/" + ruleName + ".js not found!"));
				} else {
					engine.eval(new InputStreamReader(resourcePath));
				}
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	public static void warmUp() {
		ObjectArrayList<Function> uselessResult = null;
		boolean uselessVariable;
		for (RuleType val : RuleType.values()) {
			final ObjectArrayList<Rule> ruleList = rules[val.ordinal()];
			for (final Rule rule : ruleList) {
				ObjectArrayList<Function> uselessResult2 = rule.execute(generateUselessExpression());
				uselessVariable = (uselessResult == null ? new ObjectArrayList<>() : uselessResult).equals(uselessResult2);
				uselessResult = uselessResult2;
			}
		}
		try {
			new MathSolver(generateUselessExpression()).solveAllSteps();
		} catch (InterruptedException | Error e) {
			e.printStackTrace();
		}
	}
	
	private static Expression generateUselessExpression() {
		MathContext mc = new MathContext();
		Expression expr = new Expression(mc);
		expr = (Expression) expr.setParameter(new Variable(mc, 'x', V_TYPE.VARIABLE));
		return expr;
	}
	
	public static void addRule(Rule rule) {
		rules[rule.getRuleType().ordinal()].add(rule);
		Utils.out.println(Utils.OUTPUTLEVEL_NODEBUG, "Loaded rule " + rule.getRuleName());
	}
}

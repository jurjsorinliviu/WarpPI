package org.warp.picalculator.math.rules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.warp.picalculator.StaticVars;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.MathContext;
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
			final Path rulesPath = Paths.get(StaticVars.classLoader.getResource("rules.csv").toURI());
			if (!Files.exists(rulesPath)) {
				throw new FileNotFoundException("rules.csv not found!");
			}
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
			List<String> ruleLines = Files.readAllLines(rulesPath);
			for (String rulesLine : ruleLines) {
				String[] ruleDetails = rulesLine.split(",", 1);
				String ruleName = ruleDetails[0];
				URL resourceURL = StaticVars.classLoader.getResource("rules" + File.separator + ruleName.replace(".", "_").replace('/', File.separatorChar) + ".js");
				if (resourceURL == null) {
					throw new FileNotFoundException("rules/" + ruleName + ".js not found!");
				}
				Path rulePath = Paths.get(resourceURL.toURI());
				engine.eval(new FileReader(rulePath.toString()));
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}
	
	public static void addRule(Rule rule) {
		MathContext mc = new MathContext();
		rules[rule.getRuleType().ordinal()].add(rule);
		Utils.out.println(Utils.OUTPUTLEVEL_NODEBUG, "Loaded rule " + rule.getRuleName());
	}
}

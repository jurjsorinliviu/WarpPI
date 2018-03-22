package org.warp.picalculator.math.rules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.warp.picalculator.Error;
import org.warp.picalculator.StaticVars;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Subtraction;
import org.warp.picalculator.math.functions.Sum;
import org.warp.picalculator.math.functions.SumSubtraction;
import org.warp.picalculator.math.functions.Variable;
import org.warp.picalculator.math.functions.Variable.V_TYPE;
import org.warp.picalculator.math.solver.MathSolver;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.openhft.compiler.CompilerUtils;

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
			final Path rulesPath = Utils.getResource("/math-rules.csv");
			if (!Files.exists(rulesPath)) {
				throw new FileNotFoundException("math-rules.csv not found!");
			}
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
			List<String> ruleLines = Files.readAllLines(rulesPath);
			ruleLines.remove(0); //Remove the headers
			for (String rulesLine : ruleLines) {
				if (rulesLine.length() > 0) {
					String[] ruleDetails = rulesLine.split(",", 1);
					String ruleName = ruleDetails[0];
					String ruleNameEscaped = ruleName.replace(".", "_");
					Utils.out.println("Evaluating /rules/" + ruleNameEscaped + ".java");
					String pathWithoutExtension = "/rules/" + ruleNameEscaped;
					String compiledFile = ruleNameEscaped + ".ser";
					InputStream compiledResourcePath = Utils.getResourceStreamSafe(compiledFile);
					String scriptFile = pathWithoutExtension + ".java";
					InputStream resourcePath = Utils.getResourceStream(scriptFile);
					if (resourcePath == null) {
						System.err.println(new FileNotFoundException("/rules/" + ruleName + ".java not found!"));
					} else {
						boolean loadedFromCache = false;
						if (compiledResourcePath != null) {
							if (Files.readAttributes(Paths.get(compiledFile), BasicFileAttributes.class).creationTime().compareTo(Files.readAttributes(Paths.get(scriptFile), BasicFileAttributes.class).creationTime()) < 0) {
								//The cached file is older than the source file. Deleting it.
								try {
									Files.deleteIfExists(Paths.get(compiledFile));
								} catch (Exception ex2) {
									ex2.printStackTrace();
								}
							} else {
								try {
									//Trying to read the compiled script
									ObjectInputStream ois = new ObjectInputStream(compiledResourcePath);
									RulesManager.addRule((Rule) ois.readObject());
									loadedFromCache = true;
								} catch (Exception ex) {
									ex.printStackTrace();
									try {
										Files.deleteIfExists(Paths.get(compiledFile));
									} catch (Exception ex2) {
										ex2.printStackTrace();
									}
								}
							}
						}
						if (!loadedFromCache) {
							Rule r = null;
							try {
								r = compileJavaRule(scriptFile);
								RulesManager.addRule(r);
								
								Path p = Paths.get(compiledFile.replace("/", "_")).toAbsolutePath();
								System.out.println(p);
								p.toFile().createNewFile();
								OutputStream fout = Files.newOutputStream(p, StandardOpenOption.CREATE);
								ObjectOutputStream oos = new ObjectOutputStream(fout);
								oos.writeObject(r);
							} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							 
						}
					}
				}
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static Rule compileJavaRule(String scriptFile) throws IOException, URISyntaxException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		// dynamically you can call
		InputStream resource = Utils.getResourceStream(scriptFile);
		
		String text = Utils.read(resource);
		String[] textArray = text.split("\\n", 2);
		String javaClassName = textArray[0].substring(2, textArray[0].length() - 2);
		String javaCode = textArray[1];
		Class aClass = CompilerUtils.CACHED_COMPILER.loadFromJava(javaClassName, javaCode);
		Rule rule = (Rule) aClass.newInstance();
		return rule;
	}
	
	public static void warmUp() {
		ObjectArrayList<Function> uselessResult = null;
		boolean uselessVariable = false;
		for (RuleType val : RuleType.values()) {
			final ObjectArrayList<Rule> ruleList = rules[val.ordinal()];
			for (final Rule rule : ruleList) {
				String ruleName = "<null>";
				try {
					ruleName = rule.getRuleName();
					ObjectArrayList<Function> uselessResult2 = rule.execute(generateUselessExpression());
					uselessVariable = (uselessResult == null ? new ObjectArrayList<>() : uselessResult).equals(uselessResult2);
					uselessResult = uselessResult2;
				} catch (Exception e) {
					if (uselessVariable || true) {
						System.err.println("Exception thrown by rule '" + ruleName + "'!");
						e.printStackTrace();
					}
				}
			}
		}
		try {
			new MathSolver(generateUselessExpression()).solveAllSteps();
		} catch (InterruptedException | Error e) {
			e.printStackTrace();
		}
	}
	
	private static Function generateUselessExpression() {
		MathContext mc = new MathContext();
		Function expr = new Expression(mc);
		expr = expr.setParameter(0, new Variable(mc, 'x', V_TYPE.VARIABLE));
		return expr;
	}
	
	public static void addRule(Rule rule) {
		rules[rule.getRuleType().ordinal()].add(rule);
		Utils.out.println(Utils.OUTPUTLEVEL_NODEBUG, "Loaded rule " + rule.getRuleName() + " as " + rule.getRuleType() + " rule.");
	}
}

package org.warp.picalculator.math.rules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Variable;
import org.warp.picalculator.math.functions.Variable.V_TYPE;
import org.warp.picalculator.math.solver.MathSolver;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class RulesManager {
	
	public static ObjectArrayList<Rule>[] rules;
	
	private RulesManager() {
	}

	@SuppressWarnings("unchecked")
	public static void initialize() {
		Utils.out.println(Utils.OUTPUTLEVEL_NODEBUG, "RulesManager", "Loading the rules");
		rules = new ObjectArrayList[RuleType.values().length];
		for (RuleType val : RuleType.values()) {
			rules[val.ordinal()] = new ObjectArrayList<Rule>();
		}
		try {
			boolean compiledSomething = false;
			final Path rulesPath = Utils.getResource("/rules.csv");
			if (!Files.exists(rulesPath)) {
				throw new FileNotFoundException("rules.csv not found!");
			}
			List<String> ruleLines = Files.readAllLines(rulesPath);
			ruleLines.remove(0); //Remove the headers

			boolean useCache = false;
			Path tDir = Paths.get(System.getProperty("java.io.tmpdir"), "WarpPi-Calculator").resolve("rules-rt");
//			try {
//				final Path defaultResource = Utils.getResource("/math-rules-cache.zip");
//			}
			Path cacheFilePath =  Utils.getResource("/math-rules-cache.zip");//Paths.get(Utils.getJarDirectory().toString()).resolve("math-rules-cache.zip").toAbsolutePath();
			if (cacheFilePath.toFile().exists()) {
				try {
					if (tDir.toFile().exists()) {
						tDir.toFile().delete();
					}
					Utils.unzip(cacheFilePath.toString(), tDir.getParent().toString(), "");
					useCache = !Utils.debugCache;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			for (String rulesLine : ruleLines) {
				if (rulesLine.length() > 0) {
					String[] ruleDetails = rulesLine.split(",", 1);
					String ruleName = ruleDetails[0];
					String ruleNameEscaped = ruleName.replace(".", "_");
					Utils.out.println(Utils.OUTPUTLEVEL_NODEBUG, "RulesManager", "Evaluating /rules/" + ruleNameEscaped + ".java");
					String pathWithoutExtension = "/rules/" + ruleNameEscaped;
					String scriptFile = pathWithoutExtension + ".java";
					InputStream resourcePath = Utils.getResourceStream(scriptFile);
					if (resourcePath == null) {
						System.err.println(new FileNotFoundException("/rules/" + ruleName + ".java not found!"));
					} else {
						Rule r = null;
						if (useCache) {
							try {
								Utils.out.println(Utils.OUTPUTLEVEL_DEBUG_MIN, "RulesManager", ruleName, "Trying to load cached rule");
								r = loadClassRuleFromSourceFile(scriptFile, tDir);
								if (r != null) {
									Utils.out.println(Utils.OUTPUTLEVEL_DEBUG_MIN, "RulesManager", ruleName, "Loaded cached rule");
								}
							} catch (Exception e) {
								e.printStackTrace();
								Utils.out.println(Utils.OUTPUTLEVEL_NODEBUG, "RulesManager", ruleName, "Can't load the rule!");
							}
						}
						if (r == null || !useCache) {
							Utils.out.println(Utils.OUTPUTLEVEL_DEBUG_MIN, "RulesManager", ruleName, "This rule is not cached. Compiling");
							try {
								r = compileJavaRule(scriptFile, tDir);
								compiledSomething = true;
							} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IOException e) {
								e.printStackTrace();
							}
							 
						}
						if (r != null) {
							RulesManager.addRule(r);
						}
					}
				}
			}
			Utils.out.println(Utils.OUTPUTLEVEL_NODEBUG, "RulesManager", "Loaded all the rules successfully");
			if (compiledSomething) {
				if (cacheFilePath.toFile().exists()) {
					cacheFilePath.toFile().delete();
				}
				Utils.zip(tDir.toString(), cacheFilePath.toString(), "");
				Utils.out.println(Utils.OUTPUTLEVEL_NODEBUG, "RulesManager", "Cached the compiled rules");
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static Rule compileJavaRule(String scriptFile, Path tDir) throws IOException, URISyntaxException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		InputStream resource = Utils.getResourceStream(scriptFile);
		String text = Utils.read(resource);
		String[] textArray = text.split("\\n", 5);
		String javaClassDeclaration = textArray[2].substring(6);
		int extIndex = javaClassDeclaration.lastIndexOf('.');
		String javaClassNameOnly = javaClassDeclaration.substring(extIndex + 1, javaClassDeclaration.length());
		String javaClassNameAndPath = new StringBuilder("org.warp.picalculator.math.rules.").append(javaClassDeclaration).toString();
		extIndex = javaClassNameAndPath.lastIndexOf('.');
		String javaCode = new StringBuilder("package ").append(javaClassNameAndPath.substring(0, extIndex >= 0 ? extIndex : javaClassNameAndPath.length())).append(";\n")
				.append(textArray[4]).toString();
		Path tDirPath = tDir.resolve(javaClassNameAndPath.replace('.', File.separatorChar)).getParent();
		Path tFileJava = tDirPath.resolve(javaClassNameOnly + ".java");
		Path tFileClass = tDirPath.resolve(javaClassNameOnly + ".class");
		if (!tDirPath.toFile().exists()) {
			Files.createDirectories(tDirPath);
		}
		if (tFileJava.toFile().exists()) {
			tFileJava.toFile().delete();
		}
		Files.write(tFileJava, javaCode.getBytes("UTF-8"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		boolean compiled = org.eclipse.jdt.internal.compiler.batch.Main.compile(new String[] {"-nowarn", "-1.8", tFileJava.toString()}, new PrintWriter(System.out), new PrintWriter(System.err), null);
		if (Utils.debugCache) {
			tFileJava.toFile().deleteOnExit();
		} else {
			tFileJava.toFile().delete();
		}
		if (compiled) {
			tFileClass.toFile().deleteOnExit();
			return loadClassRuleDirectly(javaClassNameAndPath, tDir);
		} else {
			throw new IOException("Can't build script file '" + scriptFile + "'");
		}
	}
	
	public static Rule loadClassRuleFromSourceFile(String scriptFile, Path tDir) throws IOException, URISyntaxException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		InputStream resource = Utils.getResourceStream(scriptFile);
		String text = Utils.read(resource);
		String[] textArray = text.split("\\n", 5);
		String javaClassName = textArray[2].substring(6);
		String javaClassNameAndPath = new StringBuilder("org.warp.picalculator.math.rules.").append(javaClassName).toString();
		try {
			return loadClassRuleDirectly(javaClassNameAndPath, tDir);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static Rule loadClassRuleDirectly(String javaClassNameAndPath, Path tDir) throws IOException, URISyntaxException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		URLClassLoader cl = new URLClassLoader(new URL[] {tDir.toUri().toURL()});
		Class<?> aClass = cl.loadClass(javaClassNameAndPath);
		cl.close();
		return (Rule) aClass.newInstance();
	}
	
	public static void warmUp() throws Error, InterruptedException {
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
		Utils.out.println(Utils.OUTPUTLEVEL_NODEBUG, "RulesManager", rule.getRuleName(), "Loaded as " + rule.getRuleType() + " rule");
	}
}

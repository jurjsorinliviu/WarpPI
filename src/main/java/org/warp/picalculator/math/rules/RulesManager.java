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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.warp.picalculator.ConsoleUtils;
import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.ZipUtils;
import org.warp.picalculator.deps.StorageUtils;
import org.warp.picalculator.deps.DJDTCompiler;
import org.warp.picalculator.deps.DSystem;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Variable;
import org.warp.picalculator.math.functions.Variable.V_TYPE;
import org.warp.picalculator.math.solver.MathSolver;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class RulesManager {

	public static ObjectArrayList<Rule>[] rules;

	private RulesManager() {}

	@SuppressWarnings("unchecked")
	public static void initialize() {
		ConsoleUtils.out.println(ConsoleUtils.OUTPUTLEVEL_NODEBUG, "RulesManager", "Loading the rules");
		rules = new ObjectArrayList[RuleType.values().length];
		for (final RuleType val : RuleType.values()) {
			rules[val.ordinal()] = new ObjectArrayList<>();
		}
		try {
			boolean compiledSomething = false;
			final Path defaultRulesPath = Utils.getResource("/default-rules.lst");
			if (!StorageUtils.exists(defaultRulesPath)) {
				throw new FileNotFoundException("default-rules.lst not found!");
			}
			final List<String> ruleLines = new ArrayList<>();
			final Path rulesPath = StorageUtils.get("rules/");
			if (rulesPath.toFile().exists()) {
				try (Stream<Path> paths = Files.walk(rulesPath)) {
					paths.filter(Files::isRegularFile).forEach((Path p) -> {
						if (p.toString().endsWith(".java")) {
							String path = rulesPath.relativize(p).toString();
							path = path.substring(0, path.length() - ".java".length());
							ruleLines.add(path);
							ConsoleUtils.out.println(ConsoleUtils.OUTPUTLEVEL_NODEBUG, "RulesManager", "Found external rule: " + p.toAbsolutePath().toString());
							System.err.println(path);
						}
					});
				}
			}
			ruleLines.addAll(Files.readAllLines(defaultRulesPath));

			boolean useCache = false;
			final Path tDir = Paths.get(System.getProperty("java.io.tmpdir"), "WarpPi-Calculator").resolve("rules-rt");
//			try {
//				final Path defaultResource = Utils.getResource("/math-rules-cache.zip");
//			}
			final Path cacheFilePath = Utils.getResource("/math-rules-cache.zip");//Paths.get(Utils.getJarDirectory().toString()).resolve("math-rules-cache.zip").toAbsolutePath();
			if (cacheFilePath.toFile().exists()) {
				try {
					if (tDir.toFile().exists()) {
						tDir.toFile().delete();
					}
					ZipUtils.unzip(cacheFilePath.toString(), tDir.getParent().toString(), "");
					useCache = !Utils.debugCache;
				} catch (final Exception ex) {
					ex.printStackTrace();
				}
			}
			for (final String rulesLine : ruleLines) {
				if (rulesLine.length() > 0) {
					final String[] ruleDetails = rulesLine.split(",", 1);
					final String ruleName = ruleDetails[0];
					final String ruleNameEscaped = ruleName.replace(".", "_");
					ConsoleUtils.out.println(ConsoleUtils.OUTPUTLEVEL_NODEBUG, "RulesManager", "Evaluating /rules/" + ruleNameEscaped + ".java");
					final String pathWithoutExtension = "/rules/" + ruleNameEscaped;
					final String scriptFile = pathWithoutExtension + ".java";
					final InputStream resourcePath = Utils.getResourceStream(scriptFile);
					if (resourcePath == null) {
						System.err.println(new FileNotFoundException("/rules/" + ruleName + ".java not found!"));
					} else {
						Rule r = null;
						if (useCache) {
							try {
								ConsoleUtils.out.println(ConsoleUtils.OUTPUTLEVEL_DEBUG_MIN, "RulesManager", ruleName, "Trying to load cached rule");
								r = loadClassRuleFromSourceFile(scriptFile, tDir);
								if (r != null) {
									ConsoleUtils.out.println(ConsoleUtils.OUTPUTLEVEL_DEBUG_MIN, "RulesManager", ruleName, "Loaded cached rule");
								}
							} catch (final Exception e) {
								e.printStackTrace();
								ConsoleUtils.out.println(ConsoleUtils.OUTPUTLEVEL_NODEBUG, "RulesManager", ruleName, "Can't load the rule!");
							}
						}
						if (r == null || !useCache) {
							ConsoleUtils.out.println(ConsoleUtils.OUTPUTLEVEL_DEBUG_MIN, "RulesManager", ruleName, "This rule is not cached. Compiling");
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
			ConsoleUtils.out.println(ConsoleUtils.OUTPUTLEVEL_NODEBUG, "RulesManager", "Loaded all the rules successfully");
			if (compiledSomething) {
				if (cacheFilePath.toFile().exists()) {
					cacheFilePath.toFile().delete();
				}
				ZipUtils.zip(tDir.toString(), cacheFilePath.toString(), "");
				ConsoleUtils.out.println(ConsoleUtils.OUTPUTLEVEL_NODEBUG, "RulesManager", "Cached the compiled rules");
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			DSystem.exit(1);
		}
	}

	public static Rule compileJavaRule(String scriptFile, Path tDir) throws IOException, URISyntaxException,
			InstantiationException, IllegalAccessException, ClassNotFoundException {
		final InputStream resource = Utils.getResourceStream(scriptFile);
		final String text = Utils.read(resource);
		final String[] textArray = text.split("\\n", 5);
		final String javaClassDeclaration = textArray[2].substring(6);
		int extIndex = javaClassDeclaration.lastIndexOf('.');
		final String javaClassNameOnly = javaClassDeclaration.substring(extIndex + 1, javaClassDeclaration.length());
		final String javaClassNameAndPath = new StringBuilder("org.warp.picalculator.math.rules.").append(javaClassDeclaration).toString();
		extIndex = javaClassNameAndPath.lastIndexOf('.');
		final String javaCode = new StringBuilder("package ").append(javaClassNameAndPath.substring(0, extIndex >= 0 ? extIndex : javaClassNameAndPath.length())).append(";\n").append(textArray[4]).toString();
		final Path tDirPath = tDir.resolve(javaClassNameAndPath.replace('.', File.separatorChar)).getParent();
		final Path tFileJava = tDirPath.resolve(javaClassNameOnly + ".java");
		final Path tFileClass = tDirPath.resolve(javaClassNameOnly + ".class");
		if (!tDirPath.toFile().exists()) {
			Files.createDirectories(tDirPath);
		}
		if (tFileJava.toFile().exists()) {
			tFileJava.toFile().delete();
		}
		Files.write(tFileJava, javaCode.getBytes("UTF-8"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		final boolean compiled = DJDTCompiler.compile(new String[] { "-nowarn", "-1.8", tFileJava.toString() }, new PrintWriter(System.out), new PrintWriter(System.err));
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

	public static Rule loadClassRuleFromSourceFile(String scriptFile, Path tDir) throws IOException, URISyntaxException,
			InstantiationException, IllegalAccessException, ClassNotFoundException {
		final InputStream resource = Utils.getResourceStream(scriptFile);
		final String text = Utils.read(resource);
		final String[] textArray = text.split("\\n", 5);
		final String javaClassName = textArray[2].substring(6);
		final String javaClassNameAndPath = new StringBuilder("org.warp.picalculator.math.rules.").append(javaClassName).toString();
		try {
			return loadClassRuleDirectly(javaClassNameAndPath, tDir);
		} catch (final Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Rule loadClassRuleDirectly(String javaClassNameAndPath, Path tDir) throws IOException,
			URISyntaxException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		final URLClassLoader cl = new URLClassLoader(new URL[] { tDir.toUri().toURL() });
		final Class<?> aClass = cl.loadClass(javaClassNameAndPath);
		cl.close();
		return (Rule) aClass.newInstance();
	}

	public static void warmUp() throws Error, InterruptedException {
		ObjectArrayList<Function> uselessResult = null;
		boolean uselessVariable = false;
		for (final RuleType val : RuleType.values()) {
			final ObjectArrayList<Rule> ruleList = rules[val.ordinal()];
			for (final Rule rule : ruleList) {
				String ruleName = "<null>";
				try {
					ruleName = rule.getRuleName();
					final ObjectArrayList<Function> uselessResult2 = rule.execute(generateUselessExpression());
					uselessVariable = (uselessResult == null ? new ObjectArrayList<>() : uselessResult).equals(uselessResult2);
					uselessResult = uselessResult2;
				} catch (final Exception e) {
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
		final MathContext mc = new MathContext();
		Function expr = new Expression(mc);
		expr = expr.setParameter(0, new Variable(mc, 'x', V_TYPE.VARIABLE));
		return expr;
	}

	public static void addRule(Rule rule) {
		rules[rule.getRuleType().ordinal()].add(rule);
		ConsoleUtils.out.println(ConsoleUtils.OUTPUTLEVEL_NODEBUG, "RulesManager", rule.getRuleName(), "Loaded as " + rule.getRuleType() + " rule");
	}
}

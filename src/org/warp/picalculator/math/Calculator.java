package org.warp.picalculator.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Number;
import org.warp.picalculator.math.functions.Variable.VariableValue;
import org.warp.picalculator.math.functions.equations.Equation;
import org.warp.picalculator.math.functions.equations.EquationsSystem;

public class Calculator {

	public AngleMode angleMode = AngleMode.DEG;
	public boolean exactMode = false;
	public ArrayList<Function> f;
	public ArrayList<Function> f2;
	public ArrayList<VariableValue> variablesValues;
	public int resultsCount;

	public Calculator() {
		f = new ArrayList<>();
		f2 = new ArrayList<>();
		variablesValues = new ArrayList<>();
		resultsCount = 0;
	}

	public Function parseString(String string) throws Error {
		if (string.contains("{")) {
			if (!string.startsWith("{")) {
				throw new Error(Errors.SYNTAX_ERROR);
			}
			final String[] parts = string.substring(1).split("\\{");
			final EquationsSystem s = new EquationsSystem(this);
			for (final String part : parts) {
				s.addFunctionToEnd(parseEquationString(part));
			}
			return s;
		} else if (string.contains("=")) {
			return parseEquationString(string);
		} else {
			return new Expression(this, string);
		}
	}

	public Function parseEquationString(String string) throws Error {
		final String[] parts = string.split("=");
		if (parts.length == 1) {
			final Equation e = new Equation(this, null, null);
			e.setVariable1(new Expression(this, parts[0]));
			e.setVariable2(new Number(this, BigInteger.ZERO));
			return e;
		} else if (parts.length == 2) {
			final Equation e = new Equation(this, null, null);
			e.setVariable1(new Expression(this, parts[0]));
			e.setVariable2(new Expression(this, parts[1]));
			return e;
		} else {
			throw new Error(Errors.SYNTAX_ERROR);
		}
	}

	public ArrayList<Function> solveExpression(ArrayList<Function> input) throws Error {
		ArrayList<Function> results = new ArrayList<>();
		final ArrayList<Function> partialResults = new ArrayList<>();
		for (final Function f : input) {
			if (f instanceof Equation) {
				throw new IllegalArgumentException("Not an expression!");
			} else {
				results.add(f);
				while (Utils.allSolved(results) == false) {
					for (final Function itm : results) {
						if (itm.isSolved() == false) {
							final long t1 = System.currentTimeMillis();
							final List<Function> dt = itm.solveOneStep();
							final long t2 = System.currentTimeMillis();
							if (t2 - t1 >= 3000) {
								throw new Error(Errors.TIMEOUT);
							}
							partialResults.addAll(dt);
						} else {
							partialResults.add(itm);
						}
					}
					results = new ArrayList<>(partialResults);
					partialResults.clear();
				}
			}
		}
		return results;
	}

	public Function getChild() {
		return f.get(0);
	}

	public void init() {
		if (f == null & f2 == null) {
			f = new ArrayList<>();
			f2 = new ArrayList<>();
			variablesValues = new ArrayList<>();
			resultsCount = 0;
		}
	}

	public void parseInputString(String eqn) throws Error {
		final ArrayList<Function> fncs = new ArrayList<>();
		if (eqn.length() > 0) {
			try {
				fncs.add(parseString(eqn.replace("sqrt", MathematicalSymbols.SQUARE_ROOT).replace("^", MathematicalSymbols.POWER)));
			} catch (final Exception ex) {

			}
		}
		f = fncs;
		for (final Function f : f) {
			try {
				f.generateGraphics();
			} catch (final NullPointerException ex) {
				throw new Error(Errors.SYNTAX_ERROR);
			}
		}
	}

	/*public void solve(EquationScreen equationScreen, char letter) throws Error {
		if (Calculator.currentSession == 0 && Calculator.sessions[0] instanceof EquationScreen) {
			EquationScreen es = (EquationScreen) Calculator.sessions[0];
			ArrayList<Function> f = es.f;
			if (f instanceof Equation) {
				List<Function> results = ((Equation)f).solve(letter);
				Collections.reverse(results);
				// add elements to al, including duplicates
				Set<Function> hs = new LinkedHashSet<>();
				hs.addAll(results);
				results.clear();
				results.addAll(hs);
				es.f2 = results;
				for (Function rf : es.f2) {
					rf.generateGraphics();
				}
			}
		}
	}*/

}

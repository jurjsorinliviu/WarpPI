package org.warp.picalculator;

import org.nevec.rjm.NumeroAvanzato;
import org.warp.device.PIDisplay;
import org.warp.engine.Screen;
import org.warp.picalculator.screens.EquationScreen;
import org.warp.picalculator.screens.SolveEquationScreen;

public class Calculator {

	public static String angleMode = "deg";
	public static Screen[] sessions = new Screen[5];
	public static int currentSession = 0;
	public static boolean haxMode = true;

	public static Number solveResult(String string) throws Error {
		System.out.println("INPUT: " + string);
		Expression expression = new Expression(string);
		return expression.solve();
	}

	public static Function parseString(String string) throws Error {
		if (string.contains("{")) {
			if (!string.startsWith("{")) {
				throw new Error(Errors.SYNTAX_ERROR);
			}
			String[] parts = string.substring(1).split("\\{");
			EquationsSystem s = new EquationsSystem();
			for (String part : parts) {
				s.addVariableToEnd(parseEquationString(part));
			}
			return s;
		} else if (string.contains("=")) {
			return parseEquationString(string);
		} else {
			return new Expression(string);
		}
	}
	
	public static Function parseEquationString(String string) throws Error {
		String[] parts = string.split("=");
		if (parts.length == 1) {
			return new Equation(new Expression(parts[0]), new Number(NumeroAvanzato.ZERO));
		} else if (parts.length == 2) {
			return new Equation(new Expression(parts[0]), new Expression(parts[1]));
		} else {
			throw new Error(Errors.SYNTAX_ERROR);
		}
	}

	public static void solve() throws Error {
		if (Calculator.currentSession == 0 && Calculator.sessions[0] instanceof EquationScreen) {
			EquationScreen es = (EquationScreen) Calculator.sessions[0];
			Function f = es.f;
			if (f instanceof Equation) {
				PIDisplay.INSTANCE.setScreen(new SolveEquationScreen(es));
			} else {
				es.f2 = es.f.solve();
				es.f2.generateGraphics();
			}
		}
	}

	public static void solve(char letter) throws Error {
		if (Calculator.currentSession == 0 && Calculator.sessions[0] instanceof EquationScreen) {
			EquationScreen es = (EquationScreen) Calculator.sessions[0];
			Function f = es.f;
			if (f instanceof Equation) {
				es.f2 = ((Equation)f).solve(letter);
				es.f2.generateGraphics();
			}
		}
	}

	public static void simplify() {
		
	}

}

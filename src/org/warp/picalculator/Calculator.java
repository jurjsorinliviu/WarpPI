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

	public static Termine calcolarisultato(String string) throws Errore {
		System.out.println("INPUT: " + string);
		Espressione espressione = new Espressione(string);
		return espressione.calcola();
	}

	public static Funzione interpreta(String string) throws Errore {
		if (string.contains("{")) {
			if (!string.startsWith("{")) {
				throw new Errore(Errori.SYNTAX_ERROR);
			}
			String[] parts = string.substring(1).split("\\{");
			Sistema s = new Sistema();
			for (String part : parts) {
				s.addVariableToEnd(interpretaEquazione(part));
			}
			return s;
		} else if (string.contains("=")) {
			return interpretaEquazione(string);
		} else {
			return new Espressione(string);
		}
	}
	
	public static Funzione interpretaEquazione(String string) throws Errore {
		String[] parts = string.split("=");
		if (parts.length == 1) {
			return new Equazione(new Espressione(parts[0]), new Termine(NumeroAvanzato.ZERO));
		} else if (parts.length == 2) {
			return new Equazione(new Espressione(parts[0]), new Espressione(parts[1]));
		} else {
			throw new Errore(Errori.SYNTAX_ERROR);
		}
	}

	public static void solve() throws Errore {
		if (Calculator.currentSession == 0 && Calculator.sessions[0] instanceof EquationScreen) {
			EquationScreen es = (EquationScreen) Calculator.sessions[0];
			Funzione f = es.f;
			if (f instanceof Equazione) {
				PIDisplay.INSTANCE.setScreen(new SolveEquationScreen(es));
			} else {
				es.f2 = es.f.calcola();
				es.f2.calcolaGrafica();
			}
		}
	}

	public static void solve(char letter) throws Errore {
		if (Calculator.currentSession == 0 && Calculator.sessions[0] instanceof EquationScreen) {
			EquationScreen es = (EquationScreen) Calculator.sessions[0];
			Funzione f = es.f;
			if (f instanceof Equazione) {
				es.f2 = ((Equazione)f).calcola(letter);
				es.f2.calcolaGrafica();
			}
		}
	}

	public static void simplify() {
		
	}

}

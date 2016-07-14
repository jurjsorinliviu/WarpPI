package org.warpgate.pi.calculator;

import org.nevec.rjm.NumeroAvanzatoVec;
import org.warp.engine.lwjgl.Screen;

public class Calculator {
	
	public static String angleMode = "deg";
	public static Screen[] sessions = new Screen[5];
	public static int currentSession = 0;
	public static boolean haxMode = true;
	
	public static Termine calcolarisultato(String string) throws Errore {
		System.out.println("INPUT: " + string);
		Parentesi espressione = new Parentesi(string);
		return espressione.calcola();
	}

	public static RisultatoEquazione calcolaequazione(String string) throws Errore {
		if (string.split("=").length == 0) {
			return new RisultatoEquazione(new Termine("0"), true);
		}
		if (string.split("=").length <= 2) {
			if (string.split("=").length == 1) {
				string = string + "=0";
			}
			Termine res1 = calcolarisultato(string.split("=")[0]);
			Termine res2 = calcolarisultato(string.split("=")[1]);
			Termine res = res1.add(res2.multiply(new Termine("-1")));
			if (res.calcola().getTerm().toString().equals("0")) {
				return new RisultatoEquazione(res.calcola(), true);
			}
			return new RisultatoEquazione(res.calcola(), false);
		}
		return new RisultatoEquazione(null, false);
	}

}

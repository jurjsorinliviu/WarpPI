package org.warpgate.pi.calculator.screens;

import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.warp.engine.lwjgl.Screen;
import org.warpgate.pi.calculator.Calculator;
import org.warpgate.pi.calculator.Errore;
import org.warpgate.pi.calculator.Errori;
import org.warpgate.pi.calculator.Funzione;
import org.warpgate.pi.calculator.Main;
import org.warpgate.pi.calculator.Parentesi;
import org.warpgate.pi.calculator.RisultatoEquazione;
import org.warpgate.pi.calculator.Termine;

public class EquationScreen extends Screen {

	public float endLoading;
	public static volatile String equazione = "";
	public static Parentesi f;
	public static Funzione f2;
	public static int ew1;
	public static int ew2;
	public static int eh2;
	public static int x1;
	public static int x2;
	public static boolean requiresleep1;
	public static boolean requiresleep2;
	public static boolean aftersleep;
	public static boolean autoscroll;
	
	@Override
	public void init() throws InterruptedException {
		canBeInHistory = true;
		endLoading = 0;
		try {
			/* Fine caricamento */
			
			//Parentesi f = new Parentesi("(Ⓐ(2X)*3Y)/(5Z^2)+(Ⓐ(11A)*13B)/(7CZ)=19XZ");
			//PARENTESI CON CALCOLI
			//Funzione f = new Sottrazione(new Somma(new Parentesi("Ⓐ9/2+Ⓐ7/2", "").calcola(), new Termine("3.5")), new Parentesi("3*2√14","").calcola());
			//PARENTESI CON DUE NUMERI FRAZIONALI COMPLETI CON INCOGNITE:
			//Funzione f = new Parentesi("(Ⓐ(2X)*3Y)/(5Z^2)+(Ⓐ(11A)*13B)/(7CZ)", "");
			//PARENTESI CON DUE NUMERI FRAZIONALI DISALLINEATI GRAFICAMENTE:
			//Funzione f = new Parentesi("((5^2-1)/2)/5-5/(5/2)=2", "");
			//TERMINE DI PROVA COMPLETO:
			//Funzione f = new Termine(new NumeroAvanzato(new Rational(3, 2), new Rational(7, 1), new Incognite(new Incognita('X', Rational.ONE)), new Incognite(new Incognita('Y', Rational.ONE)), new Incognite(new Incognita('z', Rational.ONE))));
			//PARENTESI REALISTICA CON INCOGNITE:
			//Funzione f = new Equazione(new Parentesi("X^2+(MX-M+4)^2-4X-4(MX-M+4)^2+7", ""), new Termine("0"));
			//POTENZA:
			//Funzione f = new Parentesi("(MX-M+4)^2", "");
			//NUMERO SEMPLICE LUNGO:
			//Funzione f = new Parentesi("-1219999799999996934.42229", "");
			//:
			//Funzione f = new Parentesi("5Y+XY=2", "")
			
			equazione = "0";
			f = new Parentesi(equazione);
			f2 = f.calcolaSistema();
			
			
			ew1 = f.getWidth();
			ew2 = f2.getWidth();
			eh2 = f2.getHeight(false);
			x1 = 2;
			x2 = 2;
			requiresleep1 = false;
			requiresleep2 = false;
			aftersleep = false;
			autoscroll = false;
			
			long start = System.nanoTime();
			Termine result = Calculator.calcolarisultato("((5Ⓑ2+3√(100/0.1))*Ⓐ7+9/15*2√(26/2))/21");
			long end = System.nanoTime();
			long timeElapsed = end-start;
			System.out.println("RESULT: " + result);
			System.out.println("DECIMAl RESULT: " + result.getTerm().toBigDecimal());
			System.out.println("Time elapsed: " +  (double) timeElapsed / 1000000 + " milliseconds\n");
			
			
			start = System.nanoTime();
			RisultatoEquazione eresult = Calculator.calcolaequazione("((5Ⓑ2+3√(100/0.1))*Ⓐ7+9/15*2√(26/2))/21=(175*(2√7)+3*(2√13))/105");
			end = System.nanoTime();
			timeElapsed = end-start;
			System.out.println("Is an equation: " + eresult.isAnEquation);
			System.out.println("L-R: " + eresult.LR);
			System.out.println("Time elapsed: " +  (double) timeElapsed / 1000000 + " milliseconds\n");
		} catch (Errore e) {
			d.setBackground(0.99609375f,102f/256f,34f/256f);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			d.errorStackTrace = sw.toString().toUpperCase().replace("\t", "    ").replace("\r", "").split("\n");
			d.error = e.id.toString();
			System.err.println(e.id);
		}
	}
	
	public void calcola(String eqn) {
		equazione  = eqn.replace("sqrt", "Ⓐ").replace("^", "Ⓑ");
		try {
			f = new Parentesi(equazione);
		} catch (Errore e) {
			e.printStackTrace();
		}
		try {
			f2 = f.calcolaSistema();
		} catch (Errore e) {
			e.printStackTrace();
		}
		ew1 = f.getWidth();
		ew2 = f2.getWidth();
		eh2 = f2.getHeight(false);
		x1 = 2;
		x2 = 2;
		requiresleep1 = false;
		requiresleep2 = false;
		aftersleep = false;
		autoscroll = false;
	}

	@Override
	public void beforeRender(float dt) {
		endLoading += dt;
		if (endLoading >= 2.5) {
			d.loading = false;
		}
	}
	
	@Override
	public void render() {
		d.setBackground(0.796875f, 0.90234375f, 0.828125f);
		try {
//			long delta = System.currentTimeMillis();
//	
//			if (equazione == "") {
//				throw new Errore(Errori.SYNTAX_ERROR);
//			} else {
//				if (ew1 + x1 + 5 > screenSize[0] && !requiresleep1 && autoscroll) {
//					x1-=1;
//				} else if(autoscroll) {
//					requiresleep1 = true;
//				}
//				
//				if (ew2 + x2 + 5 > screenSize[0] && !requiresleep2 && autoscroll) {
//					x2-=1;
//				} else if(autoscroll) {
//					requiresleep2 = true;
//				}
//	
//				if (requiresleep1 && requiresleep2 && autoscroll) {
//					requiresleep1 = false;
//					requiresleep2 = false;
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					x1 = 2;
//					x2 = 2;
//					aftersleep = true;
//				}
//				
//				f.draw(x1,2,d, false);
//				f2.draw(x2,screenSize[1]-2-eh2,d, false);
//			}
//			
//			if (aftersleep && autoscroll) {
//				aftersleep = false;
//				try {
//					Thread.sleep(500);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//			try {
//				delta = System.currentTimeMillis() - delta;
//				if (50-delta > 0 && autoscroll) {
//					Thread.sleep(50-delta);
//				}
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			if (false) {
				throw new Errore(Errori.SYNTAX_ERROR);
			}
		} catch (Errore e) {
			d.setBackground(0.99609375f,102f/256f,34f/256f);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			d.errorStackTrace = sw.toString().toUpperCase().replace("\t", "    ").replace("\r", "").split("\n");
			d.error = e.id.toString();
			System.err.println(e.id);
		}
	}

}

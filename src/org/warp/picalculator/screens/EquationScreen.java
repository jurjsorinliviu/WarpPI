package org.warp.picalculator.screens;

import static org.warp.picalculator.device.graphicengine.Display.Render.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.PIDisplay;
import org.warp.picalculator.device.Keyboard.Key;
import org.warp.picalculator.device.graphicengine.Display;
import org.warp.picalculator.device.graphicengine.Display.Render;
import org.warp.picalculator.device.graphicengine.Screen;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.functions.Function;

import com.rits.cloning.Cloner;
import com.rits.cloning.FastClonerArrayList;

public class EquationScreen extends Screen {

	public float endLoading;
	public volatile String equazioneCorrente = "";
	public volatile String nuovaEquazione = "";
	public volatile int caretPos = 0;
	public volatile boolean showCaret = true;
	public volatile float showCaretDelta = 0f;
	public List<Function> f;
	public List<Function> f2;
	public int resultsCount;
	public int ew1;
	public int ew2;
	public int eh2;
	public int x1;
	public int x2;
	public boolean requiresleep1;
	public boolean requiresleep2;
	public boolean aftersleep;
	public boolean autoscroll;
	public int errorLevel = 0; // 0 = nessuno, 1 = risultato, 2 = tutto
	public Error err1;
	public Error err2;
	boolean mustRefresh = true;

	public EquationScreen() {
		super();
		canBeInHistory = true;
	}
	
	@Override
	public void created() throws InterruptedException {
		endLoading = 0;
	}

	@Override
	public void init() throws InterruptedException {
		/* Fine caricamento */

		// Parentesi f = new
		// Parentesi("(Ⓐ(2X)*3Y)/(5Z^2)+(Ⓐ(11A)*13B)/(7CZ)=19XZ");
		// PARENTESI CON CALCOLI
		// Funzione f = new Sottrazione(new Somma(new Parentesi("Ⓐ9/2+Ⓐ7/2",
		// "").calcola(), new Termine("3.5")), new
		// Parentesi("3*2√14","").calcola());
		// PARENTESI CON DUE NUMERI FRAZIONALI COMPLETI CON INCOGNITE:
		// Funzione f = new
		// Parentesi("(Ⓐ(2X)*3Y)/(5Z^2)+(Ⓐ(11A)*13B)/(7CZ)", "");
		// PARENTESI CON DUE NUMERI FRAZIONALI DISALLINEATI GRAFICAMENTE:
		// Funzione f = new Parentesi("((5^2-1)/2)/5-5/(5/2)=2", "");
		// TERMINE DI PROVA COMPLETO:
		// Funzione f = new Termine(new NumeroAvanzato(new Rational(3, 2),
		// new Rational(7, 1), new Incognite(new Incognita('X',
		// Rational.ONE)), new Incognite(new Incognita('Y', Rational.ONE)),
		// new Incognite(new Incognita('z', Rational.ONE))));
		// PARENTESI REALISTICA CON INCOGNITE:
		// Funzione f = new Equazione(new
		// Parentesi("X^2+(MX-M+4)^2-4X-4(MX-M+4)^2+7", ""), new
		// Termine("0"));
		// POTENZA:
		// Funzione f = new Parentesi("(MX-M+4)^2", "");
		// NUMERO SEMPLICE LUNGO:
		// Funzione f = new Parentesi("-1219999799999996934.42229", "");
		// :
		// Funzione f = new Parentesi("5Y+XY=2", "")

//			calcola("((5^2+3√(100/0.1))+Ⓐ(7)+9/15*2√(26/2))/21");
		if (f == null & f2 == null) {
			f = new ArrayList<Function>();
			f2 = new ArrayList<Function>();
			resultsCount = 0;
		}
//			interpreta("{(5X*(15X/3X))+(25X/(5X*(15X/3X)))=15{X=5"); //TODO RIMUOVERE

		// long start = System.nanoTime();
		// Termine result =
		// Calculator.calcolarisultato("((5Ⓑ2+3√(100/0.1))*Ⓐ7+9/15*2√(26/2))/21");
		// long end = System.nanoTime();
		// long timeElapsed = end-start;
		// System.out.println("RESULT: " + result);
		// System.out.println("DECIMAl RESULT: " +
		// result.getTerm().toBigDecimal());
		// System.out.println("Time elapsed: " + (double) timeElapsed /
		// 1000000 + " milliseconds\n");
		//
		//
		// start = System.nanoTime();
		// RisultatoEquazione eresult =
		// Calculator.calcolaequazione("((5Ⓑ2+3√(100/0.1))*Ⓐ7+9/15*2√(26/2))/21=(175*(2√7)+3*(2√13))/105");
		// end = System.nanoTime();
		// timeElapsed = end-start;
		// System.out.println("Is an equation: " + eresult.isAnEquation);
		// System.out.println("L-R: " + eresult.LR);
		// System.out.println("Time elapsed: " + (double) timeElapsed /
		// 1000000 + " milliseconds\n");
	}

	public void interpreta(String eqn) throws Error {
		equazioneCorrente = eqn;
		List<Function> fncs = new ArrayList<Function>();
		fncs.add(Calculator.parseString(equazioneCorrente.replace("sqrt", "Ⓐ").replace("^", "Ⓑ")));
		f = fncs;
		for (Function f : f) {
			f.generateGraphics();
		}
	}
	
	public void solve() throws Error {
		Calculator.solve(this);
	}

	@Override
	public void beforeRender(float dt) {
		showCaretDelta += dt;
		if (showCaretDelta >= 0.5f) {
			mustRefresh = true;
			showCaret = !showCaret;
			showCaretDelta = 0f;
		}
	}

	@Override
	public void render() {
		Display.Render.glSetFont(Utils.getFont(false));
		glClearColor(0xFFCCE7D4);
		glColor3f(0, 0, 0);
		glDrawStringLeft(2, 22, nuovaEquazione.substring(0, caretPos)+(showCaret?"|":"")+nuovaEquazione.substring(((showCaret==false||nuovaEquazione.length()<=caretPos)?caretPos:caretPos+1), nuovaEquazione.length()));
		if (f != null) {
			int topSpacing = 0;
			for (Function f : f) {
				f.draw(2, 22+1+glGetCurrentFontHeight()+1+topSpacing);
				topSpacing += f.getHeight() + 2;
			}
		}
		if (f2 != null) {
			int bottomSpacing = 0;
			for (Function f : f2) {
				bottomSpacing += f.getHeight()+2;
				f.draw(Display.getWidth() - 2 - f.getWidth(), Display.getHeight() - bottomSpacing);
			}
			if (resultsCount > 1 && resultsCount != f2.size()) {
				String resultsCountText = resultsCount+" total results".toUpperCase();
				glColor(0xFF9AAEA0);
				glSetFont(Utils.getFont(true));
				bottomSpacing += glGetCurrentFontHeight()+2;
				glDrawStringRight(Display.getWidth() - 2, Display.getHeight() - bottomSpacing, resultsCountText);
			}
		}
	}

	@Override
	public boolean mustBeRefreshed() {
		if (mustRefresh) {
			mustRefresh = false;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean keyPressed(Key k) {
		switch (k) {
			case SIMPLIFY:
				if (nuovaEquazione.length() > 0) {
					Calculator.simplify(this);
				}
				return true;
			case SOLVE:
				if (PIDisplay.error != null) {
					Utils.debug.println("Resetting after error...");
					PIDisplay.error = null;
					return true;
				} else {
					if (nuovaEquazione != equazioneCorrente && nuovaEquazione.length() > 0) {
						try {
							try {
								changeEquationScreen();
								interpreta(nuovaEquazione);
								solve();
							} catch (Exception ex) {
								if (Utils.debugOn) {
									ex.printStackTrace();
								}
								throw new Error(Errors.ERROR);
							}
						} catch (Error e) {
							glClearColor(0xFFDC3C32);
							StringWriter sw = new StringWriter();
							PrintWriter pw = new PrintWriter(sw);
							e.printStackTrace(pw);
							d.errorStackTrace = sw.toString().toUpperCase().replace("\t", "    ").replace("\r", "").split("\n");
							PIDisplay.error = e.id.toString();
							System.err.println(e.id);
						}
					}
					return true;
				}
			case NUM0:
				typeChar("0");
				return true;
			case NUM1:
				typeChar("1");
				return true;
			case NUM2:
				typeChar("2");
				return true;
			case NUM3:
				typeChar("3");
				return true;
			case NUM4:
				typeChar("4");
				return true;
			case NUM5:
				typeChar("5");
				return true;
			case NUM6:
				typeChar("6");
				return true;
			case NUM7:
				typeChar("7");
				return true;
			case NUM8:
				typeChar("8");
				return true;
			case NUM9:
				typeChar("9");
				return true;
			case PLUS:
				typeChar("+");
				return true;
			case MINUS:
				typeChar("-");
				return true;
			case PLUS_MINUS:
				typeChar("±");
				return true;
			case MULTIPLY:
				typeChar("*");
				return true;
			case DIVIDE:
				typeChar("/");
				return true;
			case PARENTHESIS_OPEN:
				typeChar("(");
				return true;
			case PARENTHESIS_CLOSE:
				typeChar(")");
				return true;
			case DOT:
				typeChar(".");
				return true;
			case EQUAL:
				typeChar("=");
				return true;
			case SQRT:
				typeChar("Ⓐ");
				return true;
			case ROOT:
				typeChar("√");
				return true;
			case POWER_OF_2:
				typeChar("^");
				typeChar("2");
				return true;
			case POWER_OF_x:
				typeChar("^");
				return true;
			case LETTER_X:
				typeChar("X");
				return true;
			case LETTER_Y:
				typeChar("Y");
				return true;
			case DELETE:
				if (nuovaEquazione.length() > 0) {
					if (caretPos > 0) {
						caretPos-=1;
						nuovaEquazione=nuovaEquazione.substring(0, caretPos)+nuovaEquazione.substring(caretPos+1, nuovaEquazione.length());
					} else {
						nuovaEquazione = nuovaEquazione.substring(1);
					}
				}
				return true;
			case LEFT:
				if (caretPos > 0) {
					caretPos -= 1;
					showCaret = true;
					showCaretDelta = 0L;
				}
				return true;
			case RIGHT:
				if (caretPos < nuovaEquazione.length()) {
					caretPos += 1;
					showCaret = true;
					showCaretDelta = 0L;
				}
				return true;
			case RESET:
				if (PIDisplay.error != null) {
					Utils.debug.println("Resetting after error...");
					PIDisplay.error = null;
					return true;
				} else {
					caretPos = 0;
					nuovaEquazione="";
					return true;
				}
			case debug1:
				PIDisplay.INSTANCE.setScreen(new EmptyScreen());
				return true;
			default:
				return false;
		}
	}
	
	private void changeEquationScreen() {
		if (equazioneCorrente != null && equazioneCorrente.length() > 0) {
			EquationScreen cloned = clone();
			cloned.caretPos = cloned.equazioneCorrente.length();
			cloned.nuovaEquazione = cloned.equazioneCorrente;
			PIDisplay.INSTANCE.replaceScreen(cloned);
			this.initialized = false;
			PIDisplay.INSTANCE.setScreen(this);
			
		}
	}

	public void typeChar(String chr) {
		nuovaEquazione=nuovaEquazione.substring(0, caretPos)+chr+nuovaEquazione.substring(caretPos, nuovaEquazione.length());
		caretPos+=1;
		showCaret = true;
		showCaretDelta = 0L;
//		f.clear(); //TODO: I removed this line to prevent input blanking when pressing EQUALS button and cloning this screen, but I must see why I created this part of code.
	}

	@Override
	public boolean keyReleased(Key k) {
		
		return false;
	}
	
	@Override
	public EquationScreen clone() {
		EquationScreen es = this;
		EquationScreen es2 = new EquationScreen();
		es2.endLoading = es.endLoading;
		es2.nuovaEquazione = es.nuovaEquazione;
		es2.equazioneCorrente = es.equazioneCorrente;
		es2.showCaret = es.showCaret;
		es2.showCaretDelta = es.showCaretDelta;
		es2.caretPos = es.caretPos;
		es2.f = Utils.cloner.deepClone(es.f);
		es2.f2 = Utils.cloner.deepClone(es.f2);
		es2.resultsCount = es.resultsCount;
		es2.ew1 = es.ew1;
		es2.ew2 = es.ew2;
		es2.eh2 = es.eh2;
		es2.x1 = es.x1;
		es2.x2 = es.x2;
		es2.requiresleep1 = es.requiresleep1;
		es2.requiresleep2 = es.requiresleep2;
		es2.autoscroll = es.autoscroll;
		es2.errorLevel = es.errorLevel;
		es2.err1 = es.err1;
		es2.err2 = es.err2;
		es2.mustRefresh = es.mustRefresh;
		es2.aftersleep = es.aftersleep;
		return es2;
	}

}

package org.warp.picalculator.screens;

import static org.warp.engine.Display.Render.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.warp.device.Keyboard.Key;
import org.warp.device.PIDisplay;
import org.warp.engine.Display;
import org.warp.engine.Screen;
import org.warp.picalculator.Calculator;
import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Function;
import org.warp.picalculator.Utils;

public class EquationScreen extends Screen {

	public float endLoading;
	public volatile String equazioneCorrente = "";
	public volatile String nuovaEquazione = "";
	public volatile int caretPos = 0;
	public volatile boolean showCaret = true;
	public volatile float showCaretDelta = 0f;
	public Function f;
	public List<Function> f2;
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
		try {
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
			interpreta("0");
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

	public void interpreta(String eqn) throws Error {
		equazioneCorrente = eqn;
		f = Calculator.parseString(equazioneCorrente.replace("sqrt", "Ⓐ").replace("^", "Ⓑ"));
		f.generateGraphics();
	}
	
	public void solve() throws Error {
		Calculator.solve();
	}

	@Override
	public void beforeRender(float dt) {
		endLoading += dt;
		if (endLoading >= 1) {
			PIDisplay.loading = false;
		}
		showCaretDelta += dt;
		if (showCaretDelta >= 0.5f) {
			mustRefresh = true;
			showCaret = !showCaret;
			showCaretDelta = 0f;
		}
	}

	@Override
	public void render() {
		setFont(PIDisplay.fonts[0]);
		glClearColor(0xFFCCE7D4);
		glColor3f(0, 0, 0);
		glDrawStringLeft(2, 22, nuovaEquazione.substring(0, caretPos)+(showCaret?"|":"")+nuovaEquazione.substring(((showCaret==false||nuovaEquazione.length()<=caretPos)?caretPos:caretPos+1), nuovaEquazione.length()));
		if (f != null)
			f.draw(2, 22+1+9+1);
		if (f2 != null) {
			int bottomSpacing = 0;
			for (Function f : f2) {
				bottomSpacing += f.getHeight()+2;
				f.draw(Display.getWidth() - 2 - f.getWidth(), Display.getHeight() - bottomSpacing);
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
					Calculator.simplify();
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
	
	public void typeChar(String chr) {
		nuovaEquazione=nuovaEquazione.substring(0, caretPos)+chr+nuovaEquazione.substring(caretPos, nuovaEquazione.length());
		caretPos+=1;
		showCaret = true;
		showCaretDelta = 0L;
	}

	@Override
	public boolean keyReleased(Key k) {
		
		return false;
	}

}

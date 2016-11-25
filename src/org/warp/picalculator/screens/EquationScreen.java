package org.warp.picalculator.screens;

import static org.warp.picalculator.device.graphicengine.Display.Render.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Main;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.Keyboard;
import org.warp.picalculator.device.Keyboard.Key;
import org.warp.picalculator.device.PIDisplay;
import org.warp.picalculator.device.graphicengine.Display;
import org.warp.picalculator.device.graphicengine.RAWFont;
import org.warp.picalculator.device.graphicengine.Screen;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.Expression;

public class EquationScreen extends Screen {

	public volatile String equazioneCorrente = "";
	public volatile String nuovaEquazione = "";
	public volatile int caretPos = 0;
	public volatile boolean showCaret = true;
	public volatile float showCaretDelta = 0f;
	public ArrayList<Function> f;
	public ArrayList<Function> f2;
	public int resultsCount;
	public boolean autoscroll;
	public int scrollX = 0;
	public int errorLevel = 0; // 0 = nessuno, 1 = risultato, 2 = tutto
	boolean mustRefresh = true;
	boolean afterDoNextStep = false;

	public EquationScreen() {
		super();
		canBeInHistory = true;
	}
	
	@Override
	public void created() throws InterruptedException {
		
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

	public void interpreta(boolean temporary) throws Error {
		String eqn = nuovaEquazione;
		if (!temporary) {
			equazioneCorrente = eqn;
		}
		ArrayList<Function> fncs = new ArrayList<Function>();
		if (eqn.length() > 0) {
			fncs.add(Calculator.parseString(eqn.replace("sqrt", "Ⓐ").replace("^", "Ⓑ")));
		}
		f = fncs;
		for (Function f : f) {
			try {
				f.generateGraphics();
			} catch (NullPointerException ex) {
				throw new Error(Errors.SYNTAX_ERROR);
			}
		}
	}
	
	@Override
	public void beforeRender(float dt) {
		showCaretDelta += dt;
		if (showCaretDelta >= 0.5f) {
			mustRefresh = true;
			showCaret = !showCaret;
			showCaretDelta = 0f;
		}
		if (caretPos > nuovaEquazione.length()) {
			caretPos = nuovaEquazione.length();
		}

		if (PIDisplay.error == null) {
			glClearColor(0xFFc5c2af);
		} else {
			glClearColor(0xFFDC3C32);
		}
	}

	private static final RAWFont fontBig = Utils.getFont(false);
	
	@Override
	public void render() {
		Display.Render.glSetFont(fontBig);
		final int textColor = 0xFF000000;
		final int padding = 4;
		glColor(textColor);
		final String inputText = nuovaEquazione.substring(0, caretPos)+(showCaret?"|":"")+nuovaEquazione.substring(((showCaret==false||nuovaEquazione.length()<=caretPos)?caretPos:caretPos+1), nuovaEquazione.length());
		final boolean tooLongI = padding+glGetStringWidth(fontBig, nuovaEquazione)+padding >= Main.screenSize[0];
		int scrollI = 0;
		if (tooLongI) {
			scrollI = -scrollX;
			if (-scrollI >= Main.screenSize[0]) {
				scrollI += Main.screenSize[0];
			} else {
				scrollI = 0;
			}
		}
		glDrawStringLeft(padding+scrollI, padding+20, inputText);
		if (tooLongI) {
			glColor(clearcolor);
			glFillRect(Main.screenSize[0]-16-2, padding+20, fontBig.charH, Main.screenSize[0]);
			glColor(textColor);
			PIDisplay.drawSkinPart(Main.screenSize[0]-16, padding+20+fontBig.charH/2-16/2, 304, 0, 304+16, 16);
		}
		if (f != null) {
			int topSpacing = 0;
			Iterator<Function> iter = f.iterator();
			while (iter.hasNext()) {
				Function fnc = iter.next();
				try {
					final boolean tooLong = padding+fnc.getWidth()+padding >= Main.screenSize[0];
					int scrollA = 0;
					if (tooLong) {
						scrollA = -scrollX;
						if (-scrollA >= Main.screenSize[0]) {
							scrollA += Main.screenSize[0];
						} else {
							scrollA = 0;
						}
					}
					final int y = padding+20+padding+fontBig.charH+1+topSpacing;
					fnc.draw(padding+scrollA, y);
					if (tooLong) {
						glColor(clearcolor);
						glFillRect(Main.screenSize[0]-16-2, y, fnc.getHeight(), Main.screenSize[0]);
						glColor(textColor);
						PIDisplay.drawSkinPart(Main.screenSize[0]-16, y+fnc.getHeight()/2-16/2, 304, 0, 304+16, 16);
					}
				} catch (NullPointerException e) {
					iter.remove();
				}
				topSpacing += fnc.getHeight() + 2;
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
				bottomSpacing += fontBig.charH+2;
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
					try {
						try {
							if (!afterDoNextStep) {
								interpreta(false);
								f2 = f;
								afterDoNextStep = true;
								Calculator.simplify(this);
							}
							Calculator.simplify(this);
						} catch (Exception ex) {
							if (Utils.debugOn) {
								ex.printStackTrace();
							}
							throw new Error(Errors.SYNTAX_ERROR);
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
			case SOLVE:
				if (PIDisplay.error != null) {
					Utils.debug.println("Resetting after error...");
					PIDisplay.error = null;
					this.equazioneCorrente = null;
					this.f = null;
					this.f2 = null;
					this.resultsCount = 0;
					return true;
				} else {
					try {
						try {
							if (afterDoNextStep) {
								Calculator.simplify(this);
							} else {
								if (nuovaEquazione != equazioneCorrente && nuovaEquazione.length() > 0) {
									changeEquationScreen();
									interpreta(false);
									Calculator.solve(this);
								}
							}
						} catch (Exception ex) {
							if (Utils.debugOn) {
								ex.printStackTrace();
							}
							throw new Error(Errors.SYNTAX_ERROR);
						}
					} catch (Error e) {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						d.errorStackTrace = sw.toString().toUpperCase().replace("\t", "    ").replace("\r", "").split("\n");
						PIDisplay.error = e.id.toString();
						System.err.println(e.id);
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
				typeChar("^2");
				return true;
			case POWER_OF_x:
				typeChar("^");
				return true;
			case LETTER_X:
				typeChar(MathematicalSymbols.variables()[23]);
				return true;
			case LETTER_Y:
				typeChar(MathematicalSymbols.variables()[24]);
				return true;
			case DELETE:
				if (nuovaEquazione.length() > 0) {
					if (caretPos > 0) {
						caretPos-=1;
						nuovaEquazione=nuovaEquazione.substring(0, caretPos)+nuovaEquazione.substring(caretPos+1, nuovaEquazione.length());
					} else {
						nuovaEquazione = nuovaEquazione.substring(1);
					}
					try {interpreta(true);} catch (Error e) {}
				}
				afterDoNextStep = false;
				return true;
			case LEFT:
				if (caretPos > 0) {
					caretPos -= 1;
				} else {
					caretPos = nuovaEquazione.length();
				}
				scrollX = glGetStringWidth(fontBig, nuovaEquazione.substring(0, caretPos)+"|||");
				showCaret = true;
				showCaretDelta = 0L;
				return true;
			case RIGHT:
				if (caretPos < nuovaEquazione.length()) {
					caretPos += 1;
				} else {
					caretPos = 0;
				}
				scrollX = glGetStringWidth(fontBig, nuovaEquazione.substring(0, caretPos)+"|||");
				showCaret = true;
				showCaretDelta = 0L;
				return true;
			case RESET:
				if (PIDisplay.error != null) {
					Utils.debug.println("Resetting after error...");
					PIDisplay.error = null;
					return true;
				} else {
					caretPos = 0;
					nuovaEquazione="";
					afterDoNextStep = false;
					f.clear();
					return true;
				}
			case SURD_MODE:
				Calculator.surdMode = !Calculator.surdMode;
				try {
					try {
						if (Calculator.surdMode == false) {
							f2 = Calculator.solveExpression(f2);
						} else {
							equazioneCorrente = "";
							Keyboard.keyPressed(Key.SOLVE);
						}
					} catch (Exception ex) {
						if (Utils.debugOn)
							ex.printStackTrace();
						throw new Error(Errors.SYNTAX_ERROR);
					}
				} catch (Error e) {
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					d.errorStackTrace = sw.toString().toUpperCase().replace("\t", "    ").replace("\r", "").split("\n");
					PIDisplay.error = e.id.toString();
					System.err.println(e.id);
				}
				return true;
			case debug1:
				PIDisplay.INSTANCE.setScreen(new EmptyScreen());
				return true;
			case HISTORY_BACK:
				if (PIDisplay.INSTANCE.canGoBack()) {
					if (equazioneCorrente != null && equazioneCorrente.length() > 0 & Calculator.sessions[Calculator.currentSession+1] instanceof EquationScreen) {
						nuovaEquazione = equazioneCorrente;
						try {
							interpreta(true);
						} catch (Error e) {
						}
					}
				}
				return false;
			case HISTORY_FORWARD:
				if (PIDisplay.INSTANCE.canGoForward()) {
					if (equazioneCorrente != null && equazioneCorrente.length() > 0 & Calculator.sessions[Calculator.currentSession-1] instanceof EquationScreen) {
						nuovaEquazione = equazioneCorrente;
						try {
							interpreta(true);
						} catch (Error e) {
						}
					}
				}
				return false;
			default:
				return false;
		}
	}
	
	private void changeEquationScreen() {
		if (equazioneCorrente != null && equazioneCorrente.length() > 0) {
			EquationScreen cloned = clone();
			cloned.caretPos = cloned.equazioneCorrente.length();
			cloned.nuovaEquazione = cloned.equazioneCorrente;
			cloned.scrollX = glGetStringWidth(fontBig, cloned.equazioneCorrente);
			try {cloned.interpreta(true);} catch (Error e) {}
			PIDisplay.INSTANCE.replaceScreen(cloned);
			this.initialized = false;
			PIDisplay.INSTANCE.setScreen(this);
			
		}
	}

	public void typeChar(String chr) {
		int len = chr.length();
		nuovaEquazione=nuovaEquazione.substring(0, caretPos)+chr+nuovaEquazione.substring(caretPos, nuovaEquazione.length());
		caretPos+=len;
		scrollX = glGetStringWidth(fontBig, nuovaEquazione.substring(0, caretPos)+"|||");
		showCaret = true;
		showCaretDelta = 0L;
		afterDoNextStep = false;
		try {
			interpreta(true);
		} catch (Error e) {
		}
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
		es2.scrollX = es.scrollX;
		es2.nuovaEquazione = es.nuovaEquazione;
		es2.equazioneCorrente = es.equazioneCorrente;
		es2.showCaret = es.showCaret;
		es2.showCaretDelta = es.showCaretDelta;
		es2.caretPos = es.caretPos;
		es2.f = Utils.cloner.deepClone(es.f);
		es2.f2 = Utils.cloner.deepClone(es.f2);
		es2.resultsCount = es.resultsCount;
		es2.autoscroll = es.autoscroll;
		es2.errorLevel = es.errorLevel;
		es2.mustRefresh = es.mustRefresh;
		es2.afterDoNextStep = es.afterDoNextStep;
		return es2;
	}

}

package org.warp.picalculator.gui.screens;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Main;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.Keyboard;
import org.warp.picalculator.device.Keyboard.Key;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.math.AngleMode;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.functions.AnteriorFunction;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.FunctionMultipleValues;
import org.warp.picalculator.math.functions.FunctionTwoValues;
import org.warp.picalculator.math.functions.Variable;
import org.warp.picalculator.math.functions.Variable.VariableValue;
import org.warp.picalculator.math.functions.equations.Equation;
import org.warp.picalculator.math.functions.Number;

public class MathInputScreen extends Screen {

	public volatile String equazioneCorrente = "";
	public volatile String nuovaEquazione = "";
	public volatile int caretPos = 0;
	public volatile boolean showCaret = true;
	public volatile float showCaretDelta = 0f;
	public Calculator calc;
	public boolean autoscroll;
	public int scrollX = 0;
	public int errorLevel = 0; // 0 = nessuno, 1 = risultato, 2 = tutto
	boolean mustRefresh = true;
	boolean afterDoNextStep = false;

	public MathInputScreen() {
		super();
		canBeInHistory = true;

		calc = new Calculator();
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
		calc.init();
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
		final String eqn = nuovaEquazione;
		if (!temporary) {
			equazioneCorrente = eqn;
		}

		calc.parseInputString(eqn);
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

		if (DisplayManager.error == null) {
			DisplayManager.renderer.glClearColor(0xFFc5c2af);
		} else {
			DisplayManager.renderer.glClearColor(0xFFDC3C32);
		}
	}

	private static final BinaryFont fontBig = Utils.getFont(false);

	@Override
	public void renderStatusbar() {
		Renderer renderer = DisplayManager.renderer;
		renderer.glColor3f(1, 1, 1);
		final int pos = 2;
		final int spacersNumb = 1;
		int skinN = 0;
		if (calc.exactMode) {
			skinN = 22;
		} else {
			skinN = 21;
		}
		renderer.glFillRect(2 + 18 * pos + 2 * spacersNumb, 2, 16, 16, 16 * skinN, 16 * 0, 16, 16);
	}

	@Override
	public void render() {
		fontBig.use(DisplayManager.engine);
		final int textColor = 0xFF000000;
		final int padding = 4;
		DisplayManager.renderer.glColor(textColor);
		final int caretRealPos = MathematicalSymbols.getGraphicRepresentation(nuovaEquazione.substring(0, caretPos)).length() * (fontBig.getCharacterWidth() + 1);
		final String inputTextWithoutCaret = MathematicalSymbols.getGraphicRepresentation(nuovaEquazione);
		final boolean tooLongI = padding + fontBig.getStringWidth(nuovaEquazione) + padding >= Main.screenSize[0];
		int scrollI = 0;
		if (tooLongI) {
			scrollI = -scrollX;
			if (-scrollI >= Main.screenSize[0]) {
				scrollI += Main.screenSize[0];
			} else {
				scrollI = 0;
			}
		}
		DisplayManager.renderer.glDrawStringLeft(padding + scrollI, padding + 20, inputTextWithoutCaret);
		if (showCaret) {
			DisplayManager.renderer.glDrawStringLeft(padding + scrollI + caretRealPos, padding + 20, "|");
		}
		if (tooLongI) {
			DisplayManager.renderer.glColor(DisplayManager.renderer.glGetClearColor());
			DisplayManager.renderer.glFillColor(Main.screenSize[0] - 16 - 2, padding + 20, fontBig.getCharacterHeight(), Main.screenSize[0]);
			DisplayManager.renderer.glColor(textColor);
			DisplayManager.drawSkinPart(Main.screenSize[0] - 16, padding + 20 + fontBig.getCharacterHeight() / 2 - 16 / 2, 304, 0, 304 + 16, 16);
		}
		if (calc.f != null) {
			int topSpacing = 0;
			final Iterator<Function> iter = calc.f.iterator();
			while (iter.hasNext()) {
				final Function fnc = iter.next();
				try {
					final boolean tooLong = padding + fnc.getWidth() + padding >= Main.screenSize[0];
					int scrollA = 0;
					if (tooLong) {
						scrollA = -scrollX;
						if (-scrollA >= Main.screenSize[0]) {
							scrollA += Main.screenSize[0];
						} else {
							scrollA = 0;
						}
					}
					final int y = padding + 20 + padding + fontBig.getCharacterHeight() + 1 + topSpacing;
					fnc.draw(padding + scrollA, y);
					if (tooLong) {
						DisplayManager.renderer.glColor(DisplayManager.renderer.glGetClearColor());
						DisplayManager.renderer.glFillColor(Main.screenSize[0] - 16 - 2, y, fnc.getHeight(), Main.screenSize[0]);
						DisplayManager.renderer.glColor(textColor);
						DisplayManager.drawSkinPart(Main.screenSize[0] - 16, y + fnc.getHeight() / 2 - 16 / 2, 304, 0, 304 + 16, 16);
					}
				} catch (final NullPointerException e) {
					iter.remove();
				}
				topSpacing += fnc.getHeight() + 2;
			}
		}
		if (calc.f2 != null) {
			int bottomSpacing = 0;
			for (final Function f : calc.f2) {
				bottomSpacing += f.getHeight() + 2;
				f.draw(DisplayManager.engine.getWidth() - 2 - f.getWidth(), DisplayManager.engine.getHeight() - bottomSpacing);
			}
			if (calc.resultsCount > 1 && calc.resultsCount != calc.f2.size()) {
				final String resultsCountText = calc.resultsCount + " total results".toUpperCase();
				DisplayManager.renderer.glColor(0xFF9AAEA0);
				Utils.getFont(true).use(DisplayManager.engine);
				bottomSpacing += fontBig.getCharacterHeight() + 2;
				DisplayManager.renderer.glDrawStringRight(DisplayManager.engine.getWidth() - 2, DisplayManager.engine.getHeight() - bottomSpacing, resultsCountText);
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
					if (!afterDoNextStep) {
						try {
							try {
								interpreta(true);
								showVariablesDialog(() -> {
									equazioneCorrente = nuovaEquazione;
									calc.f2 = calc.f;
									afterDoNextStep = true;
									simplify();
								});
							} catch (final Exception ex) {
								if (Utils.debugOn) {
									ex.printStackTrace();
								}
								throw new Error(Errors.SYNTAX_ERROR);
							}
						} catch (final Error e) {
							final StringWriter sw = new StringWriter();
							final PrintWriter pw = new PrintWriter(sw);
							e.printStackTrace(pw);
							d.errorStackTrace = sw.toString().toUpperCase().replace("\t", "    ").replace("\r", "").split("\n");
							DisplayManager.error = e.id.toString();
							System.err.println(e.id);
						}
					} else {
						simplify();
					}
				}
				return true;
			case SOLVE:
				if (DisplayManager.error != null) {
					Utils.debug.println("Resetting after error...");
					DisplayManager.error = null;
					equazioneCorrente = null;
					calc.f = null;
					calc.f2 = null;
					calc.resultsCount = 0;
					return true;
				} else {
					try {
						try {
							if (afterDoNextStep) {
								simplify();
							} else {
								if (nuovaEquazione != equazioneCorrente && nuovaEquazione.length() > 0) {
									changeEquationScreen();
									interpreta(true);
									showVariablesDialog(() -> {
										equazioneCorrente = nuovaEquazione;
										solve();
									});
								}
							}
						} catch (final Exception ex) {
							if (Utils.debugOn) {
								ex.printStackTrace();
							}
							throw new Error(Errors.SYNTAX_ERROR);
						}
					} catch (final Error e) {
						final StringWriter sw = new StringWriter();
						final PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						d.errorStackTrace = sw.toString().toUpperCase().replace("\t", "    ").replace("\r", "").split("\n");
						DisplayManager.error = e.id.toString();
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
				typeChar(MathematicalSymbols.POWER + "2");
				return true;
			case POWER_OF_x:
				typeChar(MathematicalSymbols.POWER);
				return true;
			case PI:
				typeChar(MathematicalSymbols.PI);
				return true;
			case LETTER_X:
				typeChar(MathematicalSymbols.variables()[23]);
				return true;
			case LETTER_Y:
				typeChar(MathematicalSymbols.variables()[24]);
				return true;
			case SINE:
				typeChar(MathematicalSymbols.SINE);
				return true;
			case COSINE:
				typeChar(MathematicalSymbols.COSINE);
				return true;
			case TANGENT:
				typeChar(MathematicalSymbols.TANGENT);
				return true;
			case ARCSINE:
				typeChar(MathematicalSymbols.ARC_SINE);
				return true;
			case ARCCOSINE:
				typeChar(MathematicalSymbols.ARC_COSINE);
				return true;
			case ARCTANGENT:
				typeChar(MathematicalSymbols.ARC_TANGENT);
				return true;
			case DELETE:
				if (nuovaEquazione.length() > 0) {
					if (caretPos > 0) {
						caretPos -= 1;
						nuovaEquazione = nuovaEquazione.substring(0, caretPos) + nuovaEquazione.substring(caretPos + 1, nuovaEquazione.length());
					} else {
						nuovaEquazione = nuovaEquazione.substring(1);
					}
					try {
						interpreta(true);
					} catch (final Error e) {}
				}
				afterDoNextStep = false;
				return true;
			case LEFT:
				if (caretPos > 0) {
					caretPos -= 1;
				} else {
					caretPos = nuovaEquazione.length();
				}
				scrollX = fontBig.getStringWidth(nuovaEquazione.substring(0, caretPos) + "|||");
				showCaret = true;
				showCaretDelta = 0L;
				return true;
			case RIGHT:
				if (caretPos < nuovaEquazione.length()) {
					caretPos += 1;
				} else {
					caretPos = 0;
				}
				scrollX = fontBig.getStringWidth(nuovaEquazione.substring(0, caretPos) + "|||");
				showCaret = true;
				showCaretDelta = 0L;
				return true;
			case RESET:
				if (DisplayManager.error != null) {
					Utils.debug.println("Resetting after error...");
					DisplayManager.error = null;
					return true;
				} else {
					caretPos = 0;
					nuovaEquazione = "";
					afterDoNextStep = false;
					if (calc.f != null) {
						calc.f = new ArrayList<>();
					}
					return true;
				}
			case SURD_MODE:
				calc.exactMode = !calc.exactMode;
				if (calc.exactMode == false) {
					calc.f2 = solveExpression(calc.f2);
				} else {
					equazioneCorrente = "";
					Keyboard.keyPressed(Key.SOLVE);
				}
				return true;
			case debug1:
				DisplayManager.INSTANCE.setScreen(new EmptyScreen());
				return true;
			case HISTORY_BACK:
				if (DisplayManager.INSTANCE.canGoBack()) {
					if (equazioneCorrente != null && equazioneCorrente.length() > 0 & DisplayManager.sessions[DisplayManager.currentSession + 1] instanceof MathInputScreen) {
						nuovaEquazione = equazioneCorrente;
						try {
							interpreta(true);
						} catch (final Error e) {}
					}
				}
				return false;
			case HISTORY_FORWARD:
				if (DisplayManager.INSTANCE.canGoForward()) {
					if (equazioneCorrente != null && equazioneCorrente.length() > 0 & DisplayManager.sessions[DisplayManager.currentSession - 1] instanceof MathInputScreen) {
						nuovaEquazione = equazioneCorrente;
						try {
							interpreta(true);
						} catch (final Error e) {}
					}
				}
				return false;
			case debug_DEG:
				if (calc.angleMode.equals(AngleMode.DEG) == false) {
					calc.angleMode = AngleMode.DEG;
					return true;
				}
				return false;
			case debug_RAD:
				if (calc.angleMode.equals(AngleMode.RAD) == false) {
					calc.angleMode = AngleMode.RAD;
					return true;
				}
				return false;
			case debug_GRA:
				if (calc.angleMode.equals(AngleMode.GRA) == false) {
					calc.angleMode = AngleMode.GRA;
					return true;
				}
				return false;
			case DRG_CYCLE:
				if (calc.angleMode.equals(AngleMode.DEG) == true) {
					calc.angleMode = AngleMode.RAD;
				} else if (calc.angleMode.equals(AngleMode.RAD) == true) {
					calc.angleMode = AngleMode.GRA;
				} else {
					calc.angleMode = AngleMode.DEG;
				}
				return true;
			default:
				return false;
		}
	}

	private ArrayList<Function> solveExpression(ArrayList<Function> f22) {
		try {
			try {
				return calc.solveExpression(f22);
			} catch (final Exception ex) {
				if (Utils.debugOn) {
					ex.printStackTrace();
				}
				throw new Error(Errors.SYNTAX_ERROR);
			}
		} catch (final Error e) {
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			d.errorStackTrace = sw.toString().toUpperCase().replace("\t", "    ").replace("\r", "").split("\n");
			DisplayManager.error = e.id.toString();
			System.err.println(e.id);
		}
		return null;
	}

	protected void simplify() {
		try {
			try {
				showVariablesDialog();
				ArrayList<Function> results = new ArrayList<>();
				final ArrayList<Function> partialResults = new ArrayList<>();
				for (final Function f : calc.f2) {
					if (f instanceof Equation) {
						DisplayManager.INSTANCE.setScreen(new SolveEquationScreen(this));
					} else {
						results.add(f);
						for (final Function itm : results) {
							if (itm.isSolved() == false) {
								final List<Function> dt = itm.solveOneStep();
								partialResults.addAll(dt);
							} else {
								partialResults.add(itm);
							}
						}
						results = new ArrayList<>(partialResults);
						partialResults.clear();
					}
				}

				if (results.size() == 0) {
					calc.resultsCount = 0;
				} else {
					calc.resultsCount = results.size();
					Collections.reverse(results);
					// add elements to al, including duplicates
					final Set<Function> hs = new LinkedHashSet<>();
					hs.addAll(results);
					results.clear();
					results.addAll(hs);
					calc.f2 = results;
					for (final Function rf : calc.f2) {
						rf.generateGraphics();
					}
				}
				Utils.debug.println(calc.f2.toString());
			} catch (final Exception ex) {
				if (Utils.debugOn) {
					ex.printStackTrace();
				}
				throw new Error(Errors.SYNTAX_ERROR);
			}
		} catch (final Error e) {
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			d.errorStackTrace = sw.toString().toUpperCase().replace("\t", "    ").replace("\r", "").split("\n");
			DisplayManager.error = e.id.toString();
			System.err.println(e.id);
		}
	}

	protected void solve() {
		try {
			try {
				for (final Function f : calc.f) {
					if (f instanceof Equation) {
						DisplayManager.INSTANCE.setScreen(new SolveEquationScreen(this));
						return;
					}
				}

				final ArrayList<Function> results = solveExpression(calc.f);
				if (results.size() == 0) {
					calc.resultsCount = 0;
				} else {
					calc.resultsCount = results.size();
					Collections.reverse(results);
					// add elements to al, including duplicates
					final Set<Function> hs = new LinkedHashSet<>();
					hs.addAll(results);
					results.clear();
					results.addAll(hs);
					calc.f2 = results;
					for (final Function rf : calc.f2) {
						rf.generateGraphics();
					}
				}
			} catch (final Exception ex) {
				if (Utils.debugOn) {
					ex.printStackTrace();
				}
				throw new Error(Errors.SYNTAX_ERROR);
			}
		} catch (final Error e) {
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			d.errorStackTrace = sw.toString().toUpperCase().replace("\t", "    ").replace("\r", "").split("\n");
			DisplayManager.error = e.id.toString();
			System.err.println(e.id);
		}
	}

	private void changeEquationScreen() {
		if (equazioneCorrente != null && equazioneCorrente.length() > 0) {
			final MathInputScreen cloned = clone();
			cloned.caretPos = cloned.equazioneCorrente.length();
			cloned.nuovaEquazione = cloned.equazioneCorrente;
			cloned.scrollX = fontBig.getStringWidth(cloned.equazioneCorrente);
			try {
				cloned.interpreta(true);
			} catch (final Error e) {}
			DisplayManager.INSTANCE.replaceScreen(cloned);
			initialized = false;
			DisplayManager.INSTANCE.setScreen(this);

		}
	}

	public void typeChar(String chr) {
		final int len = chr.length();
		nuovaEquazione = nuovaEquazione.substring(0, caretPos) + chr + nuovaEquazione.substring(caretPos, nuovaEquazione.length());
		caretPos += len;
		scrollX = fontBig.getStringWidth(nuovaEquazione.substring(0, caretPos) + "|||");
		showCaret = true;
		showCaretDelta = 0L;
		afterDoNextStep = false;
		try {
			interpreta(true);
		} catch (final Error e) {}
//		f.clear(); //TODO: I removed this line to prevent input blanking when pressing EQUALS button and cloning this screen, but I must see why I created this part of code.
	}

	@Override
	public boolean keyReleased(Key k) {
		return false;
	}

	public void showVariablesDialog() {
		showVariablesDialog(null);
	}

	public void showVariablesDialog(final Runnable runnable) {
		final Thread ct = new Thread(() -> {
			final ArrayList<Function> variablesInFunctions = getVariables(calc.f.toArray(new Function[calc.f.size()]));
			for (final VariableValue f : calc.variablesValues) {
				if (variablesInFunctions.contains(f.v)) {
					variablesInFunctions.remove(f.v);
				}
			}

			boolean cancelled = false;
			for (final Function f : variablesInFunctions) {
				final ChooseVariableValueScreen cvs = new ChooseVariableValueScreen(this, new VariableValue((Variable) f, new Number(calc, 0)));
				DisplayManager.INSTANCE.setScreen(cvs);
				try {
					while (DisplayManager.screen == cvs) {
						Utils.debug.println(Thread.currentThread().getName());
						Thread.sleep(200);
					}
				} catch (final InterruptedException e) {}
				if (cvs.resultNumberValue == null) {
					cancelled = true;
					break;
				} else {
					final int is = calc.variablesValues.size();
					for (int i = 0; i < is; i++) {
						if (calc.variablesValues.get(i).v == f) {
							calc.variablesValues.remove(i);
						}
					}
					calc.variablesValues.add(new VariableValue((Variable) f, (Number) cvs.resultNumberValue));
				}
			}
			if (!cancelled) {
				if (runnable != null) {
					runnable.run();
				}
			}
		});
		ct.setName("Variables user-input queue thread");
		ct.setPriority(Thread.MIN_PRIORITY);
		ct.setDaemon(true);
		ct.start();
	}

	private ArrayList<Function> getVariables(Function[] fncs) {
		final ArrayList<Function> res = new ArrayList<>();
		for (final Function f : fncs) {
			if (f instanceof FunctionTwoValues) {
				res.addAll(getVariables(new Function[] { ((FunctionTwoValues) f).getVariable1(), ((FunctionTwoValues) f).getVariable2() }));
			} else if (f instanceof FunctionMultipleValues) {
				res.addAll(getVariables(((FunctionMultipleValues) f).getVariables()));
			} else if (f instanceof AnteriorFunction) {
				res.addAll(getVariables(new Function[] { ((AnteriorFunction) f).getVariable() }));
			} else if (f instanceof Variable) {
				if (!res.contains(f)) {
					res.add(f);
				}
			}
		}
		return res;
	}

	@Override
	public MathInputScreen clone() {
		final MathInputScreen es = this;
		final MathInputScreen es2 = new MathInputScreen();
		es2.scrollX = es.scrollX;
		es2.nuovaEquazione = es.nuovaEquazione;
		es2.equazioneCorrente = es.equazioneCorrente;
		es2.showCaret = es.showCaret;
		es2.showCaretDelta = es.showCaretDelta;
		es2.caretPos = es.caretPos;
//		es2.calc.f = Utils.cloner.deepClone(es.calc.f);
//		es2.calc.f2 = Utils.cloner.deepClone(es.calc.f2);
//		es2.calc.resultsCount = es.calc.resultsCount;
		es2.autoscroll = es.autoscroll;
		es2.errorLevel = es.errorLevel;
		es2.mustRefresh = es.mustRefresh;
		es2.afterDoNextStep = es.afterDoNextStep;
//		es2.calc.variablesValues = Utils.cloner.deepClone(es.calc.variablesValues);
		es2.calc = Utils.cloner.deepClone(es.calc);
		return es2;
	}

}

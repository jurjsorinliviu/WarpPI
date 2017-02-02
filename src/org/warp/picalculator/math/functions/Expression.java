package org.warp.picalculator.math.functions;

import static org.warp.picalculator.Utils.ArrayToRegex;
import static org.warp.picalculator.Utils.concat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Utils;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.functions.trigonometry.ArcCosine;
import org.warp.picalculator.math.functions.trigonometry.ArcSine;
import org.warp.picalculator.math.functions.trigonometry.ArcTangent;
import org.warp.picalculator.math.functions.trigonometry.Cosine;
import org.warp.picalculator.math.functions.trigonometry.Sine;
import org.warp.picalculator.math.functions.trigonometry.Tangent;

public class Expression extends FunctionMultipleValues {

	public Expression(Calculator root) {
		super(root);
	}

	public Expression(Calculator root, Function[] values) {
		super(root, values);
	}

	public Expression(Calculator root, Function value) {
		super(root, new Function[]{value});
	}

	private boolean initialParenthesis = false;

	public Expression(Calculator root, String string) throws Error {
		this(root, string, "", true);
	}

	public Expression(Calculator root, String string, String debugSpaces, boolean initialParenthesis) throws Error {
		super(root);
		this.initialParenthesis = initialParenthesis;
		boolean isNumber = false;

		// Determine if the expression is already a number:
		// Determina se l'espressione è già un numero:
		try {
			new Number(root, string);
			isNumber = true;
		} catch (final NumberFormatException ex) {
			isNumber = false;
		}

		String processExpression = string;
		Utils.debug.println(debugSpaces + "•Analyzing expression:" + processExpression);

		isNumber = false; //TODO: rimuovere isNumber, alcune semplificazione come la divisione per zero altrimenti verrebbero saltate.

		if (isNumber) {
			// If the expression is already a number:
			// Se l'espressione è già un numero:
			final Number t = new Number(root, string);
			setVariables(new Function[] { t });
			Utils.debug.println(debugSpaces + "•Result:" + t.toString());
		} else {
			// Else prepare the expression:
			// Altrimenti prepara l'espressione:
			debugSpaces += "  ";

			// IF the expression is not a number:
			// Se l'espressione non è già un numero:

			// Check if there are more than one equal symbol (=)
			// Controlla se ci sono più di un uguale (=)
			int equationsFound = 0;
			int systemsFound = 0;
			for (final char c : processExpression.toCharArray()) {
				if (("" + c).equals(MathematicalSymbols.EQUATION)) {
					equationsFound += 1;
				}
				if (("" + c).equals(MathematicalSymbols.SYSTEM)) {
					equationsFound += 1;
				}
			}
			if (equationsFound == 1 && systemsFound == 0) {
				processExpression = MathematicalSymbols.SYSTEM + processExpression;
				systemsFound += 1;
			}
			if (equationsFound != systemsFound) {
				throw new Error(Errors.SYNTAX_ERROR);
			}

			//Solve the exceeding symbols ++ and --
			// Correggi i segni ++ e -- in eccesso
			Pattern pattern = Pattern.compile("\\+\\++?|\\-\\-+?");
			Matcher matcher = pattern.matcher(processExpression);
			boolean symbolsChanged = false;
			while (matcher.find()) {
				symbolsChanged = true;
				final String correzione = "+";
				processExpression = processExpression.substring(0, matcher.start(0)) + correzione + processExpression.substring(matcher.start(0) + matcher.group(0).length(), processExpression.length());
				matcher = pattern.matcher(processExpression);
			}

			// Correct the exceeding symbols +- and -+
			// Correggi i segni +- e -+ in eccesso
			pattern = Pattern.compile("\\+\\-|\\-\\+");
			matcher = pattern.matcher(processExpression);
			while (matcher.find()) {
				symbolsChanged = true;
				final String correzione = "-";
				processExpression = processExpression.substring(0, matcher.start(0)) + correzione + processExpression.substring(matcher.start(0) + matcher.group(0).length(), processExpression.length());
				matcher = pattern.matcher(processExpression);
			}

			// Rimuovi i segni appena dopo le parentesi
			if (processExpression.contains("(+")) {
				symbolsChanged = true;
				processExpression = processExpression.replace("(+", "(");
			}

//			// Cambia i segni appena prima le parentesi
//			if (processExpression.contains("-(")) {
//				symbolsChanged = true;
//				processExpression = processExpression.replace("-(", "-1*(");
//			}

			// Rimuovi i segni appena dopo l'inizio
			if (processExpression.startsWith("+")) {
				symbolsChanged = true;
				processExpression = processExpression.substring(1, processExpression.length());
			}

			// Rimuovi i + in eccesso
			pattern = Pattern.compile("[" + ArrayToRegex(Utils.add(concat(MathematicalSymbols.signums(true), MathematicalSymbols.functions()), "(")) + "]\\+[^" + ArrayToRegex(concat(concat(MathematicalSymbols.signums(true), MathematicalSymbols.functions()), new String[] { "(", ")" })) + "]+?[" + ArrayToRegex(concat(MathematicalSymbols.signums(true), MathematicalSymbols.functions())) + "]|[" + ArrayToRegex(concat(MathematicalSymbols.signums(true), MathematicalSymbols.functions())) + "]+?\\+[^" + ArrayToRegex(concat(concat(MathematicalSymbols.signums(true), MathematicalSymbols.functions()), new String[] { "(", ")" })) + "]");
			matcher = pattern.matcher(processExpression);
			symbolsChanged = false;
			while (matcher.find()) {
				symbolsChanged = true;
				final String correzione = matcher.group(0).replaceFirst(Matcher.quoteReplacement("+"), "");
				processExpression = processExpression.substring(0, matcher.start(0) + 1) + correzione + processExpression.substring(matcher.start(0) + matcher.group(0).length(), processExpression.length());
				matcher = pattern.matcher(processExpression);
			}

			// Correggi i segni - in −
			processExpression = processExpression.replace("-", MathematicalSymbols.SUBTRACTION);

			// Correggi i segni − dopo di espressioni o funzioni SN in -
			pattern = Pattern.compile("[" + Utils.ArrayToRegex(concat(concat(MathematicalSymbols.functions(), new String[] { MathematicalSymbols.PARENTHESIS_OPEN }), MathematicalSymbols.signums(true))) + "]" + MathematicalSymbols.SUBTRACTION);
			matcher = pattern.matcher(processExpression);
			while (matcher.find()) {
				symbolsChanged = true;
				final String correzione = MathematicalSymbols.MINUS;
				processExpression = processExpression.substring(0, matcher.start(0) + 1) + correzione + processExpression.substring(matcher.start(0) + 2, processExpression.length());
				matcher = pattern.matcher(processExpression);
			}

			// Cambia il segno iniziale − in -
			if (processExpression.startsWith("−")) {
				symbolsChanged = true;
				processExpression = "-" + processExpression.substring(1, processExpression.length());
			}

			if (symbolsChanged) {
				Utils.debug.println(debugSpaces + "•Resolved signs:" + processExpression);
			}

//			// Aggiungi le parentesi implicite per le potenze con una incognita
//			pattern = Pattern.compile("(?<!(?:\\(|^))(["+Utils.ArrayToRegex(MathematicalSymbols.variables())+"]+"+MathematicalSymbols.POWER+"[^" + Utils.ArrayToRegex(Utils.add(concat(MathematicalSymbols.functionsNSN(), concat(MathematicalSymbols.signums(true), MathematicalSymbols.genericSyntax())), ")")) + "])(?!\\))");
//			matcher = pattern.matcher(processExpression);
//			symbolsChanged = false;
//			while (matcher.find()) {
//				symbolsChanged = true;
//				String correzione = matcher.group().replace(MathematicalSymbols.POWER, "⑴");
//				processExpression = processExpression.substring(0, matcher.start(0)) + correzione + processExpression.substring(matcher.start(0) + matcher.group(0).length(), processExpression.length());
//				matcher = pattern.matcher(processExpression);
//			}
//
//			processExpression = processExpression.replace("⑴", MathematicalSymbols.POWER);

			// Aggiungi i segni * accanto alle parentesi
			pattern = Pattern.compile("\\([^\\(]+?\\)");
			matcher = pattern.matcher(processExpression);
			while (matcher.find()) {
				symbolsChanged = true;
				// sistema i segni * impliciti prima e dopo l'espressione.
				String beforeexp = processExpression.substring(0, matcher.start(0));
				final String newexp = matcher.group(0).substring(1, matcher.group(0).length() - 1);
				String afterexp = processExpression.substring(matcher.start(0) + matcher.group(0).length(), processExpression.length());
				if (Pattern.compile("[^\\-" + Utils.ArrayToRegex(Utils.add(concat(MathematicalSymbols.functions(), concat(MathematicalSymbols.signums(true), MathematicalSymbols.genericSyntax())), "(")) + "]$").matcher(beforeexp).find()) {
					// Se la stringa precedente finisce con un numero
					beforeexp += MathematicalSymbols.MULTIPLICATION;
				}
				if (Pattern.compile("^[^\\-" + Utils.ArrayToRegex(Utils.add(concat(MathematicalSymbols.functions(), concat(MathematicalSymbols.signums(true), MathematicalSymbols.genericSyntax())), ")")) + "]").matcher(afterexp).find()) {
					// Se la stringa successiva inizia con un numero
					afterexp = MathematicalSymbols.MULTIPLICATION + afterexp;
				}
				processExpression = beforeexp + "⑴" + newexp + "⑵" + afterexp;
				matcher = pattern.matcher(processExpression);
			}

			processExpression = processExpression.replace("⑴", "(").replace("⑵", ")");

			if (symbolsChanged) {
				Utils.debug.println(debugSpaces + "•Added implicit multiplications:" + processExpression);
			}

			Utils.debug.println(debugSpaces + "•Subdivision in classes:");

			debugSpaces += "  ";

			// Convert the expression to a list of objects
			final Expression imputRawParenthesis = new Expression(root);
			imputRawParenthesis.setVariables(new Function[] {});
			String tmp = "";
			final String[] functions = concat(concat(concat(concat(MathematicalSymbols.functions(), MathematicalSymbols.parentheses()), MathematicalSymbols.signums(true)), MathematicalSymbols.variables()), MathematicalSymbols.genericSyntax());
			for (int i = 0; i < processExpression.length(); i++) {
				// Per ogni carattere cerca se è un numero o una funzione:
				final String charI = processExpression.charAt(i) + "";
				if (Utils.isInArray(charI, functions)) {

					// Finds the type of function fron the following list
					// Cerca il tipo di funzione tra le esistenti
					Function f = null;
					switch (charI) {
						case MathematicalSymbols.SUM:
							f = new Sum(root, null, null);
							break;
						case MathematicalSymbols.SUM_SUBTRACTION:
							f = new SumSubtraction(root, null, null);
							break;
						case MathematicalSymbols.SUBTRACTION:
							f = new Subtraction(root, null, null);
							break;
						case MathematicalSymbols.MINUS:
							f = new Negative(root, null);
							break;
						case MathematicalSymbols.MULTIPLICATION:
							f = new Multiplication(root, null, null);
							break;
						case MathematicalSymbols.DIVISION:
							f = new Division(root, null, null);
							break;
						case MathematicalSymbols.NTH_ROOT:
							f = new Root(root, null, null);
							break;
						case MathematicalSymbols.SQUARE_ROOT:
							f = new RootSquare(root, null);
							break;
						case MathematicalSymbols.POWER:
							f = new Power(root, null, null);
							break;
						case MathematicalSymbols.SINE:
							f = new Sine(root, null);
							break;
						case MathematicalSymbols.COSINE:
							f = new Cosine(root, null);
							break;
						case MathematicalSymbols.TANGENT:
							f = new Tangent(root, null);
							break;
						case MathematicalSymbols.ARC_SINE:
							f = new ArcSine(root, null);
							break;
						case MathematicalSymbols.ARC_COSINE:
							f = new ArcCosine(root, null);
							break;
						case MathematicalSymbols.ARC_TANGENT:
							f = new ArcTangent(root, null);
							break;
						case MathematicalSymbols.PARENTHESIS_OPEN:
							// Find the last closed parenthesis
							// cerca l'ultima parentesi chiusa
							int startIndex = i;
							int endIndex = -1;
							int jumps = -1;
							for (int i2 = startIndex; i2 < processExpression.length(); i2++) {
								if ((processExpression.charAt(i2) + "").equals(MathematicalSymbols.PARENTHESIS_CLOSE)) {
									if (jumps == 0) {
										endIndex = i2;
										break;
									} else if (jumps > 0) {
										jumps -= 1;
									} else if (jumps < 0) {
										throw new Error(Errors.UNBALANCED_BRACKETS);
									}
								} else if ((processExpression.charAt(i2) + "").equals(MathematicalSymbols.PARENTHESIS_OPEN)) {
									jumps += 1;
								}
							}
							if (endIndex == -1 || endIndex < startIndex) {
								throw new Error(Errors.UNBALANCED_BRACKETS);
							}
							startIndex += 1;
							i = startIndex;

							String tmpExpr = "";
							while (i < endIndex) {
								tmpExpr += processExpression.charAt(i);
								i++;
							}
							f = new Expression(root, tmpExpr, debugSpaces, false);
							break;
						default:
							if (Utils.isInArray(charI, MathematicalSymbols.variables())) {
								f = new Variable(root, charI);
							} else {
								if (charI == "(" || charI == ")") {
									throw new Error(Errors.UNBALANCED_BRACKETS);
								} else {
									System.err.println("Unexpected character while parsing expression: " + charI);
									throw new Error(Errors.SYNTAX_ERROR);
								}
//								throw new java.lang.RuntimeException("Il carattere " + charI + " non è tra le funzioni designate!\nAggiungerlo ad esse o rimuovere il carattere dall'espressione!");
							}
					}
					if (f instanceof Expression) {
						tmp = "";
					} else if (f instanceof Variable) {
						if (imputRawParenthesis.getVariablesLength() == 0) {
							if (tmp.length() > 0) {
								imputRawParenthesis.addFunctionToEnd(new Number(root, tmp));
								Utils.debug.println(debugSpaces + "•Added number to expression:" + tmp);
								imputRawParenthesis.addFunctionToEnd(new Multiplication(root, null, null));
								Utils.debug.println(debugSpaces + "•Added multiplication to expression:" + new Multiplication(root, null, null).getSymbol());
							}
						} else {
							final Function precedentFunction = imputRawParenthesis.getVariable(imputRawParenthesis.getVariablesLength() - 1);
							if (tmp.length() > 0) {
								if (precedentFunction instanceof Number || precedentFunction instanceof Variable) {
									imputRawParenthesis.addFunctionToEnd(new Multiplication(root, null, null));
									Utils.debug.println(debugSpaces + "•Added multiplication to expression:" + new Multiplication(root, null, null).getSymbol());
								}
								if (tmp.equals("-")) {
									imputRawParenthesis.addFunctionToEnd(new Subtraction(root, null, null));
								} else {
									imputRawParenthesis.addFunctionToEnd(new Number(root, tmp));
									Utils.debug.println(debugSpaces + "•Added number to expression:" + tmp);
								}
							}
							if (tmp.length() > 0 || (precedentFunction instanceof Number || precedentFunction instanceof Variable)) {
								imputRawParenthesis.addFunctionToEnd(new Multiplication(root, null, null));
								Utils.debug.println(debugSpaces + "•Added multiplication to expression:" + new Multiplication(root, null, null).getSymbol());
							}
						}
					} else {
						if (tmp.length() != 0) {
							if (tmp.equals("-")) {
								if (tmp.equals("-")) {
									tmp = "-1";
								}
							}
							imputRawParenthesis.addFunctionToEnd(new Number(root, tmp));
							Utils.debug.println(debugSpaces + "•Added number to expression:" + tmp);
						}
					}
					imputRawParenthesis.addFunctionToEnd(f);
					Utils.debug.println(debugSpaces + "•Added variable to expression:" + f.getSymbol() + (f instanceof Number ? " (number)" : " (variable)"));
					tmp = "";
				} else {
					try {
						if (charI.equals("-") == false && charI.equals(".") == false) {
							new BigDecimal(tmp + charI);
						}
						// Se il carattere è un numero intero, un segno
						// negativo, o un punto
						tmp += charI;
					} catch (final Exception exc) {
						throw new java.lang.RuntimeException("Il carattere " + tmp + charI + " non è nè un numero nè un espressione presente nella lista completa!\nAggiungerlo ad essa o rimuovere il carattere dall'espressione!");
					}
				}
			}
			if (tmp.length() > 0) {
				Utils.debug.println(debugSpaces + "•Added variable to expression:" + tmp);
				try {
					imputRawParenthesis.addFunctionToEnd(new Number(root, tmp));
				} catch (final NumberFormatException ex) {
					throw new Error(Errors.SYNTAX_ERROR);
				}
				tmp = "";
			}

			int dsl = debugSpaces.length();
			debugSpaces = "";
			for (int i = 0; i < dsl - 2; i++) {
				debugSpaces += " ";
			}
			Utils.debug.println(debugSpaces + "•Finished the subdivision in classes.");
			// Fine suddivisione di insieme

			Utils.debug.println(debugSpaces + "•Removing useless parentheses");
			for (int i = 0; i < imputRawParenthesis.functions.length; i++) {
				if (imputRawParenthesis.functions[i] instanceof Expression) {
					final Expression par = (Expression) imputRawParenthesis.functions[i];
					if (par.functions.length == 1) {
						final Function subFunz = par.functions[0];
						if (subFunz instanceof Expression || subFunz instanceof Number || subFunz instanceof Variable) {
							imputRawParenthesis.functions[i] = subFunz;
							Utils.debug.println(debugSpaces + "  •Useless parentheses removed");
						}
					}
				}
			}

			// Inizia l'affinazione dell'espressione
			Utils.debug.println(debugSpaces + "•Pushing classes...");

			final Function[] oldFunctionsArray = imputRawParenthesis.getVariables();
			final ArrayList<Function> oldFunctionsList = new ArrayList<>();
			for (int i = 0; i < oldFunctionsArray.length; i++) {
				Function funzione = oldFunctionsArray[i];
				if (funzione != null) {
					//Affinazione
					if (funzione instanceof Root) {
						if ((i - 1) >= 0 && oldFunctionsArray[i - 1] instanceof Number && ((Number) oldFunctionsArray[i - 1]).getTerm().compareTo(new BigDecimal(2)) == 0) {
							oldFunctionsArray[i] = null;
							oldFunctionsArray[i - 1] = null;
							oldFunctionsList.remove(oldFunctionsList.size() - 1);
							i -= 1;
							funzione = new RootSquare(root, null);
						}
					}
					//Aggiunta della funzione alla lista grezza
					oldFunctionsList.add(funzione);
				}
			}

			if (oldFunctionsList.size() > 1) {
				Utils.debug.println(debugSpaces + "  •Correcting classes:");

				int before = 0;
				String step = "SN Functions";
				int n = 0;
				do {
					before = oldFunctionsList.size();
					int i = 0;
					boolean change = false;
					if (Utils.areThereOnlyEmptySNFunctions(oldFunctionsList)) {
						step = "SN Functions"; // SECONDA FASE
					} else if (Utils.areThereOnlyEmptyNSNFunctions(oldFunctionsList)) {
						step = "NSN Functions"; // TERZA FASE
					} else if (Utils.areThereEmptyMultiplications(oldFunctionsList)) {
						step = "multiplications"; // QUARTA FASE
					} else if (Utils.areThereEmptySums(oldFunctionsList)) {
						step = "sums"; // QUINTA FASE
					} else {
//						fase = "errore";
						System.out.println("WARN: ---> POSSIBILE ERRORE????? <---");// BOH
//						throw new Errore(Errori.SYNTAX_ERROR);
						while (oldFunctionsList.size() > 1) {
							oldFunctionsList.set(0, new Multiplication(root, oldFunctionsList.get(0), oldFunctionsList.remove(1)));
						}
					}
					Utils.debug.println(debugSpaces + "  •Phase: " + step);
					while (i < oldFunctionsList.size() && change == false && oldFunctionsList.size() > 1) {
						final Function funzioneTMP = oldFunctionsList.get(i);
						if (funzioneTMP instanceof FunctionTwoValues) {
							if (step != "SN Functions") {
								if ((step == "sums" && (funzioneTMP instanceof Sum || funzioneTMP instanceof SumSubtraction || funzioneTMP instanceof Subtraction) == true && ((funzioneTMP instanceof AnteriorFunction && ((AnteriorFunction) funzioneTMP).variable == null) || (funzioneTMP instanceof FunctionTwoValues && ((FunctionTwoValues) funzioneTMP).variable1 == null && ((FunctionTwoValues) funzioneTMP).variable2 == null) || (!(funzioneTMP instanceof AnteriorFunction) && !(funzioneTMP instanceof FunctionTwoValues)))) || (step.equals("multiplications") && ((funzioneTMP instanceof Multiplication) || (funzioneTMP instanceof Division)) && ((FunctionTwoValues) funzioneTMP).variable1 == null && ((FunctionTwoValues) funzioneTMP).variable2 == null) || (step == "NSN Functions" && (funzioneTMP instanceof Sum) == false && (funzioneTMP instanceof SumSubtraction) == false && (funzioneTMP instanceof Subtraction) == false && (funzioneTMP instanceof Multiplication) == false && (funzioneTMP instanceof Division) == false && ((funzioneTMP instanceof AnteriorFunction && ((AnteriorFunction) funzioneTMP).variable == null) || (funzioneTMP instanceof FunctionTwoValues && ((FunctionTwoValues) funzioneTMP).variable1 == null && ((FunctionTwoValues) funzioneTMP).variable2 == null) || (!(funzioneTMP instanceof AnteriorFunction) && !(funzioneTMP instanceof FunctionTwoValues))))) {
									change = true;

									if (i + 1 < oldFunctionsList.size() && i - 1 >= 0) {
										((FunctionTwoValues) funzioneTMP).setVariable1(oldFunctionsList.get(i - 1));
										((FunctionTwoValues) funzioneTMP).setVariable2(oldFunctionsList.get(i + 1));
										oldFunctionsList.set(i, funzioneTMP);

										// è importante togliere prima gli elementi
										// in fondo e poi quelli davanti, perché gli
										// indici scalano da destra a sinistra.
										oldFunctionsList.remove(i + 1);
										oldFunctionsList.remove(i - 1);

										Utils.debug.println(debugSpaces + "  •Set variable to expression:" + funzioneTMP.getSymbol());
										try {
											Utils.debug.println(debugSpaces + "    " + "var1=" + ((FunctionTwoValues) funzioneTMP).getVariable1().toString());
										} catch (final NullPointerException ex2) {}
										try {
											Utils.debug.println(debugSpaces + "    " + "var2=" + ((FunctionTwoValues) funzioneTMP).getVariable2().toString());
										} catch (final NullPointerException ex2) {}
										try {
											Utils.debug.println(debugSpaces + "    " + "(result)=" + ((FunctionTwoValues) funzioneTMP).toString());
										} catch (final NullPointerException ex2) {}

									} else {
										throw new Error(Errors.SYNTAX_ERROR);
									}
								}
							}
						} else if (funzioneTMP instanceof AnteriorFunction) {
							if ((step == "SN Functions" && ((AnteriorFunction) funzioneTMP).variable == null)) {
								if (i + 1 < oldFunctionsList.size()) {
									final Function nextFunc = oldFunctionsList.get(i + 1);
									if (nextFunc instanceof AnteriorFunction && ((AnteriorFunction) nextFunc).variable == null) {

									} else {
										change = true;
										((AnteriorFunction) funzioneTMP).setVariable(nextFunc);
										oldFunctionsList.set(i, funzioneTMP);

										// è importante togliere prima gli elementi in
										// fondo e poi quelli davanti, perché gli indici
										// scalano da destra a sinistra.
										oldFunctionsList.remove(i + 1);

										Utils.debug.println(debugSpaces + "  •Set variable to expression:" + funzioneTMP.getSymbol());
										final Function var = ((AnteriorFunction) funzioneTMP).getVariable();
										if (var == null) {
											Utils.debug.println(debugSpaces + "    " + "var=null");
										} else {
											Utils.debug.println(debugSpaces + "    " + "var=" + var.toString());
										}
									}
								} else {
									throw new Error(Errors.SYNTAX_ERROR);
								}
							}
						} else if (funzioneTMP instanceof Number || funzioneTMP instanceof Variable || funzioneTMP instanceof Expression) {
							if (n < 300) {
								// Utils.debug.println(debugSpaces+" •Set variable
								// to number:"+funzioneTMP.calcola());
							}
						} else {
							throw new java.lang.RuntimeException("Tipo sconosciuto");
						}
						i++;
						n++;
					}
				} while (((oldFunctionsList.size() != before || step != "sums") && oldFunctionsList.size() > 1));
			}
			if (oldFunctionsList.isEmpty()) {
				setVariables(new Function[] { new Number(root, 0) });
			} else {
				setVariables(oldFunctionsList);
			}

			dsl = debugSpaces.length();
			debugSpaces = "";
			for (int i = 0; i < dsl - 2; i++) {
				debugSpaces += " ";
			}
			Utils.debug.println(debugSpaces + "•Finished correcting classes.");

			final String result = toString();
			Utils.debug.println(debugSpaces + "•Result:" + result);
		}
	}

	@Override
	public String getSymbol() {
		return "Parentesi";
	}

	@Override
	protected boolean isSolvable() {
		if (functions.length > 1) {
			return true;
		} else if (functions.length == 1) {
			final Function f = functions[0];
			if (f.isSolved() == false) {
				return true;
			} else {
				return !parenthesisNeeded();
			}
		}
		return false;
	}

	@Override
	public List<Function> solveOneStep() throws Error {
		final List<Function> ret = new ArrayList<>();
		if (functions.length == 1) {
			if (functions[0].isSolved() || !parenthesisNeeded()) {
				ret.add(functions[0]);
				return ret;
			} else {
				final List<Function> l = functions[0].solveOneStep();
				for (final Function f : l) {
					if (f instanceof Number || f instanceof Variable) {
						ret.add(f);
					} else {
						ret.add(new Expression(root, new Function[] { f }));
					}
				}
				return ret;
			}
		} else {
			for (final Function f : functions) {
				if (f.isSolved() == false) {
					final List<Function> partial = f.solveOneStep();
					for (final Function fnc : partial) {
						ret.add(new Expression(root, new Function[] { fnc }));
					}
				}
			}
			return ret;
		}
	}

	@Override
	public void generateGraphics() {
		for (final Function var : functions) {
			var.setSmall(small);
			var.generateGraphics();
		}

		width = calcWidth();
		height = calcHeight();
		line = calcLine();
	}

	public boolean parenthesisNeeded() {
		boolean parenthesisneeded = true;
		if (initialParenthesis) {
			parenthesisneeded = false;
		} else {
			if (functions.length == 1) {
				final Function f = functions[0];
				if (f instanceof Number || f instanceof Variable || f instanceof Expression || f instanceof Division || f instanceof Joke || f instanceof Undefined || f instanceof Power || f instanceof Sine || f instanceof Cosine || f instanceof Tangent || f instanceof ArcSine || f instanceof ArcCosine || f instanceof ArcTangent || f instanceof RootSquare) {
					parenthesisneeded = false;
				}
				if (f instanceof Multiplication) {
					if (((Multiplication) f).getVariable1() instanceof Number) {
						parenthesisneeded = !(((Multiplication) f).getVariable2() instanceof Variable);
					} else if (((Multiplication) f).getVariable2() instanceof Number) {
						parenthesisneeded = !(((Multiplication) f).getVariable1() instanceof Variable);
					} else if (((Multiplication) f).getVariable1() instanceof Variable || ((Multiplication) f).getVariable2() instanceof Variable) {
						parenthesisneeded = false;
					}
				}
			}
		}
		return parenthesisneeded;
	}

	@Override
	public void draw(int x, int y) {
		if (parenthesisNeeded() == false) {
			functions[0].draw(x, y);
		} else {
			final float miny = y;
			final float maxy = y + getHeight();
			final int h = getHeight();
			x += 1;
			DisplayManager.renderer.glDrawLine(x, y + 2, x, y + 2);
			DisplayManager.renderer.glDrawLine( x + 1, y + 1, x + 1, y + 1);
			DisplayManager.renderer.glDrawLine( x + 2, y, x + 2, y);
			
			DisplayManager.renderer.glDrawLine(x, y + 2, x, y + h - 3);
			
			DisplayManager.renderer.glDrawLine(x, y + h - 3, x, y + h - 3);
			DisplayManager.renderer.glDrawLine(x + 1, y + h - 2, x + 1, y + h - 2);
			DisplayManager.renderer.glDrawLine(x + 2, y + h - 1, x + 2, y + h - 1);
			x += 4;
			for (final Function f : functions) {
				final float fheight = f.getHeight();
				final float y2 = miny + ((maxy - miny) / 2 - fheight / 2);
				f.draw(x, (int) y2);
				x += f.getWidth();
			}
			x += 2;
			DisplayManager.renderer.glDrawLine(x, y, x, y);
			DisplayManager.renderer.glDrawLine(x + 1, y + 1, x + 1, y + 1);
			DisplayManager.renderer.glDrawLine(x + 2, y + 2, x + 2, y + 2);
			
			DisplayManager.renderer.glDrawLine(x + 2, y + 2, x + 2, y + h - 3);

			DisplayManager.renderer.glDrawLine(x, y + h - 1, x, y + h - 1);
			DisplayManager.renderer.glDrawLine(x + 1, y + h - 2, x + 1, y + h - 2);
			DisplayManager.renderer.glDrawLine(x + 2, y + h - 3, x + 2, y + h - 3);
			x += 4;
		}
	}

	@Override
	public int getWidth() {
		return width;
	}

	private int calcWidth() {
		if (parenthesisNeeded() == false) {
			return functions[0].getWidth();
		} else {
			int w = 0;
			for (final Function f : functions) {
				w += f.getWidth();
			}
			return 1 + 4 + w + 2 + 4;
		}
	}

	@Override
	public int getHeight() {
		return height;
	}

	private int calcHeight() {
		if (initialParenthesis || functions.length == 1) {
			return functions[0].getHeight();
		} else {
			Function tmin = null;
			Function tmax = null;
			for (final Function t : functions) {
				if (tmin == null || t.getLine() >= tmin.getLine()) {
					tmin = t;
				}
				if (tmax == null || t.getHeight() - t.getLine() >= tmax.getHeight() - tmax.getLine()) {
					tmax = t;
				}
			}
			if (tmin == null) {
				return Utils.getFontHeight(small);
			}
			return tmin.getLine() + tmax.getHeight() - tmax.getLine();
		}
	}

	@Override
	public int getLine() {
		return line;
	}

	private int calcLine() {
		if (initialParenthesis || functions.length == 1) {
			return functions[0].getLine();
		} else {
			Function tl = null;
			for (final Function t : functions) {
				if (tl == null || t.getLine() >= tl.getLine()) {
					tl = t;
				}
			}
			if (tl == null) {
				return Utils.getFontHeight(small) / 2;
			}
			return tl.getLine();
		}
	}

	@Override
	public String toString() {
		String vars = "null";
		if (functions != null && functions.length > 0) {
			if (functions.length == 1) {
				if (functions[0] != null) {
					vars = functions[0].toString();
				}
			} else {
				for (final Function variable : functions) {
					if (variable != null) {
						vars += ", " + variable.toString();
					}
				}
				vars = vars.substring(2);
			}
		}
		return "(" + vars + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Expression) {
			final Expression f = (Expression) o;
			final Function[] exprFuncs1 = functions;
			final Function[] exprFuncs2 = f.functions;
			if (exprFuncs1.length == exprFuncs2.length) {
				for (int i = 0; i < exprFuncs1.length; i++) {
					if (exprFuncs1[i].equals(exprFuncs2[i]) == false) {
						return false;
					}
				}
				return true;
			}
		} else if (o != null & getVariablesLength() == 1) {
			final Function f = (Function) o;
			return (functions[0].equals(f));
		} else if (o == null & getVariablesLength() == 0) {
			return true;
		}
		return false;
	}

}

package org.warp.picalculator.math.functions;

import static org.warp.picalculator.Utils.ArrayToRegex;
import static org.warp.picalculator.Utils.concat;
import static org.warp.picalculator.device.graphicengine.Display.Render.glColor3f;
import static org.warp.picalculator.device.graphicengine.Display.Render.glDrawLine;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nevec.rjm.NumeroAvanzato;
import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.Variable;
import org.warp.picalculator.math.Variables;

public class Expression extends FunctionMultipleValues {

	public Expression() {
		super();
	}

	public Expression(Function[] values) {
		super(values);
	}

	private boolean initialParenthesis = false;

	public Expression(String string) throws Error {
		this(string, "", true);
	}

	public Expression(String string, String debugSpaces, boolean initialParenthesis) throws Error {
		super();
		this.initialParenthesis = initialParenthesis;
		boolean isNumber = false;

		// Determine if the expression is already a number:
		// Determina se l'espressione è già un numero:
		try {
			new Number(string);
			isNumber = true;
		} catch (NumberFormatException ex) {
			isNumber = false;
		}
		
		String processExpression = string;
		Utils.debug.println(debugSpaces + "•Analyzing expression:" + processExpression);
		
		if (isNumber){
			// If the expression is already a number:
			// Se l'espressione è già un numero:
			Number t = new Number(string);
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
			for (char c : processExpression.toCharArray()) {
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
				String correzione = "+";
				processExpression = processExpression.substring(0, matcher.start(0) + 1) + correzione + processExpression.substring(matcher.start(0) + matcher.group(0).length(), processExpression.length());
				matcher = pattern.matcher(processExpression);
			}

			// Correct the exceeding symbols +- and -+
			// Correggi i segni +- e -+ in eccesso
			pattern = Pattern.compile("\\+\\-|\\-\\+");
			matcher = pattern.matcher(processExpression);
			while (matcher.find()) {
				symbolsChanged = true;
				String correzione = "-";
				processExpression = processExpression.substring(0, matcher.start(0)) + correzione + processExpression.substring(matcher.start(0) + matcher.group(0).length(), processExpression.length());
				matcher = pattern.matcher(processExpression);
			}
			
			// Rimuovi i segni appena dopo le parentesi
			if (processExpression.contains("(+")) {
				symbolsChanged = true;
				processExpression = processExpression.replace("(+", "(");
			}

			// Cambia i segni appena prima le parentesi
			if (processExpression.contains("-(")) {
				symbolsChanged = true;
				processExpression = processExpression.replace("-(", "-1*(");
			}
			// Rimuovi i segni appena dopo l'inizio
			if (processExpression.startsWith("+")) {
				symbolsChanged = true;
				processExpression = processExpression.substring(1, processExpression.length());
			}

			// Rimuovi i + in eccesso
			pattern = Pattern.compile("[" + ArrayToRegex(Utils.add(concat(MathematicalSymbols.signums(true, true), MathematicalSymbols.functions()), "(")) + "]\\+[^" + ArrayToRegex(concat(concat(MathematicalSymbols.signums(true, true), MathematicalSymbols.functions()), new String[] { "(", ")" })) + "]+?[" + ArrayToRegex(concat(MathematicalSymbols.signums(true, true), MathematicalSymbols.functions())) + "]|[" + ArrayToRegex(concat(MathematicalSymbols.signums(true, true), MathematicalSymbols.functions())) + "]+?\\+[^" + ArrayToRegex(concat(concat(MathematicalSymbols.signums(true, true), MathematicalSymbols.functions()), new String[] { "(", ")" })) + "]");
			matcher = pattern.matcher(processExpression);
			symbolsChanged = false;
			while (matcher.find()) {
				symbolsChanged = true;
				String correzione = matcher.group(0).replaceFirst(Matcher.quoteReplacement("+"), "");
				processExpression = processExpression.substring(0, matcher.start(0) + 1) + correzione + processExpression.substring(matcher.start(0) + matcher.group(0).length(), processExpression.length());
				matcher = pattern.matcher(processExpression);
			}

			// Correggi i segni - in +-
			pattern = Pattern.compile("[^" + Utils.ArrayToRegex(concat(concat(MathematicalSymbols.functions(), new String[] { MathematicalSymbols.PARENTHESIS_OPEN }), MathematicalSymbols.signums(true, true))) + "]-");
			matcher = pattern.matcher(processExpression);
			while (matcher.find()) {
				symbolsChanged = true;
				String correzione = "+-";
				processExpression = processExpression.substring(0, matcher.start(0) + 1) + correzione + processExpression.substring(matcher.start(0) + matcher.group(0).length(), processExpression.length());
				matcher = pattern.matcher(processExpression);
			}

			if (symbolsChanged) {
				Utils.debug.println(debugSpaces + "•Resolved signs:" + processExpression);
			}

			// Aggiungi i segni * accanto alle parentesi
			pattern = Pattern.compile("\\([^\\(]+?\\)");
			matcher = pattern.matcher(processExpression);
			symbolsChanged = false;
			while (matcher.find()) {
				symbolsChanged = true;
				// sistema i segni * impliciti prima e dopo l'espressione.
				String beforeexp = processExpression.substring(0, matcher.start(0));
				String newexp = matcher.group(0).substring(1, matcher.group(0).length() - 1);
				String afterexp = processExpression.substring(matcher.start(0) + matcher.group(0).length(), processExpression.length());
				if (Pattern.compile("[^\\-" + Utils.ArrayToRegex(Utils.add(concat(MathematicalSymbols.functions(), concat(MathematicalSymbols.signums(true, true), MathematicalSymbols.genericSyntax())), "(")) + "]$").matcher(beforeexp).find()) {
					// Se la stringa precedente finisce con un numero
					beforeexp += MathematicalSymbols.MULTIPLICATION;
				}
				if (Pattern.compile("^[^\\-" + Utils.ArrayToRegex(Utils.add(concat(MathematicalSymbols.functions(), concat(MathematicalSymbols.signums(true, true), MathematicalSymbols.genericSyntax())), ")")) + "]").matcher(afterexp).find()) {
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
			Expression imputRawParenthesis = new Expression();
			imputRawParenthesis.setVariables(new Function[] {});
			String tmp = "";
			final String[] functions = concat(concat(concat(concat(MathematicalSymbols.functions(), MathematicalSymbols.parentheses()), MathematicalSymbols.signums(true, true)), MathematicalSymbols.variables()), MathematicalSymbols.genericSyntax());
			for (int i = 0; i < processExpression.length(); i++) {
				// Per ogni carattere cerca se è un numero o una funzione:
				String charI = processExpression.charAt(i) + "";
				if (Utils.isInArray(charI, functions)) {

					// Finds the type of function fron the following list
					// Cerca il tipo di funzione tra le esistenti
					Function f = null;
					switch (charI) {
						case MathematicalSymbols.SUM:
							f = new Sum(null, null);
							break;
						case MathematicalSymbols.SUM_SUBTRACTION:
							f = new SumSubtraction(null, null);
							break;
						case MathematicalSymbols.MULTIPLICATION:
							f = new Multiplication(null, null);
							break;
						case MathematicalSymbols.PRIORITARY_MULTIPLICATION:
							f = new PrioritaryMultiplication(null, null);
							break;
						case MathematicalSymbols.DIVISION:
							f = new Division(null, null);
							break;
						case MathematicalSymbols.NTH_ROOT:
							f = new Root(null, null);
							break;
						case MathematicalSymbols.SQUARE_ROOT:
							f = new RootSquare(null);
							break;
						case MathematicalSymbols.POWER:
							f = new Power(null, null);
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
							f = new Expression(tmpExpr, debugSpaces, false);
							break;
						default:
							if (Utils.isInArray(charI, MathematicalSymbols.variables())) {
								// Fallback
								NumeroAvanzato na = NumeroAvanzato.ONE;
								Variables iy = na.getVariableY();
								iy.getVariablesList().add(new Variable(charI.charAt(0), 1, 1));
								na = na.setVariableY(iy);
								f = new Number(na);
							} else {
								throw new java.lang.RuntimeException("Il carattere " + charI + " non è tra le funzioni designate!\nAggiungerlo ad esse o rimuovere il carattere dall'espressione!");
							}
					}
					if (f instanceof Expression) {
						tmp = "";
					} else if (f instanceof Number) {
						if (imputRawParenthesis.getVariablesLength() == 0) {
							if (tmp.length() > 0) {
								imputRawParenthesis.addVariableToEnd(new Number(tmp));
								Utils.debug.println(debugSpaces + "•Added value to expression:" + tmp);
								imputRawParenthesis.addVariableToEnd(new PrioritaryMultiplication(null, null));
								Utils.debug.println(debugSpaces + "•Added variable to expression:" + new PrioritaryMultiplication(null, null).getSymbol());
							}
						} else {
							if (tmp.length() > 0) {
								if (imputRawParenthesis.getVariable(imputRawParenthesis.getVariablesLength() - 1) instanceof Number) {
									imputRawParenthesis.addVariableToEnd(new PrioritaryMultiplication(null, null));
									Utils.debug.println(debugSpaces + "•Added variable to expression:" + new PrioritaryMultiplication(null, null).getSymbol());
								}
								if (tmp.equals("-")) {
									tmp = "-1";
								}
								imputRawParenthesis.addVariableToEnd(new Number(tmp));
								Utils.debug.println(debugSpaces + "•Added value to expression:" + tmp);
							}
							if (tmp.length() > 0 || imputRawParenthesis.getVariable(imputRawParenthesis.getVariablesLength() - 1) instanceof Number) {
								imputRawParenthesis.addVariableToEnd(new PrioritaryMultiplication(null, null));
								Utils.debug.println(debugSpaces + "•Added variable to expression:" + new PrioritaryMultiplication(null, null).getSymbol());
							}
						}
					} else {
						if (tmp.length() != 0) {
							imputRawParenthesis.addVariableToEnd(new Number(tmp));
							Utils.debug.println(debugSpaces + "•Added variable to expression:" + tmp);
						}
					}
					imputRawParenthesis.addVariableToEnd(f);
					Utils.debug.println(debugSpaces + "•Added variable to expression:" + f.getSymbol());
					tmp = "";
				} else {
					try {
						if (charI.equals("-") == false && charI.equals(".") == false) {
							new BigDecimal(tmp + charI);
						}
						// Se il carattere è un numero intero, un segno
						// negativo, o un punto
						tmp += charI;
					} catch (Exception exc) {
						throw new java.lang.RuntimeException("Il carattere " + tmp + charI + " non è nè un numero nè un espressione presente nella lista completa!\nAggiungerlo ad essa o rimuovere il carattere dall'espressione!");
					}
				}
			}
			if (tmp.length() > 0) {
				Utils.debug.println(debugSpaces + "•Added variable to expression:" + tmp);
				try {
					imputRawParenthesis.addVariableToEnd(new Number(tmp));
				} catch (NumberFormatException ex) {
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
			for (int i = 0; i < imputRawParenthesis.variables.length; i++) {
				if (imputRawParenthesis.variables[i] instanceof Expression) {
					Expression par = (Expression) imputRawParenthesis.variables[i];
					if (par.variables.length == 1) {
						Function subFunz = par.variables[0];
						if (subFunz instanceof Expression || subFunz instanceof Number) {
							imputRawParenthesis.variables[i] = subFunz;
							Utils.debug.println(debugSpaces + "  •Useless parentheses removed");
						}
					}
				}
			}
			
			// Inizia l'affinazione dell'espressione
			Utils.debug.println(debugSpaces + "•Pushing classes...");

			Function[] oldFunctionsArray = imputRawParenthesis.getVariables();
			ArrayList<Function> oldFunctionsList = new ArrayList<Function>();
			for (int i = 0; i < oldFunctionsArray.length; i++) {
				Function funzione = oldFunctionsArray[i];
				if (funzione != null) {
					//Affinazione
					if (funzione instanceof Root) {
						if ((i - 1) >= 0 && oldFunctionsArray[i-1] instanceof Number && ((Number)oldFunctionsArray[i-1]).getTerm().isBigInteger(false) && ((Number)oldFunctionsArray[i-1]).getTerm().toBigInteger(false).compareTo(new BigInteger("2")) == 0) {
							oldFunctionsArray[i] = null;
							oldFunctionsArray[i-1] = null;
							oldFunctionsList.remove(oldFunctionsList.size()-1);
							i -= 1;
							funzione = new RootSquare(null);
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
					if (Utils.areThereEmptyPrioritaryMultiplications(oldFunctionsList)) {
						step = "prioritary multiplications"; // PRIMA FASE
					} else if (Utils.areThereOnlyEmptySNFunctions(oldFunctionsList)) {
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
							oldFunctionsList.set(0, new Multiplication(oldFunctionsList.get(0), oldFunctionsList.remove(1)));
						}
					}
					Utils.debug.println(debugSpaces + "  •Phase: "+step);
					while (i < oldFunctionsList.size() && change == false && oldFunctionsList.size() > 1) {
						Function funzioneTMP = oldFunctionsList.get(i);
						if (funzioneTMP instanceof FunctionTwoValues) {
							if (step != "SN Functions") {
								if (
										(step == "sums" && (funzioneTMP instanceof Sum || funzioneTMP instanceof SumSubtraction) == true && ((funzioneTMP instanceof AnteriorFunction && ((AnteriorFunction) funzioneTMP).variable == null) || (funzioneTMP instanceof FunctionTwoValues && ((FunctionTwoValues) funzioneTMP).variable1 == null && ((FunctionTwoValues) funzioneTMP).variable2 == null) || (!(funzioneTMP instanceof AnteriorFunction) && !(funzioneTMP instanceof FunctionTwoValues))))
										||
										(
											step.equals("prioritary multiplications")
											&&
											(funzioneTMP instanceof PrioritaryMultiplication)
											&&
											((FunctionTwoValues) funzioneTMP).variable1 == null
											&&
											((FunctionTwoValues) funzioneTMP).variable2 == null
										)
										||
										(
											step.equals("multiplications")
											&&
											(
												(funzioneTMP instanceof Multiplication)
												||
												(funzioneTMP instanceof Division)
											)
											&&
											((FunctionTwoValues) funzioneTMP).variable1 == null
											&&
											((FunctionTwoValues) funzioneTMP).variable2 == null
										)
										||
										(
											step == "NSN Functions"
											&&
											(funzioneTMP instanceof Sum) == false
											&&
											(funzioneTMP instanceof SumSubtraction) == false
											&&
											(funzioneTMP instanceof Multiplication) == false
											&&
											(funzioneTMP instanceof PrioritaryMultiplication) == false
											&&
											(funzioneTMP instanceof Division) == false
											&&
											(
												(
													funzioneTMP instanceof AnteriorFunction
													&&
													((AnteriorFunction) funzioneTMP).variable == null
												)
												||
												(
													funzioneTMP instanceof FunctionTwoValues
													&&
													((FunctionTwoValues) funzioneTMP).variable1 == null
													&&
													((FunctionTwoValues) funzioneTMP).variable2 == null
												)
												||
												(
													!(funzioneTMP instanceof AnteriorFunction)
													&&
													!(funzioneTMP instanceof FunctionTwoValues)
												)
											)
										)
									) {
									change = true;

									if (i + 1 < oldFunctionsList.size() && i - 1 >= 0) {
										((FunctionTwoValues) funzioneTMP).setVariable1((Function) oldFunctionsList.get(i - 1));
										((FunctionTwoValues) funzioneTMP).setVariable2((Function) oldFunctionsList.get(i + 1));
										oldFunctionsList.set(i, funzioneTMP);

										// è importante togliere prima gli elementi
										// in fondo e poi quelli davanti, perché gli
										// indici scalano da destra a sinistra.
										oldFunctionsList.remove(i + 1);
										oldFunctionsList.remove(i - 1);

										Utils.debug.println(debugSpaces + "  •Set variable to expression:" + funzioneTMP.getSymbol());
										try {
											Utils.debug.println(debugSpaces + "    " + "var1=" + ((FunctionTwoValues) funzioneTMP).getVariable1().toString());
										} catch (NullPointerException ex2) {}
										try {
											Utils.debug.println(debugSpaces + "    " + "var2=" + ((FunctionTwoValues) funzioneTMP).getVariable2().toString());
										} catch (NullPointerException ex2) {}
										try {
											Utils.debug.println(debugSpaces + "    " + "(result)=" + ((FunctionTwoValues) funzioneTMP).toString());
										} catch (NullPointerException ex2) {}

									} else {
										throw new java.lang.RuntimeException("Argomenti mancanti! Sistemare l'equazione!");
									}
								}
							}
						} else if (funzioneTMP instanceof AnteriorFunction) {
							if ((step == "SN Functions" && ((AnteriorFunction) funzioneTMP).variable == null)) {
								if (i + 1 < oldFunctionsList.size()) {
									Function nextFunc = oldFunctionsList.get(i + 1);
									if (nextFunc instanceof AnteriorFunction && ((AnteriorFunction)nextFunc).variable == null) {
										
									} else {
										change = true;
										((AnteriorFunction) funzioneTMP).setVariable((Function) nextFunc);
										oldFunctionsList.set(i, funzioneTMP);

										// è importante togliere prima gli elementi in
										// fondo e poi quelli davanti, perché gli indici
										// scalano da destra a sinistra.
										oldFunctionsList.remove(i + 1);

										Utils.debug.println(debugSpaces + "  •Set variable to expression:" + funzioneTMP.getSymbol());
										Function var = ((AnteriorFunction) funzioneTMP).getVariable();
										if (var == null) {
											Utils.debug.println(debugSpaces + "    " + "var=null");
										} else {
											Utils.debug.println(debugSpaces + "    " + "var=" + var.toString());
										}
									}
								} else {
									throw new java.lang.RuntimeException("Argomenti mancanti! Sistemare l'equazione!");
								}
							}
						} else if (funzioneTMP instanceof Number || funzioneTMP instanceof Expression) {
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
			setVariables(oldFunctionsList);

			dsl = debugSpaces.length();
			debugSpaces = "";
			for (int i = 0; i < dsl - 2; i++) {
				debugSpaces += " ";
			}
			Utils.debug.println(debugSpaces + "•Finished correcting classes.");

			String result = toString();
			Utils.debug.println(debugSpaces + "•Result:" + result);
		}
	}

	@Override
	public String getSymbol() {
		return "Parentesi";
	}

	@Override
	public List<Function> solveOneStep() throws Error {
		List<Function> ret = new ArrayList<>();
		if (variables.length == 0) {
			stepsCount = -1;
			return ret;
		} else if (variables.length == 1) {
			if (variables[0].getStepsCount() > 0) {
				List<Function> l = variables[0].solveOneStep();
				for (Function f : l) {
					if (f instanceof Number) {
						ret.add(f);
					} else {
						ret.add(new Expression(new Function[]{(Function) f}));
					}
				}
				stepsCount = -1;
				return ret;
			} else {
				ret.add(variables[0]);
				stepsCount = -1;
				return ret;
			}
		} else {
			for (Function f : variables) {
				if (f.getStepsCount() >= stepsCount - 1) {
					List<Function> partial = f.solveOneStep();
					for (Function fnc : partial) {
						ret.add(new Expression(new Function[]{(Function) fnc}));
					}
				}
			}
			stepsCount = -1;
			return ret;
		}
	}

	@Override
	public void generateGraphics() {
		for (Function var : variables) {
			var.setSmall(small);
			var.generateGraphics();
		}
		
		width = calcWidth();
		height = calcHeight();
		line = calcLine();
	}
	
	public boolean parenthesesNeeded() {
		boolean parenthesesneeded = true;
		if (initialParenthesis) {
			parenthesesneeded = false;
		} else {
			if (variables.length == 1) {
				if (variables[0] instanceof Division) {
					parenthesesneeded = false;
				} else {
					parenthesesneeded = true;
				}
			}
		}
		return parenthesesneeded;
	}
	
	@Override
	public void draw(int x, int y) {
		if (parenthesesNeeded() == false) {
			this.variables[0].draw(x, y);
		} else {
			float miny = y;
			float maxy = y + getHeight();
			int h = getHeight();
			x += 1;
			glColor3f(0, 0, 0);
			glDrawLine(x, y + 2, x + 2, y);
			glDrawLine(x, y + 2, x, y + h - 3);
			glDrawLine(x, y + h - 3, x + 2, y + h - 1);
			x += 4;
			for (Function f : variables) {
				float fheight = f.getHeight();
				float y2 = miny + ((maxy - miny) / 2 - fheight / 2);
				f.draw(x, (int) y2);
				x += f.getWidth();
			}
			x += 2;
			glDrawLine(x, y, x + 2, y + 2);
			glDrawLine(x + 2, y + 2, x + 2, y + h - 3);
			glDrawLine(x, y + h - 1, x + 2, y + h - 3);
			x += 4;
		}
	}

	@Override
	public int getWidth() {
		return width;
	}

	private int calcWidth() {
		if (parenthesesNeeded() == false) {
			return this.variables[0].getWidth();
		} else {
			int w = 0;
			for (Function f : variables) {
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
		if (initialParenthesis || variables.length == 1) {
			return this.variables[0].getHeight();
		} else {
			Function tmin = null;
			Function tmax = null;
			for (Function t : variables) {
				if (tmin == null || t.getLine() >= tmin.getLine()) {
					tmin = t;
				}
				if (tmax == null || t.getHeight() - t.getLine() >= tmax.getHeight() - tmax.getLine()) {
					tmax = t;
				}
			}
			if (tmin == null)
				return Utils.getFontHeight(small);
			return tmin.getLine() + tmax.getHeight() - tmax.getLine();
		}
	}

	@Override
	public int getLine() {
		return line;
	}
	
	private int calcLine() {
		if (initialParenthesis || variables.length == 1) {
			return this.variables[0].getLine();
		} else {
			Function tl = null;
			for (Function t : variables) {
				if (tl == null || t.getLine() >= tl.getLine()) {
					tl = t;
				}
			}
			if (tl == null)
				return Utils.getFontHeight(small) / 2;
			return tl.getLine();
		}
	}

}

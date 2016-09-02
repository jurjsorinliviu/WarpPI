package org.warp.picalculator;

import static org.warp.engine.Display.Render.glColor3f;
import static org.warp.engine.Display.Render.glDrawLine;
import static org.warp.picalculator.Utils.ArrayToRegex;
import static org.warp.picalculator.Utils.concat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nevec.rjm.NumeroAvanzato;
import org.nevec.rjm.NumeroAvanzatoVec;

public class Espressione extends FunzioneMultiplaBase {

	public Espressione() {
		super();
	}

	public Espressione(FunzioneBase[] values) {
		super(values);
	}

	private boolean parentesiIniziale = false;

	public Espressione(String string) throws Errore {
		this(string, "", true);
	}

	public Espressione(String string, String debugSpaces, boolean parentesiIniziale) throws Errore {
		super();
		this.parentesiIniziale = parentesiIniziale;
		boolean isNumber = false;

		// Determina se l'espressione è già un numero:
		try {
			new Termine(string);
			isNumber = true;
		} catch (NumberFormatException ex) {
			isNumber = false;
		}
		
		String processExpression = string;
		Utils.debug.println(debugSpaces + "•Analyzing expression:" + processExpression);
		
		if (isNumber){
			// Se l'espressione è già un numero:
			Termine t = new Termine(string);
			setVariables(new FunzioneBase[] { t });
			Utils.debug.println(debugSpaces + "•Result:" + t.toString());
		} else {
			// Altrimenti prepara l'espressione:
			debugSpaces += "  ";

			// Se l'espressione non è già un numero:

			// Controlla se ci sono più di un uguale
			int equationsFound = 0;
			int systemsFound = 0;
			for (char c : processExpression.toCharArray()) {
				if (("" + c).equals(Simboli.EQUATION)) {
					equationsFound += 1;
				}
				if (("" + c).equals(Simboli.SYSTEM)) {
					equationsFound += 1;
				}
			}
			if (equationsFound == 1 && systemsFound == 0) {
				processExpression = Simboli.SYSTEM + processExpression;
				systemsFound += 1;
			}
			if (equationsFound != systemsFound) {
				throw new Errore(Errori.SYNTAX_ERROR);
			}

			// Correggi i segni ++ e -- in eccesso
			Pattern pattern = Pattern.compile("\\+\\++?|\\-\\-+?");
			Matcher matcher = pattern.matcher(processExpression);
			boolean cambiati = false;
			while (matcher.find()) {
				cambiati = true;
				String correzione = "+";
				processExpression = processExpression.substring(0, matcher.start(0) + 1) + correzione + processExpression.substring(matcher.start(0) + matcher.group(0).length(), processExpression.length());
				matcher = pattern.matcher(processExpression);
			}

			// Correggi i segni +- e -+ in eccesso
			pattern = Pattern.compile("\\+\\-|\\-\\+");
			matcher = pattern.matcher(processExpression);
			while (matcher.find()) {
				cambiati = true;
				String correzione = "-";
				processExpression = processExpression.substring(0, matcher.start(0)) + correzione + processExpression.substring(matcher.start(0) + matcher.group(0).length(), processExpression.length());
				matcher = pattern.matcher(processExpression);
			}

			// Rimuovi i segni appena dopo le parentesi
			if (processExpression.contains("(+")) {
				cambiati = true;
				processExpression = processExpression.replace("(+", "(");
			}

			// Cambia i segni appena prima le parentesi
			if (processExpression.contains("-(")) {
				cambiati = true;
				processExpression = processExpression.replace("-(", "-1*(");
			}
			// Rimuovi i segni appena dopo l'inizio
			if (processExpression.startsWith("+")) {
				cambiati = true;
				processExpression = processExpression.substring(1, processExpression.length());
			}

			// Rimuovi i + in eccesso
			pattern = Pattern.compile("[" + ArrayToRegex(Utils.add(concat(Simboli.segni(true, true), Simboli.funzioni()), "(")) + "]\\+[^" + ArrayToRegex(concat(concat(Simboli.segni(true, true), Simboli.funzioni()), new String[] { "(", ")" })) + "]+?[" + ArrayToRegex(concat(Simboli.segni(true, true), Simboli.funzioni())) + "]|[" + ArrayToRegex(concat(Simboli.segni(true, true), Simboli.funzioni())) + "]+?\\+[^" + ArrayToRegex(concat(concat(Simboli.segni(true, true), Simboli.funzioni()), new String[] { "(", ")" })) + "]");
			matcher = pattern.matcher(processExpression);
			cambiati = false;
			while (matcher.find()) {
				cambiati = true;
				String correzione = matcher.group(0).replaceFirst(Matcher.quoteReplacement("+"), "");
				processExpression = processExpression.substring(0, matcher.start(0) + 1) + correzione + processExpression.substring(matcher.start(0) + matcher.group(0).length(), processExpression.length());
				matcher = pattern.matcher(processExpression);
			}

			// Correggi i segni - in +-
			pattern = Pattern.compile("[^" + Utils.ArrayToRegex(concat(concat(Simboli.funzioni(), new String[] { Simboli.PARENTHESIS_OPEN }), Simboli.segni(true, true))) + "]-");
			matcher = pattern.matcher(processExpression);
			while (matcher.find()) {
				cambiati = true;
				String correzione = "+-";
				processExpression = processExpression.substring(0, matcher.start(0) + 1) + correzione + processExpression.substring(matcher.start(0) + matcher.group(0).length(), processExpression.length());
				matcher = pattern.matcher(processExpression);
			}

			if (cambiati) {
				Utils.debug.println(debugSpaces + "•Resolved signs:" + processExpression);
			}

			// Aggiungi i segni * accanto alle parentesi
			pattern = Pattern.compile("\\([^\\(]+?\\)");
			matcher = pattern.matcher(processExpression);
			cambiati = false;
			while (matcher.find()) {
				cambiati = true;
				// sistema i segni * impliciti prima e dopo l'espressione.
				String beforeexp = processExpression.substring(0, matcher.start(0));
				String newexp = matcher.group(0).substring(1, matcher.group(0).length() - 1);
				String afterexp = processExpression.substring(matcher.start(0) + matcher.group(0).length(), processExpression.length());
				if (Pattern.compile("[^\\-" + Utils.ArrayToRegex(Utils.add(concat(Simboli.funzioni(), concat(Simboli.segni(true, true), Simboli.sintassiGenerale())), "(")) + "]$").matcher(beforeexp).find()) {
					// Se la stringa precedente finisce con un numero
					beforeexp += Simboli.MULTIPLICATION;
				}
				if (Pattern.compile("^[^\\-" + Utils.ArrayToRegex(Utils.add(concat(Simboli.funzioni(), concat(Simboli.segni(true, true), Simboli.sintassiGenerale())), ")")) + "]").matcher(afterexp).find()) {
					// Se la stringa successiva inizia con un numero
					afterexp = Simboli.MULTIPLICATION + afterexp;
				}
				processExpression = beforeexp + "⑴" + newexp + "⑵" + afterexp;
				matcher = pattern.matcher(processExpression);
			}

			processExpression = processExpression.replace("⑴", "(").replace("⑵", ")");

			if (cambiati) {
				Utils.debug.println(debugSpaces + "•Added implicit multiplications:" + processExpression);
			}

			Utils.debug.println(debugSpaces + "•Subdivision in classes:");

			debugSpaces += "  ";

			// Suddividi tutto
			Espressione parentesiNonSuddivisaCorrettamente = new Espressione();
			parentesiNonSuddivisaCorrettamente.setVariables(new FunzioneBase[] {});
			String tmp = "";
			final String[] funzioni = concat(concat(concat(concat(Simboli.funzioni(), Simboli.parentesi()), Simboli.segni(true, true)), Simboli.incognite()), Simboli.sintassiGenerale());
			for (int i = 0; i < processExpression.length(); i++) {
				// Per ogni carattere cerca se è un numero o una funzione:
				String charI = processExpression.charAt(i) + "";
				if (Utils.isInArray(charI, funzioni)) {

					// Cerca il tipo di funzione tra le esistenti
					FunzioneBase f = null;
					switch (charI) {
						case Simboli.SUM:
							f = new Somma(null, null);
							break;
						case Simboli.MULTIPLICATION:
							f = new Moltiplicazione(null, null);
							break;
						case Simboli.PRIORITARY_MULTIPLICATION:
							f = new MoltiplicazionePrioritaria(null, null);
							break;
						case Simboli.DIVISION:
							f = new Divisione(null, null);
							break;
						case Simboli.NTH_ROOT:
							f = new Radice(null, null);
							break;
						case Simboli.SQUARE_ROOT:
							f = new RadiceQuadrata(null);
							break;
						case Simboli.POTENZA:
							f = new Potenza(null, null);
							break;
						case Simboli.PARENTHESIS_OPEN:
							// cerca l'ultima parentesi chiusa
							int startIndex = i;
							int endIndex = -1;
							int jumps = -1;
							for (int i2 = startIndex; i2 < processExpression.length(); i2++) {
								if ((processExpression.charAt(i2) + "").equals(Simboli.PARENTHESIS_CLOSE)) {
									if (jumps == 0) {
										endIndex = i2;
										break;
									} else if (jumps > 0) {
										jumps -= 1;
									} else if (jumps < 0) {
										throw new Errore(Errori.UNBALANCED_BRACKETS);
									}
								} else if ((processExpression.charAt(i2) + "").equals(Simboli.PARENTHESIS_OPEN)) {
									jumps += 1;
								}
							}
							if (endIndex == -1 || endIndex < startIndex) {
								throw new Errore(Errori.UNBALANCED_BRACKETS);
							}
							startIndex += 1;
							i = startIndex;

							String tmpExpr = "";
							while (i < endIndex) {
								tmpExpr += processExpression.charAt(i);
								i++;
							}
							f = new Espressione(tmpExpr, debugSpaces, false);
							break;
						default:
							if (Utils.isInArray(charI, Simboli.incognite())) {
								// Fallback
								NumeroAvanzato na = NumeroAvanzato.ONE;
								Incognite iy = na.getIncognitey();
								iy.incognite.add(new Incognita(charI.charAt(0), 1, 1));
								na = na.setIncognitey(iy);
								f = new Termine(na);
							} else {
								throw new java.lang.RuntimeException("Il carattere " + charI + " non è tra le funzioni designate!\nAggiungerlo ad esse o rimuovere il carattere dall'espressione!");
							}
					}
					if (f instanceof Espressione) {
						tmp = "";
					} else if (f instanceof Termine) {
						if (parentesiNonSuddivisaCorrettamente.getVariablesLength() == 0) {
							if (tmp.length() > 0) {
								parentesiNonSuddivisaCorrettamente.addVariableToEnd(new Termine(tmp));
								Utils.debug.println(debugSpaces + "•Added value to expression:" + tmp);
								parentesiNonSuddivisaCorrettamente.addVariableToEnd(new MoltiplicazionePrioritaria(null, null));
								Utils.debug.println(debugSpaces + "•Added variable to expression:" + new MoltiplicazionePrioritaria(null, null).simbolo());
							}
						} else {
							if (tmp.length() > 0) {
								if (parentesiNonSuddivisaCorrettamente.getVariable(parentesiNonSuddivisaCorrettamente.getVariablesLength() - 1) instanceof Termine) {
									parentesiNonSuddivisaCorrettamente.addVariableToEnd(new MoltiplicazionePrioritaria(null, null));
									Utils.debug.println(debugSpaces + "•Added variable to expression:" + new MoltiplicazionePrioritaria(null, null).simbolo());
								}
								if (tmp.equals("-")) {
									tmp = "-1";
								}
								parentesiNonSuddivisaCorrettamente.addVariableToEnd(new Termine(tmp));
								Utils.debug.println(debugSpaces + "•Added value to expression:" + tmp);
							}
							if (tmp.length() > 0 || parentesiNonSuddivisaCorrettamente.getVariable(parentesiNonSuddivisaCorrettamente.getVariablesLength() - 1) instanceof Termine) {
								parentesiNonSuddivisaCorrettamente.addVariableToEnd(new MoltiplicazionePrioritaria(null, null));
								Utils.debug.println(debugSpaces + "•Added variable to expression:" + new MoltiplicazionePrioritaria(null, null).simbolo());
							}
						}
					} else {
						if (tmp.length() != 0) {
							parentesiNonSuddivisaCorrettamente.addVariableToEnd(new Termine(tmp));
							Utils.debug.println(debugSpaces + "•Added variable to expression:" + tmp);
						}
					}
					parentesiNonSuddivisaCorrettamente.addVariableToEnd(f);
					Utils.debug.println(debugSpaces + "•Added variable to expression:" + f.simbolo());
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
					parentesiNonSuddivisaCorrettamente.addVariableToEnd(new Termine(tmp));
				} catch (NumberFormatException ex) {
					throw new Errore(Errori.SYNTAX_ERROR);
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
			for (int i = 0; i < parentesiNonSuddivisaCorrettamente.variables.length; i++) {
				if (parentesiNonSuddivisaCorrettamente.variables[i] instanceof Espressione) {
					Espressione par = (Espressione) parentesiNonSuddivisaCorrettamente.variables[i];
					if (par.variables.length == 1) {
						FunzioneBase subFunz = par.variables[0];
						if (subFunz instanceof Espressione || subFunz instanceof Termine) {
							parentesiNonSuddivisaCorrettamente.variables[i] = subFunz;
							Utils.debug.println(debugSpaces + "  •Useless parentheses removed");
						}
					}
				}
			}
			
			// Inizia l'affinazione dell'espressione
			Utils.debug.println(debugSpaces + "•Pushing classes...");

			FunzioneBase[] funzioniOLDArray = parentesiNonSuddivisaCorrettamente.getVariables();
			ArrayList<FunzioneBase> funzioniOLD = new ArrayList<FunzioneBase>();
			for (int i = 0; i < funzioniOLDArray.length; i++) {
				FunzioneBase funzione = funzioniOLDArray[i];
				if (funzione != null) {
					//Affinazione
					if (funzione instanceof Radice) {
						if ((i - 1) >= 0 && funzioniOLDArray[i-1] instanceof Termine && ((Termine)funzioniOLDArray[i-1]).getTerm().compareTo(new NumeroAvanzatoVec(new NumeroAvanzato(new BigInteger("2")))) == 0) {
							funzioniOLDArray[i] = null;
							funzioniOLDArray[i-1] = null;
							funzioniOLD.remove(funzioniOLD.size()-1);
							i -= 1;
							funzione = new RadiceQuadrata(null);
						}
					}
					//Aggiunta della funzione alla lista grezza
					funzioniOLD.add(funzione);
				}
			}

			if (funzioniOLD.size() > 1) {
				Utils.debug.println(debugSpaces + "  •Correcting classes:");

				int before = 0;
				String fase = "funzioniSN";
				int n = 0;
				do {
					before = funzioniOLD.size();
					int i = 0;
					boolean change = false;
					if (Utils.ciSonoMoltiplicazioniPrioritarieNonImpostate(funzioniOLD)) {
						fase = "moltiplicazioni prioritarie"; // PRIMA FASE
					} else if (Utils.ciSonoFunzioniSNnonImpostate(funzioniOLD)) {
						fase = "funzioniSN"; // SECONDA FASE
					} else if (Utils.ciSonoFunzioniNSNnonImpostate(funzioniOLD)) {
						fase = "funzioniNSN"; // TERZA FASE
					} else if (Utils.ciSonoMoltiplicazioniNonImpostate(funzioniOLD)) {
						fase = "moltiplicazioni"; // QUARTA FASE
					} else if (Utils.ciSonoSommeNonImpostate(funzioniOLD)) {
						fase = "somme"; // QUINTA FASE
					} else {
//						fase = "errore";
						System.out.println("WARN: ---> POSSIBILE ERRORE????? <---");// BOH
//						throw new Errore(Errori.SYNTAX_ERROR);
						while (funzioniOLD.size() > 1) {
							funzioniOLD.set(0, new Moltiplicazione(funzioniOLD.get(0), funzioniOLD.remove(1)));
						}
					}
					Utils.debug.println(debugSpaces + "  •Phase: "+fase);
					while (i < funzioniOLD.size() && change == false && funzioniOLD.size() > 1) {
						FunzioneBase funzioneTMP = funzioniOLD.get(i);
						if (funzioneTMP instanceof FunzioneDueValoriBase) {
							if (fase != "funzioniSN") {
								if (
										(fase == "somme" && (funzioneTMP instanceof Somma) == true && ((funzioneTMP instanceof FunzioneAnterioreBase && ((FunzioneAnterioreBase) funzioneTMP).variable == null) || (funzioneTMP instanceof FunzioneDueValoriBase && ((FunzioneDueValoriBase) funzioneTMP).variable1 == null && ((FunzioneDueValoriBase) funzioneTMP).variable2 == null) || (!(funzioneTMP instanceof FunzioneAnterioreBase) && !(funzioneTMP instanceof FunzioneDueValoriBase))))
										||
										(
											fase.equals("moltiplicazioni prioritarie")
											&&
											(funzioneTMP instanceof MoltiplicazionePrioritaria)
											&&
											((FunzioneDueValoriBase) funzioneTMP).variable1 == null
											&&
											((FunzioneDueValoriBase) funzioneTMP).variable2 == null
										)
										||
										(
											fase.equals("moltiplicazioni")
											&&
											(
												(funzioneTMP instanceof Moltiplicazione)
												||
												(funzioneTMP instanceof Divisione)
											)
											&&
											((FunzioneDueValoriBase) funzioneTMP).variable1 == null
											&&
											((FunzioneDueValoriBase) funzioneTMP).variable2 == null
										)
										||
										(
											fase == "funzioniNSN"
											&&
											(funzioneTMP instanceof Somma) == false
											&&
											(funzioneTMP instanceof Moltiplicazione) == false
											&&
											(funzioneTMP instanceof MoltiplicazionePrioritaria) == false
											&&
											(funzioneTMP instanceof Divisione) == false
											&&
											(
												(
													funzioneTMP instanceof FunzioneAnterioreBase
													&&
													((FunzioneAnterioreBase) funzioneTMP).variable == null
												)
												||
												(
													funzioneTMP instanceof FunzioneDueValoriBase
													&&
													((FunzioneDueValoriBase) funzioneTMP).variable1 == null
													&&
													((FunzioneDueValoriBase) funzioneTMP).variable2 == null
												)
												||
												(
													!(funzioneTMP instanceof FunzioneAnterioreBase)
													&&
													!(funzioneTMP instanceof FunzioneDueValoriBase)
												)
											)
										)
									) {
									change = true;

									if (i + 1 < funzioniOLD.size() && i - 1 >= 0) {
										((FunzioneDueValoriBase) funzioneTMP).setVariable1((FunzioneBase) funzioniOLD.get(i - 1));
										((FunzioneDueValoriBase) funzioneTMP).setVariable2((FunzioneBase) funzioniOLD.get(i + 1));
										funzioniOLD.set(i, funzioneTMP);

										// è importante togliere prima gli elementi
										// in fondo e poi quelli davanti, perché gli
										// indici scalano da destra a sinistra.
										funzioniOLD.remove(i + 1);
										funzioniOLD.remove(i - 1);

										Utils.debug.println(debugSpaces + "  •Set variable to expression:" + funzioneTMP.simbolo());
										try {
											Utils.debug.println(debugSpaces + "    " + "var1=" + ((FunzioneDueValoriBase) funzioneTMP).getVariable1().calcola());
										} catch (NullPointerException ex2) {}
										try {
											Utils.debug.println(debugSpaces + "    " + "var2=" + ((FunzioneDueValoriBase) funzioneTMP).getVariable2().calcola());
										} catch (NullPointerException ex2) {}
										try {
											Utils.debug.println(debugSpaces + "    " + "(result)=" + ((FunzioneDueValoriBase) funzioneTMP).calcola());
										} catch (NullPointerException ex2) {}

									} else {
										throw new java.lang.RuntimeException("Argomenti mancanti! Sistemare l'equazione!");
									}
								}
							}
						} else if (funzioneTMP instanceof FunzioneAnterioreBase) {
							if ((fase == "funzioniSN" && ((FunzioneAnterioreBase) funzioneTMP).variable == null)) {
								if (i + 1 < funzioniOLD.size()) {
									FunzioneBase nextFunc = funzioniOLD.get(i + 1);
									if (nextFunc instanceof FunzioneAnterioreBase && ((FunzioneAnterioreBase)nextFunc).variable == null) {
										
									} else {
										change = true;
										((FunzioneAnterioreBase) funzioneTMP).setVariable((FunzioneBase) nextFunc);
										funzioniOLD.set(i, funzioneTMP);

										// è importante togliere prima gli elementi in
										// fondo e poi quelli davanti, perché gli indici
										// scalano da destra a sinistra.
										funzioniOLD.remove(i + 1);

										Utils.debug.println(debugSpaces + "  •Set variable to expression:" + funzioneTMP.simbolo());
										FunzioneBase var = ((FunzioneAnterioreBase) funzioneTMP).getVariable().calcola();
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
						} else if (funzioneTMP instanceof Termine || funzioneTMP instanceof Espressione) {
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
				} while (((funzioniOLD.size() != before || fase != "somme") && funzioniOLD.size() > 1));
			}
			setVariables(funzioniOLD);

			dsl = debugSpaces.length();
			debugSpaces = "";
			for (int i = 0; i < dsl - 2; i++) {
				debugSpaces += " ";
			}
			Utils.debug.println(debugSpaces + "•Finished correcting classes.");

			Termine result = calcola();
			Utils.debug.println(debugSpaces + "•Result:" + result);
		}
	}

	@Override
	public String simbolo() {
		return "Parentesi";
	}

	@Override
	public Termine calcola() throws Errore {
		if (variables.length == 0) {
			return new Termine("0");
		} else if (variables.length == 1) {
			return (Termine) variables[0].calcola();
		} else {
			Termine result = new Termine("0");
			for (Funzione f : variables) {
				result = result.add((Termine) f.calcola());
			}
			return result;
		}
	}

	@Override
	public void calcolaGrafica() {
		for (Funzione var : variables) {
			var.setSmall(small);
			var.calcolaGrafica();
		}
		
		width = calcWidth();
		height = calcHeight();
		line = calcLine();
	}
	
	public boolean parenthesesNeeded() {
		boolean parenthesesneeded = true;
		if (parentesiIniziale) {
			parenthesesneeded = false;
		} else {
			if (variables.length == 1) {
				if (variables[0] instanceof Divisione) {
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
			for (Funzione f : variables) {
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
			for (Funzione f : variables) {
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
		if (parentesiIniziale || variables.length == 1) {
			return this.variables[0].getHeight();
		} else {
			Funzione tmin = null;
			Funzione tmax = null;
			for (Funzione t : variables) {
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
		if (parentesiIniziale || variables.length == 1) {
			return this.variables[0].getLine();
		} else {
			Funzione tl = null;
			for (Funzione t : variables) {
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

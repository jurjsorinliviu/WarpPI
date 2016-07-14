package org.warpgate.pi.calculator;

import static org.warpgate.pi.calculator.Utils.ArrayToRegex;
import static org.warpgate.pi.calculator.Utils.concat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nevec.rjm.NumeroAvanzato;
import org.nevec.rjm.NumeroAvanzatoVec;

public class Parentesi extends FunzioneMultipla {

	public Parentesi() {
		super();
	}
	
	public Parentesi(Funzione[] values) {
		super(values);
	}

	private boolean parentesiIniziale = false;
	
	public Parentesi(String string) throws Errore {
		this(string, "", true);
	}
	
	public Parentesi(String string, String debugSpaces, boolean parentesiIniziale) throws Errore {
		super();
		this.parentesiIniziale = parentesiIniziale;
		try{
			//Se l'espressione è già un numero:
			setVariables(new Funzione[]{new Termine(string)});
			Utils.debug.println(debugSpaces+"•(Value:"+string+")");
		} catch (NumberFormatException ex) {
			String processExpression = string;

			Utils.debug.println(debugSpaces+"•Analyzing expression:"+processExpression);
			debugSpaces+= "  ";
			
			//Se l'espressione non è già un numero:

			//Controlla se ci sono più di un uguale
			int equationsFound = 0;
			int systemsFound = 0;
			for (char c : processExpression.toCharArray()) {
				if ((""+c).equals(Simboli.EQUATION)) {
					equationsFound+=1;
				}
				if ((""+c).equals(Simboli.SYSTEM)) {
					equationsFound+=1;
				}
			}
			if (equationsFound == 1 && systemsFound == 0) {
				processExpression = Simboli.SYSTEM+processExpression;
				systemsFound+=1;
			}
			if (equationsFound != systemsFound) {
				throw new Errore(Errori.SYNTAX_ERROR);
			}
			
			//Correggi i segni ++ e -- in eccesso
			Pattern pattern = Pattern.compile("\\+\\++?|\\-\\-+?");
			Matcher matcher = pattern.matcher(processExpression);
			boolean cambiati = false;
			while (matcher.find()) {
				cambiati = true;
				String correzione = "+";
				processExpression = processExpression.substring(0, matcher.start(0)+1)+correzione+processExpression.substring(matcher.start(0)+matcher.group(0).length(), processExpression.length());
				matcher = pattern.matcher(processExpression);
			}
			
			//Correggi i segni +- e -+ in eccesso
			pattern = Pattern.compile("\\+\\-|\\-\\+");
			matcher = pattern.matcher(processExpression);
			while (matcher.find()) {
				cambiati = true;
				String correzione = "-";
				processExpression = processExpression.substring(0, matcher.start(0))+correzione+processExpression.substring(matcher.start(0)+matcher.group(0).length(), processExpression.length());
				matcher = pattern.matcher(processExpression);
			}
			
			//Rimuovi i segni appena dopo le parentesi
			if (processExpression.contains("(+")) {
				cambiati = true;
				processExpression = processExpression.replace("(+", "(");
			}
			
			//Cambia i segni appena prima le parentesi
			if (processExpression.contains("-(")) {
				cambiati = true;
				processExpression = processExpression.replace("-(", "-1*(");
			}
			//Rimuovi i segni appena dopo l'inizio
			if (processExpression.startsWith("+")) {
				cambiati = true;
				processExpression = processExpression.substring(1, processExpression.length());
			}
			
			//Rimuovi i + in eccesso
			pattern = Pattern.compile("["+
			ArrayToRegex(Utils.add(concat(Simboli.segni(true), Simboli.funzioni()), "("))
			+"]\\+[^"+
			ArrayToRegex(concat(concat(Simboli.segni(true), Simboli.funzioni()), new String[]{"(", ")"}))
			+"]+?["+
			ArrayToRegex(concat(Simboli.segni(true), Simboli.funzioni()))
			+"]|["+
			ArrayToRegex(concat(Simboli.segni(true), Simboli.funzioni()))
			+"]+?\\+[^"+
			ArrayToRegex(concat(concat(Simboli.segni(true), Simboli.funzioni()), new String[]{"(", ")"}))
			+"]");
			matcher = pattern.matcher(processExpression);
			cambiati = false;
			while (matcher.find()) {
				cambiati = true;
				String correzione = matcher.group(0).replaceFirst(Matcher.quoteReplacement("+"), "");
				processExpression = processExpression.substring(0, matcher.start(0)+1)+correzione+processExpression.substring(matcher.start(0)+matcher.group(0).length(), processExpression.length());
				matcher = pattern.matcher(processExpression);
			}
			
			//Correggi i segni - in +-
			pattern = Pattern.compile("[^"+Utils.ArrayToRegex(concat(concat(Simboli.funzioni(),new String[]{Simboli.PARENTHESIS_OPEN}), Simboli.segni(true)))+"]-");
			matcher = pattern.matcher(processExpression);
			while (matcher.find()) {
				cambiati = true;
				String correzione = "+-";
				processExpression = processExpression.substring(0, matcher.start(0)+1)+correzione+processExpression.substring(matcher.start(0)+matcher.group(0).length(), processExpression.length());
				matcher = pattern.matcher(processExpression);
			}

			if (cambiati) {
				Utils.debug.println(debugSpaces+"•Resolved signs:"+processExpression);
			}
			
			//Aggiungi i segni * accanto alle parentesi
			pattern = Pattern.compile("\\([^\\(]+?\\)");
			matcher = pattern.matcher(processExpression);
			cambiati = false;
			while (matcher.find()) {
				cambiati = true;
				//sistema i segni * impliciti prima e dopo l'espressione.
				String beforeexp = processExpression.substring(0, matcher.start(0));
				String newexp = matcher.group(0).substring(1, matcher.group(0).length()-1);
				String afterexp = processExpression.substring(matcher.start(0)+matcher.group(0).length(), processExpression.length());
				if (Pattern.compile("[^\\-"+Utils.ArrayToRegex(Utils.add(concat(Simboli.funzioni(), concat(Simboli.segni(true), Simboli.sintassiGenerale())), "("))+"]$").matcher(beforeexp).find()) {
					//Se la stringa precedente finisce con un numero
					beforeexp += Simboli.MULTIPLICATION;
				}
				if (Pattern.compile("^[^\\-"+Utils.ArrayToRegex(Utils.add(concat(Simboli.funzioni(), concat(Simboli.segni(true), Simboli.sintassiGenerale())), ")"))+"]").matcher(afterexp).find()) {
					//Se la stringa successiva inizia con un numero
					afterexp = Simboli.MULTIPLICATION+afterexp;
				}
				processExpression = beforeexp+"⑴"+newexp+"⑵"+afterexp;
				matcher = pattern.matcher(processExpression);
			}

			processExpression = processExpression.replace("⑴", "(").replace("⑵", ")");
			
			if (cambiati) {
				Utils.debug.println(debugSpaces+"•Added implicit multiplications:"+processExpression);
			}
			
			Utils.debug.println(debugSpaces+"•Subdivision in classes:");
			
			debugSpaces += "  ";
			

			//Suddividi tutto
			Parentesi parentesiNonSuddivisaCorrettamente = new Parentesi();
			parentesiNonSuddivisaCorrettamente.setVariables(new Funzione[]{});
			String tmp = "";
			Incognite tmpI = new Incognite();
			final String[] funzioni = concat(concat(concat(concat(Simboli.funzioni(), Simboli.parentesi()), Simboli.segni(true)), Simboli.incognite()), Simboli.sintassiGenerale());
			for (int i = 0; i < processExpression.length(); i++) {
				//Per ogni carattere cerca se è un numero o una funzione:
				String charI = processExpression.charAt(i)+"";
				if (Utils.isInArray(charI, funzioni)) {
					
					//Cerca il tipo di funzione tra le esistenti
					Funzione f = null;
					switch (charI) {
					case Simboli.SUM:
						f = new Somma(null, null);
						break;
					case Simboli.MULTIPLICATION:
						f = new Moltiplicazione(null, null);
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
					case Simboli.EQUATION:
						f = new Equazione(null, null);
						break;
					case Simboli.SYSTEM:
						f = new ParteSistema(null);
						break;
					case Simboli.PARENTHESIS_OPEN:
						//cerca l'ultima parentesi chiusa
						int startIndex = i;
						int endIndex = -1;
						int jumps = -1;
						for (int i2 = startIndex; i2 < processExpression.length(); i2++) {
							if ((processExpression.charAt(i2)+"").equals(Simboli.PARENTHESIS_CLOSE)) {
								if (jumps == 0) {
									endIndex = i2;
									break;
								} else if (jumps > 0) {
									jumps -= 1;
								} else if (jumps < 0) {
									throw new Errore(Errori.UNBALANCED_BRACKETS);
								}
							} else if ((processExpression.charAt(i2)+"").equals(Simboli.PARENTHESIS_OPEN)) {
								jumps +=1;
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
						f = new Parentesi(tmpExpr, debugSpaces, false);
						break;
					default:
						if (Utils.isInArray(charI, Simboli.incognite())) {
							//Fallback
							NumeroAvanzato na = NumeroAvanzato.ONE;
							Incognite iy = na.getIncognitey();
							iy.incognite.add(new Incognita(charI.charAt(0), 1, 1));
							na = na.setIncognitey(iy);
							f = new Termine(na);
						} else {
							throw new java.lang.RuntimeException("Il carattere "+charI+" non è tra le funzioni designate!\nAggiungerlo ad esse o rimuovere il carattere dall'espressione!");
						}
					}
					if (f instanceof Parentesi) {
						tmp = "";
					} else if (f instanceof Termine) {
						if (parentesiNonSuddivisaCorrettamente.getVariablesLength() == 0) {
							if (tmp.length() > 0) {
								parentesiNonSuddivisaCorrettamente.addVariableToEnd(new Termine(tmp));
								Utils.debug.println(debugSpaces+"•Added value to expression:"+tmp);
								parentesiNonSuddivisaCorrettamente.addVariableToEnd(new Moltiplicazione(null, null));
								Utils.debug.println(debugSpaces+"•Added variable to expression:"+new Moltiplicazione(null, null).simbolo());
							}
						} else {
							if (tmp.length() > 0) {
								if (parentesiNonSuddivisaCorrettamente.getVariable(parentesiNonSuddivisaCorrettamente.getVariablesLength()-1) instanceof Termine) {
									parentesiNonSuddivisaCorrettamente.addVariableToEnd(new Moltiplicazione(null, null));
									Utils.debug.println(debugSpaces+"•Added variable to expression:"+new Moltiplicazione(null, null).simbolo());
								}
								if (tmp.equals("-")) {
									tmp = "-1";
								}
								parentesiNonSuddivisaCorrettamente.addVariableToEnd(new Termine(tmp));
								Utils.debug.println(debugSpaces+"•Added value to expression:"+tmp);
							}
							if (tmp.length() > 0 || parentesiNonSuddivisaCorrettamente.getVariable(parentesiNonSuddivisaCorrettamente.getVariablesLength()-1) instanceof Termine) {
								parentesiNonSuddivisaCorrettamente.addVariableToEnd(new Moltiplicazione(null, null));
								Utils.debug.println(debugSpaces+"•Added variable to expression:"+new Moltiplicazione(null, null).simbolo());
							}
						}
					} else {
						if (tmp.length() != 0) {
							parentesiNonSuddivisaCorrettamente.addVariableToEnd(new Termine(tmp));
							Utils.debug.println(debugSpaces+"•Added variable to expression:"+tmp);
						}
					}
					parentesiNonSuddivisaCorrettamente.addVariableToEnd(f);
					Utils.debug.println(debugSpaces+"•Added variable to expression:"+f.simbolo());
					tmp = "";
				} else {
					try{
						if (charI.equals("-") == false && charI.equals(".") == false) {
							Double.parseDouble(tmp + charI);
						}
						//Se il carattere è un numero intero, un segno negativo, o un punto
						tmp += charI;
					} catch (NumberFormatException exc) {
						throw new java.lang.RuntimeException("Il carattere "+tmp+charI+" non è nè un numero nè un espressione presente nella lista completa!\nAggiungerlo ad essa o rimuovere il carattere dall'espressione!");
					}
				}
			}
			if (tmp.length() > 0) {
				Utils.debug.println(debugSpaces+"•Added variable to expression:"+tmp);
				parentesiNonSuddivisaCorrettamente.addVariableToEnd(new Termine(tmp));
				tmp = "";
			}

			int dsl = debugSpaces.length();debugSpaces = "";for (int i = 0; i < dsl-2; i++) {debugSpaces += " ";}
			Utils.debug.println(debugSpaces+"•Finished the subdivision in classes.");
			//Fine suddivisione di insieme
			
			Utils.debug.println(debugSpaces+"•Removing useless parentheses");
			for (int i = 0; i < parentesiNonSuddivisaCorrettamente.variables.length; i++) {
				if (parentesiNonSuddivisaCorrettamente.variables[i] instanceof Parentesi) {
					Parentesi par = (Parentesi) parentesiNonSuddivisaCorrettamente.variables[i];
					if (par.variables.length == 1) {
						Funzione subFunz = par.variables[0];
						if (subFunz instanceof Parentesi || subFunz instanceof Termine) {
							parentesiNonSuddivisaCorrettamente.variables[i] = subFunz;
							Utils.debug.println(debugSpaces+"  •Useless parentheses removed");
						}
					}
				}
			}
			
			//Inizia l'affinazione dell'espressione
			Utils.debug.println(debugSpaces+"•Pushing classes...");

			Funzione[] funzioniOLDArray = parentesiNonSuddivisaCorrettamente.getVariables();
			ArrayList<Funzione> funzioniOLD = new ArrayList<Funzione>();
			for (int i = 0; i < funzioniOLDArray.length; i++) {
				if (funzioniOLDArray[i] != null) {
					funzioniOLD.add(funzioniOLDArray[i]);
				}
			}

			Utils.debug.println(debugSpaces+"  •Correcting classes:");
			
			int before = 0;
			String fase = "funzioniSN";
			int n = 0;
			do {
				before = funzioniOLD.size();
				int i = 0;
				boolean change = false;
				if (Utils.ciSonoFunzioniSNnonImpostate(funzioniOLD)) {
					fase = "funzioniSN"; // PRIMA FASE
				} else if (Utils.ciSonoFunzioniNSNnonImpostate(funzioniOLD)) {
					fase = "funzioniNSN"; //SECONDA FASE
				} else if (Utils.ciSonoMoltiplicazioniNonImpostate(funzioniOLD)) {
					fase = "moltiplicazioni"; //TERZA FASE
				} else if (Utils.ciSonoSommeNonImpostate(funzioniOLD)) {
					fase = "somme"; //QUARTA FASE
				} else if (Utils.ciSonoEquazioniNonImpostate(funzioniOLD)) {
					fase = "equazioni"; //QUINTA FASE
				} else if (Utils.ciSonoSistemiNonImpostati(funzioniOLD)) {
					fase = "sistemi"; //SESTA FASE
				} else {
					System.out.println("errore?");//BOH
					throw new Errore(Errori.SYNTAX_ERROR);
				}
				while (i < funzioniOLD.size() && change == false && funzioniOLD.size() > 1) {
					Funzione funzioneTMP = funzioniOLD.get(i);
					if (funzioneTMP instanceof FunzioneDueValori) {
						if (fase != "funzioniSN") {
							if (
									(
										fase == "somme" && (funzioneTMP instanceof Somma) == true
										&&
										(
											(
												funzioneTMP instanceof FunzioneAnteriore
												&&
												((FunzioneAnteriore)funzioneTMP).variable == null
											)
											||
											(
												funzioneTMP instanceof FunzioneDueValori
												&&
												((FunzioneDueValori)funzioneTMP).variable1 == null
												&&
												((FunzioneDueValori)funzioneTMP).variable2 == null
											)
											||
											(
												!(funzioneTMP instanceof FunzioneAnteriore)
												&&
												!(funzioneTMP instanceof FunzioneDueValori)
											)
										)
									)
								||
									(
										(fase == "moltiplicazioni" && (funzioneTMP instanceof Somma) == false)
										&&
										(
											(
												funzioneTMP instanceof FunzioneAnteriore
												&&
												((FunzioneAnteriore)funzioneTMP).variable == null
											)
											||
											(
												funzioneTMP instanceof FunzioneDueValori
												&&
												((FunzioneDueValori)funzioneTMP).variable1 == null
												&&
												((FunzioneDueValori)funzioneTMP).variable2 == null
											)
											||
											(
												!(funzioneTMP instanceof FunzioneAnteriore)
												&&
												!(funzioneTMP instanceof FunzioneDueValori)
											)
										)
									)
								||
									(fase == "equazioni" && funzioneTMP instanceof Equazione)
								||
									(
										fase == "funzioniNSN"
										&&
										(funzioneTMP instanceof Somma) == false
										&&
										(funzioneTMP instanceof Moltiplicazione) == false
										&&
										(funzioneTMP instanceof Divisione) == false
										&&
										(
											(
												funzioneTMP instanceof FunzioneAnteriore
												&&
												((FunzioneAnteriore)funzioneTMP).variable == null
											)
											||
											(
												funzioneTMP instanceof FunzioneDueValori
												&&
												((FunzioneDueValori)funzioneTMP).variable1 == null
												&&
												((FunzioneDueValori)funzioneTMP).variable2 == null
											)
											||
											(
												!(funzioneTMP instanceof FunzioneAnteriore)
												&&
												!(funzioneTMP instanceof FunzioneDueValori)
											)
										)
									)
								) {
								change = true;
								
								if (i+1 < funzioniOLD.size() && i-1 >= 0 ) {
									((FunzioneDueValori) funzioneTMP).setVariable1(funzioniOLD.get(i-1));
									((FunzioneDueValori) funzioneTMP).setVariable2(funzioniOLD.get(i+1));
									funzioniOLD.set(i, funzioneTMP);
									
									//è importante togliere prima gli elementi in fondo e poi quelli davanti, perché gli indici scalano da destra a sinistra.
									funzioniOLD.remove(i+1);
									funzioniOLD.remove(i-1);
									
									Utils.debug.println(debugSpaces+"  •Set variable to expression:"+funzioneTMP.simbolo());
									try {Utils.debug.println(debugSpaces+"    "+"var1="+((FunzioneDueValori) funzioneTMP).getVariable1().calcola());} catch (NullPointerException ex2) {}
									try {Utils.debug.println(debugSpaces+"    "+"var2="+((FunzioneDueValori) funzioneTMP).getVariable2().calcola());} catch (NullPointerException ex2) {}
									try {Utils.debug.println(debugSpaces+"    "+"(result)="+((FunzioneDueValori) funzioneTMP).calcola());} catch (NullPointerException ex2) {}
									
								} else {
									throw new java.lang.RuntimeException("Argomenti mancanti! Sistemare l'equazione!");
								}
							}
						}
					} else if (funzioneTMP instanceof FunzioneAnteriore) {
						if (
								(
									fase == "funzioniSN"
									&&
									((FunzioneAnteriore) funzioneTMP).variable == null
								)
								||
								(
									fase == "sistemi"
									&&
									funzioneTMP instanceof ParteSistema
								)
							) {
							change = true;
							if (i+1 < funzioniOLD.size()) {
								((FunzioneAnteriore) funzioneTMP).setVariable(funzioniOLD.get(i+1));
								funzioniOLD.set(i, funzioneTMP);
								
								//è importante togliere prima gli elementi in fondo e poi quelli davanti, perché gli indici scalano da destra a sinistra.
								funzioniOLD.remove(i+1);
								
								Utils.debug.println(debugSpaces+"  •Set variable to expression:"+funzioneTMP.simbolo());
								Utils.debug.println(debugSpaces+"    "+"var="+((FunzioneAnteriore) funzioneTMP).getVariable().calcola().toString());
							} else {
								throw new java.lang.RuntimeException("Argomenti mancanti! Sistemare l'equazione!");
							}
						}
					} else if (funzioneTMP instanceof Termine || funzioneTMP instanceof Parentesi) {
						if (n < 300) {
							//Utils.debug.println(debugSpaces+"  •Set variable to number:"+funzioneTMP.calcola());
						}
					} else {
						throw new java.lang.RuntimeException("Tipo sconosciuto");
					}
					i++;
					n++;
				}
			} while (((funzioniOLD.size() != before || fase != "sistemi") && funzioniOLD.size() > 1));
			setVariables(funzioniOLD);
			
			dsl = debugSpaces.length();debugSpaces = "";for (int i = 0; i < dsl-2; i++) {debugSpaces += " ";}
			Utils.debug.println(debugSpaces+"•Finished correcting classes.");
			
			Termine result = calcola();
			Utils.debug.println(debugSpaces+"•Result:"+result);
		}
	}

	@Override
	public String simbolo() {
		return "Parentesi";
	}

	public ParteSistema calcolaSistema() throws Errore {
		return new ParteSistema(calcolaEquazione());
	}

	public Equazione calcolaEquazione() throws Errore {
		return new Equazione(calcola(), new Termine("0"));
	}
	
	@Override
	public Termine calcola() throws Errore {
		if (variables.length == 0) {
			return new Termine("0");
		} else if (variables.length == 1) {
			return variables[0].calcola();
		} else {
			Termine result = new Termine("0");
			for (Funzione f : variables) {
				result = result.add(f.calcola());
			}
			return result;
		}
	}

	@Override
	public void draw(int x, int y, Display g, boolean small) {
		if (parentesiIniziale && variables.length == 1) {
			this.variables[0].draw(x, y, g, small);
		} else {
			float miny = y;
			float maxy = y+getHeight(small);
			int lw = Utils.getPlainTextWidth("(")+1;
			int h = getHeight(small);
			g.setColor(Color.BLACK);
			if (small) {
				g.draw45Line(x+3, y, x+2, y+1, true);
				g.drawOrthoLine(x+2, y+1, x+2, y+h-2);
				g.draw45Line(x+2, y+h-2, x+3, y+h-1, false);
			} else {
				g.draw45Line(x+3, y, x+1, y+2, true);
				g.drawOrthoLine(x+1, y+2, x+1, y+h-3);
				g.draw45Line(x+1, y+h-3, x+3, y+h-1, false);
			}
			x += lw;
			for (Funzione f : variables) {
				float fheight = f.getHeight(small);
				float y2=miny+((maxy-miny)/2-fheight/2);
				f.draw(x, (int) y2, g, small);
				x+=f.getWidth();
			}
			if (small) {
				g.draw45Line(x+1, y, x+2, y+1, false);
				g.drawOrthoLine(x+2, y+1, x+2, y+h-2);
				g.draw45Line(x+2, y+h-2, x+1, y+h-1, true);
			} else {
				g.draw45Line(x+1, y, x+3, y+2, false);
				g.drawOrthoLine(x+3, y+2, x+3, y+h-3);
				g.draw45Line(x+3, y+h-3, x+1, y+h-1, true);
			}
		}
	}

	@Override
	public int getWidth() {
		if (parentesiIniziale && variables.length == 1) {
			return this.variables[0].getWidth();
		} else {
			int w = 0;
			int lw = Utils.getPlainTextWidth("(")+1;
			for (Funzione f : variables) {
				w+=f.getWidth();
			}
			return w+lw*2;
		}
	}

	@Override
	public int getHeight(boolean small) {
		if (parentesiIniziale && variables.length == 1) {
			return this.variables[0].getHeight(small);
		} else {
			Funzione tmin = null;
			Funzione tmax = null;
			for (Funzione t : variables) {
				if (tmin == null || t.getLine(small) >= tmin.getLine(small)) {
					tmin = t;
				}
				if (tmax == null || t.getHeight(small) - t.getLine(small) >= tmax.getHeight(small) - tmax.getLine(small)) {
					tmax = t;
				}
			}
			if (tmin == null) return Utils.getFontHeight(small);
			return tmin.getLine(small) + tmax.getHeight(small) - tmax.getLine(small);
		}
	}

	
	@Override
	public int getLine(boolean small) {
		if (parentesiIniziale && variables.length == 1) {
			return this.variables[0].getLine(small);
		} else {
			Funzione tl = null;
			for (Funzione t : variables) {
				if (tl == null || t.getLine(small) >= tl.getLine(small)) {
					tl = t;
				}
			}
			if (tl == null) return Utils.getFontHeight(small)/2;
			return tl.getLine(small);
		}
	}

}

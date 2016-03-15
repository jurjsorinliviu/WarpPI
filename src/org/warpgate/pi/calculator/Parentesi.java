package org.warpgate.pi.calculator;

import static org.warpgate.pi.calculator.Utils.ArrayToRegex;
import static org.warpgate.pi.calculator.Utils.concat;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nevec.rjm.BigSurdVec;

public class Parentesi extends FunzioneMultipla {

	public Parentesi() {
		super();
	}
	
	public Parentesi(Funzione[] values) {
		super(values);
	}
	
	public Parentesi(String string, String debugSpaces) throws Errore {
		super();
		try{
			//Se l'espressione è già un numero:
			setVariables(new Funzione[]{new Termine(string)});
			Utils.debug.println(debugSpaces+"•Result:"+string);
		} catch (NumberFormatException ex) {
			String processExpression = string;

			Utils.debug.println(debugSpaces+"•Analyzing expression:"+processExpression);
			debugSpaces+= "  ";
			
			//Se l'espressione non è già un numero:

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
				processExpression = processExpression.substring(0, matcher.start(0)+1)+correzione+processExpression.substring(matcher.start(0)+matcher.group(0).length(), processExpression.length());
				matcher = pattern.matcher(processExpression);
			}
			
			//Rimuovi i segni appena dopo le parentesi
			if (processExpression.contains("(+")) {
				cambiati = true;
				processExpression = processExpression.replace("(+", "(");
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
			pattern = Pattern.compile("[^"+Utils.ArrayToRegex(concat(concat(Simboli.funzioni(),Simboli.parentesi()), Simboli.segni(true)))+"]-");
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
				if (Pattern.compile("[^"+Utils.ArrayToRegex(Utils.add(concat(Simboli.funzioni(), Simboli.segni(true)), "("))+"]$").matcher(beforeexp).find()) {
					//Se la stringa precedente finisce con un numero
					beforeexp += "*";
				}
				if (Pattern.compile("^[^"+Utils.ArrayToRegex(Utils.add(concat(Simboli.funzioni(), Simboli.segni(true)), ")"))+"]").matcher(afterexp).find()) {
					//Se la stringa successiva inizia con un numero
					afterexp = "*"+afterexp;
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
			final String[] funzioni = concat(concat(Simboli.funzioni(), Simboli.parentesi()), Simboli.segni(true));
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
						f = new Parentesi(tmpExpr, debugSpaces);
						break;
					default:
						throw new java.lang.RuntimeException("Il carattere "+charI+" non è tra le funzioni designate!\nAggiungerlo ad esse o rimuovere il carattere dall'espressione!");
					}
					if (f instanceof Parentesi) {
						tmp = "";
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
						if (!(charI.equals("-") || charI.equals("."))) {
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
			
			//Inizia l'affinazione dell'espressione
			Utils.debug.println(debugSpaces+"•Pushing classes...");

			Funzione[] funzioniOLDArray = parentesiNonSuddivisaCorrettamente.getVariables();
			ArrayList<Funzione> funzioniOLD = new ArrayList<Funzione>();
			for (int i = 0; i < funzioniOLDArray.length; i++) {
				if (funzioniOLDArray[i] != null) {
					funzioniOLD.add(funzioniOLDArray[i]);
				}
			}
			
			Utils.debug.println(debugSpaces+"•Correcting classes:");

			debugSpaces += "  ";
			
			int before = 0;
			String fase = "funzioniSN";
			int n = 0;
			do {
				before = funzioniOLD.size();
				int i = 0;
				boolean change = false;
				if (Utils.ciSonoSoloNumeriESomme(funzioniOLD)) {
					fase = "somme"; //QUARTA FASE
				} else if (Utils.ciSonoFunzioniSNnonImpostate(funzioniOLD)) {
					fase = "funzioniSN"; // PRIMA FASE
				} else if (Utils.ciSonoAltreFunzioni(funzioniOLD)) {
					fase = "funzioniNSN"; //SECONDA FASE
				} else {
					fase = "moltiplicazioni"; //TERZA FASE
				}
				while (i < funzioniOLD.size() && change == false) {
					Funzione funzioneTMP = funzioniOLD.get(i);
					if (funzioneTMP instanceof FunzioneDueValori) {
						if (fase != "funzioniSN") {
							if (
									fase == "somme" && (funzioneTMP instanceof Somma) == true
								||
									(fase == "moltiplicazioni" && (funzioneTMP instanceof Somma) == false)
								||
									(fase == "funzioniNSN" && (funzioneTMP instanceof Somma) == false && (funzioneTMP instanceof Moltiplicazione) == false && (funzioneTMP instanceof Divisione) == false)
								) {
								change = true;
								if (i+1 < funzioniOLD.size() && i-1 >= 0 ) {							
									((FunzioneDueValori) funzioneTMP).setVariable1(funzioniOLD.get(i-1));
									((FunzioneDueValori) funzioneTMP).setVariable2(funzioniOLD.get(i+1));
									funzioniOLD.set(i, funzioneTMP.calcola());
									
									//è importante togliere prima gli elementi in fondo e poi quelli davanti, perché gli indici scalano da destra a sinistra.
									funzioniOLD.remove(i+1);
									funzioniOLD.remove(i-1);
									
									Utils.debug.println(debugSpaces+"•Set variable to expression:"+funzioneTMP.simbolo());
									Utils.debug.println(debugSpaces+"  "+"var1="+((FunzioneDueValori) funzioneTMP).getVariable1().calcola());
									Utils.debug.println(debugSpaces+"  "+"var2="+((FunzioneDueValori) funzioneTMP).getVariable2().calcola());
									Utils.debug.println(debugSpaces+"  "+"(result)="+((FunzioneDueValori) funzioneTMP).calcola());
								} else {
									throw new java.lang.RuntimeException("Argomenti mancanti! Sistemare l'equazione!");
								}
							}
						}
					} else if (funzioneTMP instanceof FunzioneSingola) {
						if (fase == "funzioniSN") {
							change = true;
							if (i+1 < funzioniOLD.size()) {							
								((FunzioneSingola) funzioneTMP).setVariable(funzioniOLD.get(i+1));
								funzioniOLD.set(i, funzioneTMP);
								
								//è importante togliere prima gli elementi in fondo e poi quelli davanti, perché gli indici scalano da destra a sinistra.
								funzioniOLD.remove(i+1);
								
								Utils.debug.println(debugSpaces+"•Set variable to expression:"+funzioneTMP.simbolo());
								Utils.debug.println(debugSpaces+"  "+"var="+((FunzioneSingola) funzioneTMP).getVariable().calcola());
							} else {
								throw new java.lang.RuntimeException("Argomenti mancanti! Sistemare l'equazione!");
							}
						}
					} else if (funzioneTMP instanceof Termine || funzioneTMP instanceof Parentesi) {
						if (n < 300) {
							//Utils.debug.println(debugSpaces+"•Set variable to number:"+funzioneTMP.calcola());
						}
					} else {
						throw new java.lang.RuntimeException("Tipo sconosciuto");
					}
					i++;
					n++;
				}
			} while (funzioniOLD.size() != before || fase != "somme");
			setVariables(funzioniOLD);
			
			dsl = debugSpaces.length();debugSpaces = "";for (int i = 0; i < dsl-2; i++) {debugSpaces += " ";}
			Utils.debug.println(debugSpaces+"•Finished correcting classes.");
			
			Utils.debug.println(debugSpaces+"•Result:"+calcola());
		}
	}

	@Override
	public String simbolo() {
		return "Parentesi";
	}

	@Override
	public Termine calcola() throws Errore {
		Termine result = new Termine(BigSurdVec.ZERO);
		for (Funzione f : variables) {
			result = result.add(f.calcola());
		}
		return result;
	}

}

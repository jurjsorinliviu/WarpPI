package org.warpgate.pi.calculator;

import java.awt.Graphics;
import java.math.BigInteger;

import org.nevec.rjm.NumeroAvanzatoVec;

public class Moltiplicazione extends FunzioneDueValori {

	public Moltiplicazione(Funzione value1, Funzione value2) {
		super(value1, value2);
	}

	@Override
	public String simbolo() {
		return Simboli.MULTIPLICATION;
	}

	@Override
	public Termine calcola() throws Errore {
		return getVariable1().calcola().multiply(getVariable2().calcola());
	}
	
	@Override
	public boolean drawSignum() {
		Funzione[] tmpVar = new Funzione[]{variable1, variable2};
		boolean[] ok = new boolean[]{false, false};
		for (int val = 0; val < 2; val++) {
			while (!ok[val]) {
				if (tmpVar[val] instanceof Divisione) {
					ok[0] = true;
					ok[1] = true;
				} else if (tmpVar[val] instanceof Incognita) {
					ok[val] = true;
				} else if (tmpVar[val] instanceof Termine) {
					if (val == 0) {
						ok[val] = true;
					} else {
						if (!(tmpVar[0] instanceof Termine)) {
							ok[val] = true;
						} else {
							if (((Termine)tmpVar[0]).term.isBigInteger(false)) {
								if (((Termine)tmpVar[val]).term.toBigInteger(true).compareTo(new BigInteger("1")) == 0) {
									if (((Termine)tmpVar[val]).term.toNumeroAvanzato().getIncognitey().count() > 0) {
										ok[val] = true;
									} else {
										break;
									}
								} else {
									break;
								}
							} else {
								ok[val] = true;
							}
						}
					}
				} else if (tmpVar[val] instanceof Potenza) {
					tmpVar[val] = ((Potenza)tmpVar[val]).variable1;
				} else if (tmpVar[val] instanceof Radice) {
					ok[val] = true;
				} else if (tmpVar[val] instanceof RadiceQuadrata) {
					ok[val] = true;
				} else if (tmpVar[val] instanceof Parentesi) {
					ok[0] = true;
					ok[1] = true;
				} else if (tmpVar[val] instanceof FunzioneDueValori) {
					if (val == 0) {
						tmpVar[val] = ((FunzioneDueValori)tmpVar[val]).variable2;
					} else {
						tmpVar[val] = ((FunzioneDueValori)tmpVar[val]).variable1;
					}
				} else if (tmpVar[val] instanceof FunzioneAnteriore) {
					tmpVar[val] = ((FunzioneAnteriore)tmpVar[val]).variable;
				}
			}
		}
		
		if (ok[0] == true && ok[1] == true) {
			return false;
		} else {
			return true;
		}
	}
}
package org.warp.picalculator;

import java.math.BigInteger;

public class Moltiplicazione extends FunzioneDueValoriBase {

	public Moltiplicazione(FunzioneBase value1, FunzioneBase value2) {
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
		Funzione[] tmpVar = new Funzione[] { variable1, variable2 };
		boolean[] ok = new boolean[] { false, false };
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
							if (((Termine) tmpVar[val]).term.isBigInteger(false)) { // TODO: prima era tmpVar[0], ma crashava. RICONTROLLARE! La logica potrebbe essere sbagliata
								if (((Termine) tmpVar[val]).term.toBigInteger(true).compareTo(new BigInteger("1")) == 0) {
									if (((Termine) tmpVar[val]).term.toNumeroAvanzato().getIncognitey().count() > 0) {
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
					tmpVar[val] = ((Potenza) tmpVar[val]).variable1;
				} else if (tmpVar[val] instanceof Radice) {
					ok[val] = true;
				} else if (tmpVar[val] instanceof RadiceQuadrata) {
					ok[val] = true;
				} else if (tmpVar[val] instanceof Espressione) {
					ok[0] = true;
					ok[1] = true;
				} else if (tmpVar[val] instanceof FunzioneDueValoriBase) {
					if (val == 0) {
						tmpVar[val] = ((FunzioneDueValoriBase) tmpVar[val]).variable2;
					} else {
						tmpVar[val] = ((FunzioneDueValoriBase) tmpVar[val]).variable1;
					}
				} else if (tmpVar[val] instanceof FunzioneAnterioreBase) {
					tmpVar[val] = ((FunzioneAnterioreBase) tmpVar[val]).variable;
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
package org.warpgate.pi.calculator;

import org.nevec.rjm.BigSurdVec;

public class Divisione extends FunzioneDueValori {

	public Divisione(Funzione value1, Funzione value2) {
		super(value1, value2);
	}

	@Override
	public String simbolo() {
		return Simboli.DIVISION;
	}

	@Override
	public Termine calcola() throws Errore {
		if (getVariable2().calcola().getTerm().compareTo(BigSurdVec.ZERO) == 0) {
			throw new Errore(Errori.DIVISION_BY_ZERO);
		}
		return getVariable1().calcola().divide(getVariable2().calcola());
	}
}
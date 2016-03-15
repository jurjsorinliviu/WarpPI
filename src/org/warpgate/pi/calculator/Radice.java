package org.warpgate.pi.calculator;

import org.nevec.rjm.BigSurdVec;

public class Radice extends FunzioneDueValori {
	
	public Radice(Funzione value1, Funzione value2) {
		super(value1, value2);
	}

	@Override
	public String simbolo() {
		return Simboli.NTH_ROOT;
	}

	@Override
	public Termine calcola() throws NumberFormatException, Errore {
		Termine exponent = new Termine(BigSurdVec.ONE);
		exponent = exponent.divide(getVariable1().calcola());
		return getVariable2().calcola().pow(exponent);
	}
}

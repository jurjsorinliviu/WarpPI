package org.warpgate.pi.calculator;

public class Potenza extends FunzioneDueValori {
	
	public Potenza(Funzione value1, Funzione value2) {
		super(value1, value2);
	}

	@Override
	public String simbolo() {
		return Simboli.POTENZA;
	}

	@Override
	public Termine calcola() throws NumberFormatException, Errore {
		return getVariable1().calcola().pow(getVariable2().calcola());
	}
}

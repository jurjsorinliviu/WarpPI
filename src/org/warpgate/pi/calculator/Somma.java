package org.warpgate.pi.calculator;

public class Somma extends FunzioneDueValori {

	public Somma(Funzione value1, Funzione value2) {
		super(value1, value2);
	}

	@Override
	public String simbolo() {
		return Simboli.SUM;
	}

	@Override
	public Termine calcola() throws Errore {
		return getVariable1().calcola().add(getVariable2().calcola());
	}

}

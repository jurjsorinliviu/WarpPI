package org.warpgate.pi.calculator;

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
}
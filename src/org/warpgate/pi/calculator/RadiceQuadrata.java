package org.warpgate.pi.calculator;

import org.nevec.rjm.Rational;

public class RadiceQuadrata extends FunzioneSingola {

	public RadiceQuadrata(Funzione value) {
		super(value);
	}

	@Override
	public String simbolo() {
		return Simboli.SQUARE_ROOT;
	}

	@Override
	public Termine calcola() throws NumberFormatException, Errore {
		return getVariable().calcola().pow(new Termine(Rational.ONE.divide(new Rational(2,1))));
	}
}

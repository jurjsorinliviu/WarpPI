package org.warpgate.pi.calculator;

import org.nevec.rjm.Rational;

public abstract class FunzioneDueValori implements Funzione {
	public FunzioneDueValori(Funzione value1, Funzione value2) {
		setVariable1(value1);
		setVariable2(value2);
	}
	protected Funzione variable1 = new Termine(Rational.ZERO);
	public Funzione getVariable1() {
		return variable1;
	}
	public void setVariable1(Funzione value) {
		variable1 = value;
	}
	protected Funzione variable2 = new Termine(Rational.ZERO);
	public Funzione getVariable2() {
		return variable2;
	}
	public void setVariable2(Funzione value) {
		variable2 = value;
	}
	@Override
	public abstract String simbolo();
	@Override
	public abstract Termine calcola() throws Errore;
}

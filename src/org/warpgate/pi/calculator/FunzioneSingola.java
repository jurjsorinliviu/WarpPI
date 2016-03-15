package org.warpgate.pi.calculator;

import org.nevec.rjm.BigSurdVec;

public abstract class FunzioneSingola implements Funzione {
	public FunzioneSingola(Funzione value) {
		setVariable(value);
	}
	protected Funzione variable = new Termine(BigSurdVec.ZERO);
	public Funzione getVariable() {
		return variable;
	}
	public void setVariable(Funzione value) {
		variable = value;
	}
	@Override
	public abstract String simbolo();
	@Override
	public abstract Termine calcola() throws Errore;
}

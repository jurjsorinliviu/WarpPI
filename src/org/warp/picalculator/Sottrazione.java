package org.warp.picalculator;

public class Sottrazione extends FunzioneDueValoriBase {

	public Sottrazione(FunzioneBase value1, FunzioneBase value2) {
		super(value1, value2);
	}

	@Override
	public String simbolo() {
		return Simboli.SUBTRACTION;
	}

	@Override
	public Termine calcola() throws Errore {
		return getVariable1().calcola().add(getVariable2().calcola().multiply(new Termine("-1")));
	}

}
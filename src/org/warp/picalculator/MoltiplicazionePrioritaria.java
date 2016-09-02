package org.warp.picalculator;

public class MoltiplicazionePrioritaria extends FunzioneDueValoriBase {

	public MoltiplicazionePrioritaria(FunzioneBase value1, FunzioneBase value2) {
		super(value1, value2);
	}

	@Override
	public String simbolo() {
		return Simboli.PRIORITARY_MULTIPLICATION;
	}

	@Override
	public Termine calcola() throws Errore {
		return getVariable1().calcola().multiply(getVariable2().calcola());
	}

	@Override
	public boolean drawSignum() {
		return false;
	}
}
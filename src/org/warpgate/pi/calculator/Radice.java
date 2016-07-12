package org.warpgate.pi.calculator;

import org.nevec.rjm.NumeroAvanzatoVec;
import org.warp.engine.Display;

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
		Termine exponent = new Termine(NumeroAvanzatoVec.ONE);
		exponent = exponent.divide(getVariable1().calcola());
		return getVariable2().calcola().pow(exponent);
	}

	@Override
	public void draw(int x, int y, Display g, boolean small) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight(boolean small) {
		// TODO Auto-generated method stub
		return 0;
	}
}

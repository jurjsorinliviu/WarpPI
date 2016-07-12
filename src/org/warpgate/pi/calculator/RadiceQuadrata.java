package org.warpgate.pi.calculator;

import org.nevec.rjm.NumeroAvanzatoVec;
import org.nevec.rjm.Rational;
import org.warp.engine.Display;

public class RadiceQuadrata extends FunzioneAnteriore {

	public RadiceQuadrata(Funzione value) {
		super(value);
	}
	
	@Override
	public String simbolo() {
		return Simboli.SQUARE_ROOT;
	}

	@Override
	public Termine calcola() throws NumberFormatException, Errore {
		Termine result = getVariable().calcola().pow(new Termine(new Rational(1,2)));
		return result;
	}
	

	@Override
	public void draw(int x, int y, Display g, boolean small) {
		Utils.writeSquareRoot(g, getVariable(), x, y, small);
	}

	@Override
	public int getWidth() {
		return 5+getVariable().getWidth()+2;
	}

	@Override
	public int getHeight(boolean small) {
		int h1 = getVariable().getHeight(small)+2;
		return h1;
	}

	@Override
	public int getLine(boolean small) {
		int h1 = getVariable().getLine(small)+2;
		return h1;
	}
}

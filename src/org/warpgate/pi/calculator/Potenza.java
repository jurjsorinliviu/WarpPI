package org.warpgate.pi.calculator;

import org.warp.engine.Display;

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

	@Override
	public void draw(int x, int y, Display g, boolean small) {
		int dx = 0;
		variable1.draw(dx+x, getHeight(small)-variable1.getHeight(small)+y, g, small);
		dx+=variable1.getWidth()+1;
		variable2.draw(dx+x, y, g, small);
	}

	@Override
	public int getHeight(boolean small) {
		return variable1.getHeight(small)+variable2.getHeight(true)-4;
	}
	
	@Override
	public int getLine(boolean small) {
		return variable2.getHeight(true)-4+variable1.getLine(small);
	}
	
	@Override
	public int getWidth() {
		int w1 = getVariable1().getWidth();
		int w2 = getVariable2().getWidth();

		return w1+1+w2;
	}
}

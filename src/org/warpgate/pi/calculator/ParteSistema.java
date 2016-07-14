package org.warpgate.pi.calculator;

import java.awt.Color;

import org.nevec.rjm.Rational;
import org.warp.engine.Display;

public class ParteSistema extends FunzioneAnteriore {

	public ParteSistema(Funzione value) {
		super(value);
	}

	@Override
	public String simbolo() {
		return Simboli.SYSTEM;
	}

	@Override
	public Termine calcola() throws NumberFormatException, Errore {
		//TODO implementare il calcolo dei sistemi
		return variable.calcola();
	}
	

	@Override
	public void draw(int x, int y, Display g, boolean small) {
		small = false;
		final int h = this.getHeight(small)-1;
		final int varLine = variable.getLine(small);
		final int paddingTop = 3;
		final int spazioSotto = (h-3-2)/2+paddingTop;
		final int spazioSopra = h - spazioSotto;
		variable.draw(x+5, y+paddingTop, g, small);
		g.setColor(Color.black);
		g.drawOrthoLine(x+2, y+0, x+3, y+0);
		g.drawOrthoLine(x+1, y+1, x+1, y+spazioSotto/2);
		g.drawOrthoLine(x+2, y+spazioSotto/2+1, x+2, y+spazioSotto-1);
		g.drawOrthoLine(x+0, y+spazioSotto, x+1, y+spazioSotto);
		g.drawOrthoLine(x+2, y+spazioSotto+1, x+2, y+spazioSotto+spazioSopra/2-1);
		g.drawOrthoLine(x+1, y+spazioSotto+spazioSopra/2, x+1, y+h-1);
		g.drawOrthoLine(x+2, y+h, x+3, y+h);
	}

	@Override
	public int getWidth() {
		return 5+getVariable().getWidth();
	}

	@Override
	public int getHeight(boolean small) {
		small = false;
		int h1 = 3+getVariable().getHeight(small)+2;
		return h1;
	}

	@Override
	public int getLine(boolean small) {
		small = false;
		int h1 = 3+getVariable().getLine(small);
		return h1;
	}
}

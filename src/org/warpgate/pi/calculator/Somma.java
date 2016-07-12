package org.warpgate.pi.calculator;

import java.awt.Color;
import java.math.BigInteger;

import org.nevec.rjm.NumeroAvanzatoVec;
import org.warp.engine.Display;

public class Somma extends FunzioneDueValori {

	public Somma(Funzione value1, Funzione value2) {
		super(value1, value2);
	}

	@Override
	public String simbolo() {
		return Simboli.SUM;
	}

	@Override
	public Termine calcola() throws Errore {
		Termine val1 = getVariable1().calcola();
		Termine val2 = getVariable2().calcola();
		Termine result = val1.add(val2);
		return result;
	}
	

	@Override
	public void draw(int x, int y, Display g, boolean small) {
		int ln = getLine(small);
		int dx = 0;
		variable1.draw(dx+x, ln-variable1.getLine(small)+y, g, small);
		dx+=variable1.getWidth()+1;
		try {
			NumeroAvanzatoVec tm = variable2.calcola().getTerm();

				Utils.writeLetter(g, simbolo(), dx+x, ln-Utils.getFontHeight(small)/2+y, small);
				dx+=Utils.getPlainTextWidth(simbolo());
				dx+=1;

		} catch (Errore e) {
			e.printStackTrace();
		}
		variable2.draw(dx+x, ln-variable2.getLine(small)+y, g, small);
	}

	@Override
	public int getWidth() {
		int dx = 0;
		dx+=variable1.getWidth()+1;
		try {
			NumeroAvanzatoVec tm = variable2.calcola().getTerm();
				dx+=Utils.getPlainTextWidth(simbolo());
				dx+=1;
		} catch (Errore e) {
			e.printStackTrace();
		}
		return dx+=variable2.getWidth();
	}
}

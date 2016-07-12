package org.warpgate.pi.calculator;

import org.nevec.rjm.NumeroAvanzatoVec;
import org.warp.engine.Display;

import com.rits.cloning.Cloner;

public abstract class FunzioneAnteriore implements Funzione {
	public FunzioneAnteriore(Funzione value) {
		setVariable(value);
	}
	protected Funzione variable = new Termine(NumeroAvanzatoVec.ZERO);
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
	

	@Override
	public void draw(int x, int y, Display g, boolean small) {
		int w1 = getVariable().getWidth();
		float h1 = getVariable().getHeight(small);
		int wsegno = Utils.getPlainTextWidth(simbolo());
		float hsegno = Utils.getFontHeight(small);
		float maxh = getHeight(small);

		Utils.writeLetter(g, simbolo(), x, (int)Math.floor(y+(maxh-hsegno)/2), small);
		getVariable().draw(x+wsegno+1, (int)Math.floor(y+(maxh-h1)/2), g, small);
	}

	@Override
	public int getWidth() {
		return Utils.getPlainTextWidth(simbolo())+1+getVariable().getWidth();
	}

	@Override
	public int getHeight(boolean small) {
		return variable.getHeight(small);
	}
	
	@Override
	public int getLine(boolean small) {
		return variable.getLine(small);
	}
	
	@Override
	public String toString() {
		try {
			return calcola().toString();
		} catch (Errore e) {
			return e.id.toString();
		}
	}
	
	@Override
	public FunzioneAnteriore clone() {
		Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}
}

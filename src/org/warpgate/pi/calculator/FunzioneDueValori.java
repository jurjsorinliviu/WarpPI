package org.warpgate.pi.calculator;

import org.nevec.rjm.Rational;
import org.warp.engine.Display;

import com.rits.cloning.Cloner;

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
	

	@Override
	public void draw(int x, int y, Display g, boolean small) {
		int ln = getLine(small);
		int dx = 0;
		variable1.draw(dx+x, ln-variable1.getLine(small)+y, g, small);
		dx+=variable1.getWidth()+1;
		if (drawSignum()) {
			Utils.writeLetter(g, simbolo(), dx+x, ln-Utils.getFontHeight(small)/2+y, small);
			dx+=1;
			dx+=Utils.getPlainTextWidth(simbolo());
		}
		variable2.draw(dx+x, ln-variable2.getLine(small)+y, g, small);
	}

	@Override
	public int getWidth() {
		return variable1.getWidth()+1+ (drawSignum() ? Utils.getPlainTextWidth(simbolo())+1 : 0) +variable2.getWidth();
	}

	@Override
	public int getHeight(boolean small) {
		Funzione tmin = variable1;
		Funzione tmax = variable1;
		if (tmin == null || variable2.getLine(small) >= tmin.getLine(small)) {
			tmin = variable2;
		}
		if (tmax == null || variable2.getHeight(small) - variable2.getLine(small) >= tmax.getHeight(small) - tmax.getLine(small)) {
			tmax = variable2;
		}
		return tmin.getLine(small) + tmax.getHeight(small) - tmax.getLine(small);
	}
	
	@Override
	public int getLine(boolean small) {
		Funzione tl = variable1;
		if (tl == null || variable2.getLine(small) >= tl.getLine(small)) {
			tl = variable2;
		}
		return tl.getLine(small);
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
	public FunzioneDueValori clone() {
		Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}
	
	public boolean drawSignum() {
		return true;
	}
}

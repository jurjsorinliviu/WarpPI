package org.warpgate.pi.calculator;

import com.rits.cloning.Cloner;

public interface Funzione {
	public String simbolo();
	public Termine calcola() throws Errore;
	public void draw(int x, int y, boolean small);
	public int getWidth();
	public int getHeight(boolean small);
	public int getLine(boolean small);
}

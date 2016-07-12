package org.warpgate.pi.calculator;

import org.warp.engine.Display;

import com.rits.cloning.Cloner;

public interface Funzione {
	public String simbolo();
	public Termine calcola() throws Errore;
	public void draw(int x, int y, Display g, boolean small);
	public int getWidth();
	public int getHeight(boolean small);
	public int getLine(boolean small);
}

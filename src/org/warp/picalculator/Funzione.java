package org.warp.picalculator;

public interface Funzione {
	public String simbolo();

	public Funzione calcola() throws Errore;

	public void calcolaGrafica();

	public void draw(int x, int y);

	public int getWidth();

	public int getHeight();

	public int getLine();
	
	public void setSmall(boolean small);
}

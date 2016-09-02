package org.warp.picalculator;

public abstract class FunzioneBase implements Funzione {

	@Override
	public abstract String simbolo();

	@Override
	public abstract Termine calcola() throws Errore;

	@Override
	public abstract void calcolaGrafica();

	@Override
	public abstract void draw(int x, int y);

	@Override	
	public abstract int getWidth();

	@Override
	public abstract int getHeight();

	@Override
	public abstract int getLine();

	@Override
	public abstract void setSmall(boolean small);

}

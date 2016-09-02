package org.warp.picalculator;

import static org.warp.engine.Display.Render.glDrawLine;

public class Sistema extends FunzioneMultipla {
	static final int spacing = 2;
	
	public Sistema() {
		super();
	}
	
	public Sistema(Funzione value) {
		super(new Funzione[]{value});
	}
	
	public Sistema(Funzione[] value) {
		super(value);
	}

	@Override
	public String simbolo() {
		return null;
	}

	@Override
	public Funzione calcola() throws NumberFormatException, Errore {
		// TODO implementare il calcolo dei sistemi
		return variables[0].calcola();
	}

	@Override
	public void calcolaGrafica() {
		for (Funzione f : variables) {
			f.setSmall(false);
			f.calcolaGrafica();
		}
		
		width = 0;
		for (Funzione f : variables) {
			if (f.getWidth() > width) {
				width = f.getWidth();
			}
		}
		width += 5;
		
		height = 3;
		for (Funzione f : variables) {
			height += f.getHeight()+spacing;
		}
		height = height - spacing + 2;
		
		line = height/2;
	}
	
	@Override
	public void draw(int x, int y) {

		final int h = this.getHeight() - 1;
		final int paddingTop = 3;
		final int spazioSotto = (h - 3 - 2) / 2 + paddingTop;
		final int spazioSopra = h - spazioSotto;
		int dy = paddingTop;
		for (Funzione f : variables) {
			f.draw(x + 5, y + dy);
			dy+=f.getHeight()+spacing;
		}
		
		
		glDrawLine(x + 2, y + 0, x + 3, y + 0);
		glDrawLine(x + 1, y + 1, x + 1, y + spazioSotto / 2);
		glDrawLine(x + 2, y + spazioSotto / 2 + 1, x + 2, y + spazioSotto - 1);
		glDrawLine(x + 0, y + spazioSotto, x + 1, y + spazioSotto);
		glDrawLine(x + 2, y + spazioSotto + 1, x + 2, y + spazioSotto + spazioSopra / 2 - 1);
		glDrawLine(x + 1, y + spazioSotto + spazioSopra / 2, x + 1, y + h - 1);
		glDrawLine(x + 2, y + h, x + 3, y + h);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getLine() {
		return line;
	}
}

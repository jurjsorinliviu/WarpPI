package org.warp.picalculator;

public class Potenza extends FunzioneDueValoriBase {

	public Potenza(FunzioneBase value1, FunzioneBase value2) {
		super(value1, value2);
	}

	@Override
	public String simbolo() {
		return Simboli.POTENZA;
	}

	@Override
	public void calcolaGrafica() {
		variable1.setSmall(small);
		variable1.calcolaGrafica();
		
		variable2.setSmall(true);
		variable2.calcolaGrafica();
		
		height = variable1.getHeight() + variable2.getHeight() - 4;
		line = variable2.getHeight() - 4 + variable1.getLine();
		width = getVariable1().getWidth() + getVariable2().getWidth()+1;
	}

	@Override
	public Termine calcola() throws NumberFormatException, Errore {
		return getVariable1().calcola().pow(getVariable2().calcola());
	}

	@Override
	public void draw(int x, int y) {
//		glColor3f(0, 127-50+new Random().nextInt(50), 0);
//		glFillRect(x,y,width,height);
//		glColor3f(0, 0, 0);
		
		int dx = 0;
		variable1.draw(dx + x, getHeight() - variable1.getHeight() + y);
		dx += variable1.getWidth();
		variable2.draw(dx + x, y);
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getLine() {
		return line;
	}

	@Override
	public int getWidth() {
		return width;
	}
}

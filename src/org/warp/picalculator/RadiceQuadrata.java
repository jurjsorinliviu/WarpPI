package org.warp.picalculator;

import org.nevec.rjm.Rational;

public class RadiceQuadrata extends FunzioneAnterioreBase {

	public RadiceQuadrata(FunzioneBase value) {
		super(value);
	}

	@Override
	public String simbolo() {
		return Simboli.SQUARE_ROOT;
	}
	
	@Override
	public void calcolaGrafica() {
		variable.setSmall(small);
		variable.calcolaGrafica();
		
		height = getVariable().getHeight() + 2;
		width = 1 + 4 + getVariable().getWidth() + 1;
		line = getVariable().getLine() + 2;
	}

	@Override
	public Termine calcola() throws Errore {
		try {
			Termine result = getVariable().calcola();
			result = result.pow(new Termine(new Rational(1, 2)));
			return result;
		} catch(NullPointerException ex) {
			throw new Errore(Errori.ERROR);
		} catch(NumberFormatException ex) {
			throw new Errore(Errori.SYNTAX_ERROR);
		} catch(ArithmeticException ex) {
			throw new Errore(Errori.NUMBER_TOO_SMALL);
		}
	}

	@Override
	public void draw(int x, int y) {
//		glColor3f(0, 255, 0);
//		glFillRect(x,y,width,height);
//		glColor3f(0, 0, 0);
		
		Utils.writeSquareRoot(getVariable(), x, y, small);
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

package org.warpgate.pi.calculator;

import java.awt.Color;
import java.awt.Graphics;

import org.nevec.rjm.NumeroAvanzatoVec;
import org.nevec.rjm.Rational;
import org.warp.engine.Display;

public class Divisione extends FunzioneDueValori {

	public Divisione(Funzione value1, Funzione value2) {
		super(value1, value2);
	}

	@Override
	public String simbolo() {
		return Simboli.DIVISION;
	}

	@Override
	public Termine calcola() throws Errore {
		if (variable2 == null || variable1 == null) {
			return new Termine("0");
		}
		if (variable2.calcola().getTerm().compareTo(NumeroAvanzatoVec.ZERO) == 0) {
			throw new Errore(Errori.DIVISION_BY_ZERO);
		}
		return variable1.calcola().divide(variable2.calcola());
	}

	public boolean hasMinus() {
		String numerator = variable1.toString();
		if (numerator.startsWith("-")) {
			return true;
		}
		return false;
	}

	public void draw(int x, int y, Display g, boolean small, boolean drawMinus) {
		boolean beforedrawminus = this.drawMinus;
		this.drawMinus = drawMinus;
		draw(x, y, g, small);
		this.drawMinus = beforedrawminus;
	}
	
	private boolean drawMinus = true;
	
	@Override
	public void draw(int x, int y, Display g, boolean small) {
		Object var1 = variable1;
		Object var2 = variable2;
		small = true;
		boolean minus = false;
		int minusw = 0;
		int minush = 0;
		String numerator = ((Funzione)var1).toString();
		if (numerator.startsWith("-") && ((Funzione)var1) instanceof Termine && ((Termine)var1).term.isBigInteger(true)) {
			minus = true;
			numerator = numerator.substring(1);
		}
		int w1 = 0;
		int h1 = 0;
		if (minus) {
			w1 = Utils.getPlainTextWidth(numerator);
			h1 = Utils.getFontHeight(small);
		} else {
			w1 = ((Funzione)var1).getWidth();
			h1 = ((Funzione)var1).getHeight(small);
		}
		int w2 = ((Funzione)var2).getWidth();
		int maxw;
		if (w1 > w2) {
			maxw = 1+w1+1;
		} else {
			maxw = 1+w2+1;
		}
		if (minus && drawMinus) {
			minusw = Utils.getPlainTextWidth("-")+1;
			minush = Utils.getFontHeight(small);
			Utils.writeLetter(g, "-", x, y+h1+1+1-(minush/2), small);
			Utils.writeLetter(g, numerator, (int)(x+minusw+1+((double)(maxw-w1))/2d), y, small);
		} else {
			((Funzione)var1).draw((int)(x+minusw+1+((double)(maxw-w1))/2d), y, g, true);
		}
		((Funzione)var2).draw((int)(x+minusw+1+((double)(maxw-w2))/2d), y+h1+1+1+1, g, true);
		g.setColor(Color.BLACK);
		g.fillRect(x+minusw+1, y+h1+1, maxw, 1);
	}

	public int getHeight(boolean small, boolean drawMinus) {
		boolean beforedrawminus = this.drawMinus;
		this.drawMinus = drawMinus;
		int h = getHeight(small);
		this.drawMinus = beforedrawminus;
		return h;
	}

	@Override
	public int getHeight(boolean small) {
		boolean minus = false;
		small = true;
		String numerator = variable1.toString();
		if (numerator.startsWith("-")  && variable1 instanceof Termine && ((Termine) variable1).term.isBigInteger(true)) {
			minus = true;
			numerator = numerator.substring(1);
		}
		int w1 = 0;
		int h1 = 0;
		if (minus) {
			w1 = Utils.getPlainTextWidth(numerator);
			h1 = Utils.getFontHeight(small);
		} else {
			w1 = variable1.getWidth();
			h1 = variable1.getHeight(small);
		}
		int w2 = variable2.getWidth();
		int h2 = variable2.getHeight(small);
		int maxw;
		if (w1 > w2) {
			maxw = 1+w1+1;
		} else {
			maxw = 1+w2+1;
		}
		return h1+3+h2;
	}

	@Override
	public int getLine(boolean small) {
		return variable1.getHeight(true)+1;
	}

	public int getWidth(boolean drawMinus) {
		boolean beforedrawminus = this.drawMinus;
		this.drawMinus = drawMinus;
		int w = getWidth();
		this.drawMinus = beforedrawminus;
		return w;
	}

	@Override
	public int getWidth() {
		boolean minus = false;
		String numerator = variable1.toString();
		if (numerator.startsWith("-") && variable1 instanceof Termine && ((Termine) variable1).term.isBigInteger(true)) {
			minus = true;
			numerator = numerator.substring(1);
		}
		int w1 = 0;
		int h1 = 0;
		if (minus) {
			w1 = Utils.getPlainTextWidth(numerator);
		} else {
			w1 = variable1.getWidth();
		}
		int w2 = variable2.getWidth();
		int maxw;
		if (w1 > w2) {
			maxw = 1+w1+1;
		} else {
			maxw = 1+w2+1;
		}
		if (minus && drawMinus) {
			return Utils.getPlainTextWidth("-")+1+maxw+1;
		} else {
			return maxw+2;
		}
	}
}
package org.warp.picalculator;

import static org.warp.engine.Display.Render.getStringWidth;
import static org.warp.engine.Display.Render.glColor3f;
import static org.warp.engine.Display.Render.glDrawStringLeft;
import static org.warp.engine.Display.Render.glFillRect;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import org.nevec.rjm.BigDecimalMath;
import org.nevec.rjm.NumeroAvanzato;
import org.nevec.rjm.NumeroAvanzatoVec;
import org.nevec.rjm.Rational;
import org.warp.device.PIDisplay;
import org.warp.engine.Display;

import com.rits.cloning.Cloner;

public class Termine extends FunzioneBase {

	protected NumeroAvanzatoVec term = NumeroAvanzatoVec.ZERO;
	protected int width;
	protected int height;
	protected int line;
	protected boolean small;

	public Termine(NumeroAvanzatoVec val) {
		term = val;
	}

	public Termine(String s) throws Errore {
		term = new NumeroAvanzatoVec(new NumeroAvanzato(Utils.getRational(s), Rational.ONE));
	}

	public Termine(Rational r) {
		term = new NumeroAvanzatoVec(new NumeroAvanzato(r, Rational.ONE));
	}

	public Termine(BigInteger r) {
		term = new NumeroAvanzatoVec(new NumeroAvanzato(new Rational(r, BigInteger.ONE), Rational.ONE));
	}

	public Termine(BigDecimal r) {
		term = new NumeroAvanzatoVec(new NumeroAvanzato(Utils.getRational(r), Rational.ONE));
	}

	public Termine(NumeroAvanzato numeroAvanzato) {
		term = new NumeroAvanzatoVec(numeroAvanzato);
	}

	public NumeroAvanzatoVec getTerm() {
		return term;
	}

	public void setTerm(NumeroAvanzatoVec val) {
		term = val;
	}

	@Override
	public void calcolaGrafica() {
		line = calcLine(); //TODO pp
		height = calcHeight();
		width = calcWidth();
	}
	
	@Override
	public Termine calcola() {
		return this;
	}

	@Override
	public String simbolo() {
		return toString();
	}

	public Termine add(Termine f) throws Errore {
		Termine ret = new Termine(getTerm().add(f.getTerm()));
		return ret;
	}

	public Termine multiply(Termine f) throws Errore {
		Termine ret = new Termine(getTerm().multiply(f.getTerm()));
		return ret;
	}

	public Termine divide(Termine f) throws Errore {
		Termine ret = new Termine(getTerm().divide(f.getTerm()));
		return ret;
	}

	public Termine pow(Termine f) throws Errore {
		Termine ret = new Termine(NumeroAvanzatoVec.ONE);
		if (f.getTerm().isBigInteger(true)) {
			for (BigInteger i = BigInteger.ZERO; i.compareTo(f.getTerm().toBigInteger(true)) < 0; i = i.add(BigInteger.ONE)) {
				ret = ret.multiply(new Termine(getTerm()));
			}
		} else if (getTerm().isRational(true) && f.getTerm().isRational(false) && f.getTerm().toRational(false).compareTo(Rational.HALF) == 0) {
			// Rational originalExponent = f.getTerm().toRational();
			// Rational rootExponent = new Rational(originalExponent.denom(),
			// originalExponent.numer());
			Rational numberToRoot = getTerm().toRational(true);
			NumeroAvanzato na = new NumeroAvanzato(Rational.ONE, numberToRoot);
			na = na.setIncognitex(getTerm().toNumeroAvanzato().getIncognitey().multiply(getTerm().toNumeroAvanzato().getIncognitez()));
			na = na.setIncognitey(new Incognite());
			na = na.setIncognitez(getTerm().toNumeroAvanzato().getIncognitez());
			ret = new Termine(na);
		} else {
			ret = new Termine(BigDecimalMath.pow(getTerm().BigDecimalValue(new MathContext(Utils.scale, Utils.scaleMode2)), f.getTerm().BigDecimalValue(new MathContext(Utils.scale, Utils.scaleMode2))));
		}
		return ret;
	}

	@Override
	public String toString() {
		return getTerm().toFancyString();
	}

//	public void draw(int x, int y, PIDisplay g, boolean small, boolean drawMinus) {
//		boolean beforedrawminus = this.drawMinus;
//		this.drawMinus = drawMinus;
//		draw(x, y, small);
//		this.drawMinus = beforedrawminus;
//	}

	private boolean drawMinus = true;

	@Override
	public void draw(int x, int y) {
		
		if (getTerm().isBigInteger(false)) {
			if (small) {
				Display.Render.setFont(PIDisplay.fonts[1]);
			} else {
				Display.Render.setFont(PIDisplay.fonts[0]);
			}
			String t = toString();

			if (t.startsWith("-")) {
				if (drawMinus) {

				} else {
					t = t.substring(1);
				}
			}
			glDrawStringLeft(x+1, y, t);
		} else if (getTerm().isRational(false)) {
			small = true;
			Display.Render.setFont(PIDisplay.fonts[1]);
			Rational r = getTerm().toRational(false);
			boolean minus = false;
			int minusw = 0;
			int minush = 0;
			String numerator = r.numer().toString();
			if (numerator.startsWith("-")) {
				minus = true;
				numerator = numerator.substring(1);
			}
			int w1 = getStringWidth(numerator);
			int h1 = Utils.getFontHeight(small);
			int w2 = getStringWidth(r.denom().toString());
			int maxw;
			if (w1 > w2) {
				maxw = 1 + w1 + 1;
			} else {
				maxw = 1 + w2 + 1;
			}
			if (minus) {
				if (drawMinus) {
					minusw = getStringWidth("-");
					minush = Utils.getFontHeight(small);
					maxw += minusw;
					glDrawStringLeft(x, y + h1 + 1 + 1 - (minush / 2), "-");
				}
			}
			glDrawStringLeft((int) (x + minusw + 1 + (maxw - w1) / 2d), y, numerator);
			glDrawStringLeft((int) (x + minusw + 1 + (maxw - w2) / 2d), y + h1 + 1 + 1 + 1, r.denom().toString());
			glColor3f(0, 0, 0);
			glFillRect(x + minusw + 1, y + h1 + 1, maxw, 1);
		} else if (getTerm().toFancyString().contains("/")) {
			small = true;
			Display.Render.setFont(PIDisplay.fonts[1]);
			String r = getTerm().toFancyString();
			String numer = r.substring(0, r.lastIndexOf("/"));
			String denom = r.substring(numer.length() + 1, r.length());
			if (numer.startsWith("(") && numer.endsWith(")")) {
				numer = numer.substring(1, numer.length() - 1);
			}
			boolean minus = false;
			if (numer.startsWith("-")) {
				minus = true;
				numer = numer.substring(1);
			}
			int w1 = getStringWidth(numer.toString());
			int h1 = Utils.getFontHeight(small);
			int w2 = getStringWidth(denom.toString());
			int maxw;
			if (w1 > w2) {
				maxw = w1 + 2;
			} else {
				maxw = w2 + 2;
			}
			int minusw = 0;
			int minush = 0;
			if (minus) {
				if (drawMinus) {
					minusw = getStringWidth("-") + 1;
					minush = Utils.getFontHeight(small);
					maxw += minusw;
					glDrawStringLeft(x, y + h1 + 1 + 1 - (minush / 2), "-");
				}
			}
			glDrawStringLeft((int) (x + minusw + 1 + (maxw - w1) / 2d), y, numer);
			glDrawStringLeft((int) (x + minusw + 1 + (maxw - w2) / 2d), y + h1 + 1 + 1 + 1, denom);
			glColor3f(0, 0, 0);
			glFillRect(x + minusw + 1, y + h1 + 1, maxw, 1);
		} else {
			if (small) {
				Display.Render.setFont(PIDisplay.fonts[1]);
			} else {
				Display.Render.setFont(PIDisplay.fonts[0]);
			}
			String r = getTerm().toFancyString();

			if (r.startsWith("-")) {
				if (drawMinus) {

				} else {
					r = r.substring(1);
				}
			}

			glDrawStringLeft(x+1, y, r.toString());
		}
	}

	public int getHeight(boolean drawMinus) {
		boolean beforedrawminus = this.drawMinus;
		this.drawMinus = drawMinus;
		int h = getHeight();
		this.drawMinus = beforedrawminus;
		return h;
	}

	@Override
	public int getHeight() {
		return height;
	}
	
	private int calcHeight() {
		if (getTerm().isBigInteger(false)) {
			int h1 = Utils.getFontHeight(small);
			return h1;
		} else if (getTerm().isRational(false)) {
			small = true;
			int h1 = Utils.getFontHeight(small);
			int h2 = Utils.getFontHeight(small);
			return h1 + 3 + h2;
		} else if (getTerm().toFancyString().contains("/")) {
			small = true;
			int h1 = Utils.getFontHeight(small);
			int h2 = Utils.getFontHeight(small);
			return h1 + 3 + h2;
		} else {
			int h1 = Utils.getFontHeight(small);
			return h1;
		}
	}
	@Override
	public int getWidth() {
		return width;
	}
	
	public int calcWidth() {
		if (getTerm().isBigInteger(false)) {
			String t = toString();
			if (t.startsWith("-")) {
				if (drawMinus) {

				} else {
					t = t.substring(1);
				}
			}
			return getStringWidth(t)+1;
		} else if (getTerm().isRational(false)) {
			Rational r = getTerm().toRational(false);
			boolean minus = false;
			String numerator = r.numer().toString();
			if (numerator.startsWith("-")) {
				minus = true;
				numerator = numerator.substring(1);
			}
			int w1 = getStringWidth(numerator);
			int w2 = getStringWidth(r.denom().toString());
			int maxw;
			if (w1 > w2) {
				maxw = 1 + w1 + 1;
			} else {
				maxw = 1 + w2 + 1;
			}
			if (minus) {
				if (drawMinus) {
					maxw += getStringWidth("-");
				}
			}
			return maxw + 1;
		} else if (getTerm().toFancyString().contains("/")) {
			String r = getTerm().toFancyString();
			String numer = r.substring(0, r.lastIndexOf("/"));
			String denom = r.substring(numer.length() + 1, r.length());
			if (numer.startsWith("(") && numer.endsWith(")")) {
				numer = numer.substring(1, numer.length() - 1);
			}
			boolean minus = false;
			if (numer.startsWith("-")) {
				minus = true;
				numer = numer.substring(1);
			}
			int w1 = getStringWidth(numer.toString());
			int w2 = getStringWidth(denom.toString());
			int maxw;
			if (w1 > w2) {
				maxw = w1 + 1;
			} else {
				maxw = w2 + 1;
			}
			if (minus) {
				if (drawMinus) {
					maxw += getStringWidth("-");
				}
			}
			return maxw + 2;
		} else {
			String r = getTerm().toFancyString();
			if (r.startsWith("-")) {
				if (drawMinus) {

				} else {
					r = r.substring(1);
				}
			}
			return getStringWidth(r.toString())+1;
		}
	}

	public boolean soloIncognitaSemplice() {
		if (this.getTerm().isBigInteger(true)) {
			if (this.getTerm().toBigInteger(true).compareTo(BigInteger.ONE) == 0 && this.getTerm().toNumeroAvanzato().getIncognitey().count() > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getLine() {
		return line;
	}
	
	private int calcLine() {
		if (getTerm().isBigInteger(false)) {
			return Utils.getFontHeight(small) / 2;
		} else if (getTerm().isRational(false)) {
			small = true;
			int h1 = Utils.getFontHeight(small);
			return h1 + 1;
		} else if (getTerm().toFancyString().contains("/")) {
			small = true;
			int h1 = Utils.getFontHeight(small);
			return h1 + 1;
		} else {
			int h1 = Utils.getFontHeight(small);
			return h1 / 2;
		}
	}

	@Override
	public Termine clone() {
		Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}
	
	@Override
	public void setSmall(boolean small) {
		this.small = small;
	}

	/*
	 * @Override
	 * public void draw(int x, int y, Graphics g) {
	 * }
	 * 
	 * @Override
	 * public int getHeight() {
	 * return Utils.getFontHeight();
	 * }
	 * 
	 * @Override
	 * public int getWidth() {
	 * return 6*toString().length()-1;
	 * }
	 */
}

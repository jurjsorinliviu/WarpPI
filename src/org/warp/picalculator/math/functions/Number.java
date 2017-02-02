package org.warp.picalculator.math.functions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.nevec.rjm.BigDecimalMath;
import org.nevec.rjm.BigIntegerMath;
import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.math.Calculator;

import com.rits.cloning.Cloner;

public class Number implements Function {

	private final Calculator root;
	protected BigDecimal term;
	protected int width;
	protected int height;
	protected int line;
	protected boolean small;

	public Number(Calculator root, BigInteger val) {
		this.root = root;
		term = new BigDecimal(val).setScale(Utils.scale, Utils.scaleMode2);
	}

	public Number(Calculator root, BigDecimal val) {
		this.root = root;
		term = val.setScale(Utils.scale, Utils.scaleMode2);
	}

	public Number(Calculator root, String s) throws Error {
		this(root, new BigDecimal(s).setScale(Utils.scale, Utils.scaleMode2));
	}

	public Number(Calculator root, int s) {
		this(root, BigDecimal.valueOf(s).setScale(Utils.scale, Utils.scaleMode2));
	}

	public Number(Calculator root, float s) {
		this(root, BigDecimal.valueOf(s).setScale(Utils.scale, Utils.scaleMode2));
	}

	public Number(Calculator root, double s) {
		this(root, BigDecimal.valueOf(s).setScale(Utils.scale, Utils.scaleMode2));
	}

	public BigDecimal getTerm() {
		return term;
	}

	public void setTerm(BigDecimal val) {
		term = val.setScale(Utils.scale, Utils.scaleMode2);
	}

	@Override
	public void generateGraphics() {
		line = calcLine(); //TODO pp
		height = calcHeight();
		width = calcWidth();
	}

	@Override
	public String getSymbol() {
		return toString();
	}

	public Number add(Number f) {
		final Number ret = new Number(root, getTerm().add(f.getTerm()));
		return ret;
	}

	public Number multiply(Number f) {
		final Number ret = new Number(root, getTerm().multiply(f.getTerm()));
		return ret;
	}

	public Number divide(Number f) throws Error {
		final Number ret = new Number(root, BigDecimalMath.divideRound(getTerm(), f.getTerm()));
		return ret;
	}

	public Number pow(Number f) throws Error {
		Number ret = new Number(root, BigDecimal.ONE);
		if (Utils.isIntegerValue(f.term)) {
			final BigInteger bi = f.term.toBigInteger();
			for (BigInteger i = BigInteger.ZERO; i.compareTo(bi) < 0; i = i.add(BigInteger.ONE)) {
				ret = ret.multiply(new Number(root, getTerm()));
			}
		} else {
			ret.term = BigDecimalMath.pow(term, f.term);
		}
		return ret;
	}

	@Override
	public String toString() {
		final String sWith0 = getTerm().setScale(Utils.displayScale, Utils.scaleMode2).toPlainString();
		final String sExtendedWith0 = getTerm().toPlainString();
		//Remove trailing zeroes. Thanks to Kent, http://stackoverflow.com/questions/14984664/remove-trailing-zero-in-java
		String s = sWith0.indexOf(".") < 0 ? sWith0 : sWith0.replaceAll("0*$", "").replaceAll("\\.$", "");
		final String sExtended = sExtendedWith0.indexOf(".") < 0 ? sExtendedWith0 : sExtendedWith0.replaceAll("0*$", "").replaceAll("\\.$", "");

		if (sExtended.length() > s.length()) {
			s = s + "…";
		}

		if (root.exactMode == false) {
			final String cuttedNumber = s.split("\\.")[0];
			if (cuttedNumber.length() > 8) {
				return cuttedNumber.substring(0, 1) + "," + cuttedNumber.substring(1, 8) + "ℯ℮" + (cuttedNumber.length() - 1);
			}
		}
		return s;
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
		Utils.getFont(small).use(DisplayManager.engine);
		String t = toString();

		if (t.startsWith("-")) {
			if (drawMinus) {

			} else {
				t = t.substring(1);
			}
		}
		if (t.contains("ℯ℮")) {
			final BinaryFont defaultf = Utils.getFont(small);
			final BinaryFont smallf = Utils.getFont(true);
			final String s = t.substring(0, t.indexOf("ℯ℮") + 2);
			final int sw = defaultf.getStringWidth(s);
			DisplayManager.renderer.glDrawStringLeft(x + 1, y + smallf.getCharacterHeight() - 2, s);
			smallf.use(DisplayManager.engine);
			DisplayManager.renderer.glDrawStringLeft(x + 1 + sw - 3, y, t.substring(t.indexOf("ℯ℮") + 2));
		} else {
			DisplayManager.renderer.glDrawStringLeft(x + 1, y, t);
		}
	}

	public int getHeight(boolean drawMinus) {
		final boolean beforedrawminus = this.drawMinus;
		this.drawMinus = drawMinus;
		final int h = getHeight();
		this.drawMinus = beforedrawminus;
		return h;
	}

	@Override
	public int getHeight() {
		return height;
	}

	private int calcHeight() {
		final String t = toString();
		if (t.contains("ℯ℮")) {
			return Utils.getFontHeight(small) - 2 + Utils.getFontHeight(true);
		} else {
			final int h1 = Utils.getFontHeight(small);
			return h1;
		}
	}

	@Override
	public int getWidth() {
		return width;
	}

	public int calcWidth() {
		String t = toString();
		if (t.startsWith("-")) {
			if (drawMinus) {

			} else {
				t = t.substring(1);
			}
		}
		if (t.contains("ℯ℮")) {
			final BinaryFont defaultf = Utils.getFont(small);
			final BinaryFont smallf = Utils.getFont(true);
			final String s = t.substring(0, t.indexOf("ℯ℮") + 2);
			final int sw = defaultf.getStringWidth(s);
			return 1 + sw - 3 + smallf.getStringWidth(t.substring(t.indexOf("ℯ℮") + 2));
		} else {
			return Utils.getFont(small).getStringWidth(t) + 1;
		}
	}

	@Override
	public int getLine() {
		return line;
	}

	private int calcLine() {
		final String t = toString();
		if (t.contains("ℯ℮")) {
			return (Utils.getFontHeight(small) / 2) - 2 + Utils.getFontHeight(true);
		} else {
			return Utils.getFontHeight(small) / 2;
		}
	}

	@Override
	public Number clone() {
		final Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}

	@Override
	public void setSmall(boolean small) {
		this.small = small;
	}

	@Override
	public boolean isSolved() {
		if (root.exactMode) {
			return isInteger();
		} else {
			return true;
		}
	}

	@Override
	public List<Function> solveOneStep() throws Error {
		final List<Function> result = new ArrayList<>();
		if (root.exactMode) {
			Number divisor = new Number(root, BigInteger.TEN.pow(getNumberOfDecimalPlaces()));
			Number numb = new Number(root, term.multiply(divisor.term));
			Division div = new Division(root, numb, divisor);
			result.add(div);
		} else {
			result.add(this);
		}
		return result;
	}
	
	public int getNumberOfDecimalPlaces() {
	    return Math.max(0, term.stripTrailingZeros().scale());
	}
	
	public boolean isInteger() {
		return getNumberOfDecimalPlaces() <= 0;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o != null & term != null) {
			if (o instanceof Number) {
				final BigDecimal nav = ((Number) o).getTerm();
				final boolean na1 = term.compareTo(BigDecimal.ZERO) == 0;
				final boolean na2 = nav.compareTo(BigDecimal.ZERO) == 0;
				if (na1 == na2) {
					if (na1 == true) {
						return true;
					}
				} else {
					return false;
				}
				return nav.compareTo(term) == 0;
			}
		}
		return false;
	}

	@Override
	public Calculator getRoot() {
		return root;
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

	public boolean canBeFactorized() {
		if (Utils.isIntegerValue(getTerm())) {
			return getTerm().toBigIntegerExact().compareTo(BigInteger.valueOf(1)) > 1;
		}
		return false;
	}

	public LinkedList<BigInteger> getFactors() {
		BigInteger n = getTerm().toBigIntegerExact();
		final BigInteger two = BigInteger.valueOf(2);
		final LinkedList<BigInteger> fs = new LinkedList<>();

		if (n.compareTo(two) < 0) {
			throw new IllegalArgumentException("must be greater than one");
		}

		while (n.mod(two).equals(BigInteger.ZERO)) {
			fs.add(two);
			n = n.divide(two);
		}

		if (n.compareTo(BigInteger.ONE) > 0) {
			BigInteger f = BigInteger.valueOf(3);
			while (f.multiply(f).compareTo(n) <= 0) {
				if (n.mod(f).equals(BigInteger.ZERO)) {
					fs.add(f);
					n = n.divide(f);
				} else {
					f = f.add(two);
				}
			}
			fs.add(n);
		}

		return fs;
	}
}

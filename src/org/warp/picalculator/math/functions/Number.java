package org.warp.picalculator.math.functions;

import static org.warp.picalculator.device.graphicengine.Display.Render.glDrawStringLeft;
import static org.warp.picalculator.device.graphicengine.Display.Render.glGetStringWidth;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.nevec.rjm.BigDecimalMath;
import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.graphicengine.Display;
import org.warp.picalculator.device.graphicengine.RAWFont;
import org.warp.picalculator.math.Calculator;

import com.rits.cloning.Cloner;

public class Number implements Function {

	private Function parent;
	protected BigDecimal term;
	protected int width;
	protected int height;
	protected int line;
	protected boolean small;

	public Number(Calculator calc, Function parent, BigInteger val) {
		this.parent = parent;
		term = new BigDecimal(val).setScale(Utils.scale, Utils.scaleMode2);
	}
	
	public Number(Function parent, BigDecimal val) {
		this.parent = parent;
		term = val.setScale(Utils.scale, Utils.scaleMode2);
	}

	public Number(Function parent, String s) throws Error {
		this(parent, new BigInteger(s));
	}

	public Number(Function parent, int s) {
		this(parent, BigDecimal.valueOf(s).setScale(Utils.scale, Utils.scaleMode2));
	}

	public Number(Function parent, float s) {
		this(parent, BigDecimal.valueOf(s).setScale(Utils.scale, Utils.scaleMode2));
	}

	public Number(Function parent, double s) {
		this(parent, BigDecimal.valueOf(s).setScale(Utils.scale, Utils.scaleMode2));
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
		Number ret = new Number(this.parent, getTerm().add(f.getTerm()));
		return ret;
	}

	public Number multiply(Number f) {
		Number ret = new Number(this.parent, getTerm().multiply(f.getTerm()));
		return ret;
	}

	public Number divide(Number f) throws Error {
		Number ret = new Number(this.parent, BigDecimalMath.divideRound(getTerm(), f.getTerm()));
		return ret;
	}
	
	public Number pow(Number f) throws Error {
		Number ret = new Number(this.parent, BigDecimal.ONE);
		if (Utils.isIntegerValue(f.term)) {
			final BigInteger bi = f.term.toBigInteger();
			for (BigInteger i = BigInteger.ZERO; i.compareTo(bi) < 0; i = i.add(BigInteger.ONE)) {
				ret = ret.multiply(new Number(this.parent, getTerm()));
			}
		} else {
			ret.term = BigDecimalMath.pow(term, f.term);
		}
		return ret;
	}

	@Override
	public String toString() {
		String sWith0 = getTerm().setScale(Utils.displayScale, Utils.scaleMode2).toPlainString();
		String sExtendedWith0 = getTerm().toPlainString();
		//Remove trailing zeroes. Thanks to Kent, http://stackoverflow.com/questions/14984664/remove-trailing-zero-in-java
		String s = sWith0.indexOf(".") < 0 ? sWith0 : sWith0.replaceAll("0*$", "").replaceAll("\\.$", "");
		String sExtended = sExtendedWith0.indexOf(".") < 0 ? sExtendedWith0 : sExtendedWith0.replaceAll("0*$", "").replaceAll("\\.$", "");
		
		if (sExtended.length() > s.length()) {
			s = s+"…";
		}
		
		if (Calculator.exactMode == false) {
			String cuttedNumber = s.split("\\.")[0];
			if (cuttedNumber.length() > 8) {
				return cuttedNumber.substring(0, 1)+","+cuttedNumber.substring(1, 8)+"ℯ℮"+(cuttedNumber.length()-1);
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
		Display.Render.glSetFont(Utils.getFont(small));
		String t = toString();

		if (t.startsWith("-")) {
			if (drawMinus) {

			} else {
				t = t.substring(1);
			}
		}
		if (t.contains("ℯ℮")) {
			final RAWFont defaultf = Utils.getFont(small);
			final RAWFont smallf = Utils.getFont(true);
			String s = t.substring(0,  t.indexOf("ℯ℮")+2);
			int sw = glGetStringWidth(defaultf, s);
			glDrawStringLeft(x+1, y+smallf.charH-2, s);
			Display.Render.glSetFont(smallf);
			glDrawStringLeft(x+1+sw-3, y, t.substring(t.indexOf("ℯ℮")+2));
		} else {
			glDrawStringLeft(x+1, y, t);
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
		String t = toString();
		if (t.contains("ℯ℮")) {
			return Utils.getFontHeight(small)-2+Utils.getFontHeight(true);
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
		String t = toString();
		if (t.startsWith("-")) {
			if (drawMinus) {

			} else {
				t = t.substring(1);
			}
		}
		if (t.contains("ℯ℮")) {
			final RAWFont defaultf = Utils.getFont(small);
			final RAWFont smallf = Utils.getFont(true);
			String s = t.substring(0,  t.indexOf("ℯ℮")+2);
			int sw = glGetStringWidth(defaultf, s);
			return 1+sw-3+glGetStringWidth(smallf, t.substring(t.indexOf("ℯ℮")+2));
		} else {
			return glGetStringWidth(Utils.getFont(small), t)+1;
		}
	}
	
	@Override
	public int getLine() {
		return line;
	}
	
	private int calcLine() {
		String t = toString();
		if (t.contains("ℯ℮")) {
			return (Utils.getFontHeight(small) / 2)-2+Utils.getFontHeight(true);
		} else {
			return Utils.getFontHeight(small) / 2;
		}
	}

	@Override
	public Number clone() {
		Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}
	
	@Override
	public void setSmall(boolean small) {
		this.small = small;
	}

	@Override
	public boolean isSolved() {
		return true;
	}

	@Override
	public List<Function> solveOneStep() throws Error {
		List<Function> result = new ArrayList<>();
		result.add(this);
		return result;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o != null & term != null) {
			if (o instanceof Number) {
				BigDecimal nav = ((Number) o).getTerm();
				boolean na1 = term.compareTo(BigDecimal.ZERO) == 0;
				boolean na2 = nav.compareTo(BigDecimal.ZERO) == 0;
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

	public Function setParent(Function value) {
		parent = value;
		return this;
	}

	@Override
	public Function getParent() {
		return parent;
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
	
	public LinkedList<BigInteger> getFactors()
	{
		BigInteger n = getTerm().toBigIntegerExact();
	    BigInteger two = BigInteger.valueOf(2);
	    LinkedList<BigInteger> fs = new LinkedList<BigInteger>();

	    if (n.compareTo(two) < 0)
	    {
	        throw new IllegalArgumentException("must be greater than one");
	    }

	    while (n.mod(two).equals(BigInteger.ZERO))
	    {
	        fs.add(two);
	        n = n.divide(two);
	    }

	    if (n.compareTo(BigInteger.ONE) > 0)
	    {
	        BigInteger f = BigInteger.valueOf(3);
	        while (f.multiply(f).compareTo(n) <= 0)
	        {
	            if (n.mod(f).equals(BigInteger.ZERO))
	            {
	                fs.add(f);
	                n = n.divide(f);
	            }
	            else
	            {
	                f = f.add(two);
	            }
	        }
	        fs.add(n);
	    }

	    return fs;
	}
}

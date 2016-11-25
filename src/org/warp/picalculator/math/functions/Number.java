package org.warp.picalculator.math.functions;

import static org.warp.picalculator.device.graphicengine.Display.Render.glDrawStringLeft;
import static org.warp.picalculator.device.graphicengine.Display.Render.glGetStringWidth;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.graphicengine.Display;

import com.rits.cloning.Cloner;

public class Number implements Function {

	private Function parent;
	protected BigInteger term;
	protected int width;
	protected int height;
	protected int line;
	protected boolean small;
	
	public Number(Function parent, BigInteger val) {
		this.parent = parent;
		term = val;
	}

	public Number(Function parent, String s) throws Error {
		this(parent, new BigInteger(s));
	}

	public Number(Function parent, int s) {
		this(parent, BigInteger.valueOf(s));
	}

	public BigInteger getTerm() {
		return term;
	}

	public void setTerm(BigInteger val) {
		term = val;
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
		Number ret = new Number(this.parent, getTerm().divide(f.getTerm()));
		return ret;
	}

	public Number pow(Number f) throws Error {
		Number ret = new Number(this.parent, BigInteger.ONE);
		for (BigInteger i = BigInteger.ZERO; i.compareTo(f.getTerm()) < 0; i = i.add(BigInteger.ONE)) {
			ret = ret.multiply(new Number(this.parent, getTerm()));
		}
		return ret;
	}

	@Override
	public String toString() {
		return getTerm().toString();
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
		glDrawStringLeft(x+1, y, t);
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
		int h1 = Utils.getFontHeight(small);
		return h1;
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
		return glGetStringWidth(Utils.getFont(small), t)+1;
	}
	
	@Override
	public int getLine() {
		return line;
	}
	
	private int calcLine() {
		return Utils.getFontHeight(small) / 2;
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
				BigInteger nav = ((Number) o).getTerm();
				boolean na1 = term.compareTo(BigInteger.ZERO) == 0;
				boolean na2 = nav.compareTo(BigInteger.ZERO) == 0;
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
}

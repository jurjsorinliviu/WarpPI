package org.warp.picalculator.math.functions;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.graphicengine.cpu.CPUEngine;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.rules.FractionsRule1;
import org.warp.picalculator.math.rules.FractionsRule11;
import org.warp.picalculator.math.rules.FractionsRule12;
import org.warp.picalculator.math.rules.FractionsRule2;
import org.warp.picalculator.math.rules.FractionsRule3;
import org.warp.picalculator.math.rules.UndefinedRule2;

public class Division extends FunctionTwoValues {

	public Division(Calculator root, Function value1, Function value2) {
		super(root, value1, value2);
	}

	@Override
	protected Function NewInstance(Calculator root, Function value1, Function value2) {
		return new Division(root, value1, value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.DIVISION;
	}

	@Override
	protected boolean isSolvable() {
		if (FractionsRule1.compare(this)) {
			return true;
		}
		if (FractionsRule2.compare(this)) {
			return true;
		}
		if (FractionsRule3.compare(this)) {
			return true;
		}
		if (FractionsRule11.compare(this)) {
			return true;
		}
		if (FractionsRule12.compare(this)) {
			return true;
		}
		if (UndefinedRule2.compare(this)) {
			return true;
		}
		if (variable1 instanceof Number && variable2 instanceof Number) {
			if (root.exactMode) {
				try {
					return ((Number)variable1).divide((Number)variable2).isInteger();
				} catch (Error e) {
					return false;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public ArrayList<Function> solve() throws Error {
		ArrayList<Function> result = new ArrayList<>();
		if (FractionsRule1.compare(this)) {
			result = FractionsRule1.execute(this);
		} else if (FractionsRule2.compare(this)) {
			result = FractionsRule2.execute(this);
		} else if (FractionsRule3.compare(this)) {
			result = FractionsRule3.execute(this);
		} else if (FractionsRule11.compare(this)) {
			result = FractionsRule11.execute(this);
		} else if (FractionsRule12.compare(this)) {
			result = FractionsRule12.execute(this);
		} else if (UndefinedRule2.compare(this)) {
			result = UndefinedRule2.execute(this);
		} else if (variable1 instanceof Number && variable2 instanceof Number) {
			result.add(((Number) variable1).divide((Number) variable2));
		}
		return result;
	}

	public boolean hasMinus() {
		final String numerator = variable1.toString();
		if (numerator.startsWith("-")) {
			return true;
		}
		return false;
	}

	public void draw(int x, int y, boolean small, boolean drawMinus) {
		final boolean beforedrawminus = this.drawMinus;
		this.drawMinus = drawMinus;
		draw(x, y);
		this.drawMinus = beforedrawminus;
	}

	private boolean drawMinus = true;

	@Override
	public void generateGraphics() {
		variable1.generateGraphics();

		variable2.generateGraphics();

		width = calcWidth();
		height = calcHeight();
		line = variable1.getHeight() + 1;
	}

	@Override
	public void draw(int x, int y) {
//		glColor3f(255, 127-50+new Random().nextInt(50), 0);
//		glFillRect(x,y,width,height);
//		glColor3f(0, 0, 0);

		final Object var1 = variable1;
		final Object var2 = variable2;
		boolean minus = false;
		int minusw = 0;
		int minush = 0;
		String numerator = ((Function) var1).toString();
		if (numerator.startsWith("-") && ((Function) var1) instanceof Number) {
			minus = true;
			numerator = numerator.substring(1);
		}
		int w1 = 0;
		int h1 = 0;
		Utils.getFont(small).use(DisplayManager.engine);
		if (minus) {
			w1 = Utils.getFont(small).getStringWidth(numerator);
			h1 = Utils.getFont(small).getCharacterHeight();
		} else {
			w1 = ((Function) var1).getWidth();
			h1 = ((Function) var1).getHeight();
		}
		final int w2 = ((Function) var2).getWidth();
		int maxw;
		if (w1 > w2) {
			maxw = 1 + w1;
		} else {
			maxw = 1 + w2;
		}
		if (minus && drawMinus) {
			minusw = Utils.getFont(small).getCharacterWidth() /* Width of minus */ + 1;
			minush = Utils.getFont(small).getCharacterHeight();
			DisplayManager.renderer.glDrawStringLeft(x + 1, y + h1 + 1 + 1 - (minush / 2), "-");
			DisplayManager.renderer.glDrawStringLeft((int) (x + 1 + minusw + 1 + (maxw - w1) / 2d), y, numerator);
		} else {
			((Function) var1).draw((int) (x + 1 + minusw + (maxw - w1) / 2d), y);
		}
		((Function) var2).draw((int) (x + 1 + minusw + (maxw - w2) / 2d), y + h1 + 1 + 1 + 1);
		DisplayManager.renderer.glFillColor(x + 1 + minusw, y + h1 + 1, maxw, 1);
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	protected int calcHeight() {

		boolean minus = false;
		String numerator = variable1.toString();
		if (numerator.startsWith("-") && variable1 instanceof Number) {
			minus = true;
			numerator = numerator.substring(1);
		}
		int h1 = 0;
		if (minus) {
			h1 = Utils.getFontHeight(small);
		} else {
			h1 = variable1.getHeight();
		}
		final int h2 = variable2.getHeight();
		return h1 + 3 + h2;
	}

	@Override
	public int getLine() {
		return line;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	protected int calcWidth() {
		boolean minus = false;
		String numerator = variable1.toString();
		if (numerator.startsWith("-") && variable1 instanceof Number) {
			minus = true;
			numerator = numerator.substring(1);
		}
		int w1 = 0;
		if (minus) {
			w1 = Utils.getFont(small).getStringWidth(numerator);
		} else {
			w1 = variable1.getWidth();
		}
		final int w2 = variable2.getWidth();
		int maxw = 0;
		if (w1 > w2) {
			maxw = w1 + 1;
		} else {
			maxw = w2 + 1;
		}
		if (minus && drawMinus) {
			return 1 + Utils.getFont(small).getCharacterWidth() /* Width of minus */ + 1 + maxw;
		} else {
			return 1 + maxw;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Division) {
			final FunctionTwoValues f = (FunctionTwoValues) o;
			return variable1.equals(f.variable1) && variable2.equals(f.variable2);
		}
		return false;
	}
}
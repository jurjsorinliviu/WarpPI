package org.warp.picalculator.math.functions;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Utils;
import org.warp.picalculator.gui.PIDisplay;
import org.warp.picalculator.gui.graphicengine.cpu.CPUDisplay;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.rules.ExpandRule1;
import org.warp.picalculator.math.rules.NumberRule3;
import org.warp.picalculator.math.rules.NumberRule4;
import org.warp.picalculator.math.rules.NumberRule5;

public class SumSubtraction extends FunctionTwoValues {

	public SumSubtraction(Calculator root, Function value1, Function value2) {
		super(root, value1, value2);
	}
	
	@Override
	protected Function NewInstance(Calculator root, Function value1, Function value2) {
		return new SumSubtraction(root, value1, value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.SUM_SUBTRACTION;
	}

	@Override
	protected boolean isSolvable() {
		if (variable1 instanceof Number & variable2 instanceof Number) {
			return true;
		}
		if (NumberRule3.compare(this)) return true;
		if (ExpandRule1.compare(this)) return true;
		if (NumberRule4.compare(this)) return true;
		if (NumberRule5.compare(this)) return true;
		return false;
	}
	
	@Override
	public ArrayList<Function> solve() throws Error {
		if (variable1 == null || variable2 == null) {
			throw new Error(Errors.SYNTAX_ERROR);
		}
		ArrayList<Function> result = new ArrayList<>();
		if (NumberRule3.compare(this)) {
			result = NumberRule3.execute(this);
		} else if (ExpandRule1.compare(this)) {
			result = ExpandRule1.execute(this);
		} else if (NumberRule4.compare(this)) {
			result = NumberRule4.execute(this);
		} else if (NumberRule5.compare(this)) {
			result = NumberRule5.execute(this);
		} else if (variable1.isSolved() & variable2.isSolved()) {
			result.add(((Number)variable1).add((Number)variable2));
			result.add(((Number)variable1).add(((Number)variable2).multiply(new Number(root, "-1"))));
		}
		return result;
	}

	@Override
	public void generateGraphics() {
		variable1.setSmall(small);
		variable1.generateGraphics();
		
		variable2.setSmall(small);
		variable2.generateGraphics();
		
		width = calcWidth();
		height = calcHeight();
		line = calcLine();
	}
	
	@Override
	public void draw(int x, int y) {
//		glColor3f(127, 127-50+new Random().nextInt(50), 255);
//		glFillRect(x,y,width,height);
//		glColor3f(0, 0, 0);
		
		int ln = getLine();
		int dx = 0;
		variable1.draw(dx + x, ln - variable1.getLine() + y);
		dx += variable1.getWidth();
		PIDisplay.renderer.glSetFont(Utils.getFont(small));
		dx += 1;
		PIDisplay.renderer.glDrawStringLeft(dx + x, ln - Utils.getFontHeight(small) / 2 + y, getSymbol());
		dx += PIDisplay.renderer.glGetStringWidth(Utils.getFont(small), getSymbol());
		variable2.draw(dx + x, ln - variable2.getLine() + y);
	}

	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	protected int calcWidth() {
		int dx = 0;
		dx += variable1.getWidth();
		dx += 1;
		dx += PIDisplay.renderer.glGetStringWidth(Utils.getFont(small), getSymbol());
		return dx += variable2.getWidth();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof SumSubtraction) {
			FunctionTwoValues f = (FunctionTwoValues) o;
			return variable1.equals(f.variable1) && variable2.equals(f.variable2);
		}
		return false;
	}
}

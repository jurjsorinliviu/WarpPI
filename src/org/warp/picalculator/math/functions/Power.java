package org.warp.picalculator.math.functions;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.rules.ExponentRule1;
import org.warp.picalculator.math.rules.ExponentRule2;
import org.warp.picalculator.math.rules.ExponentRule3;
import org.warp.picalculator.math.rules.ExponentRule4;
import org.warp.picalculator.math.rules.FractionsRule4;
import org.warp.picalculator.math.rules.FractionsRule5;
import org.warp.picalculator.math.rules.UndefinedRule1;

public class Power extends FunctionTwoValues {
	
	public Power(Calculator root, Function value1, Function value2) {
		super(root, value1, value2);
	}
	
	@Override
	protected Function NewInstance(Calculator root, Function value1, Function value2) {
		return new Power(root, value1, value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.POWER;
	}

	@Override
	protected boolean isSolvable() {
		if (variable1 instanceof Number & variable2 instanceof Number) {
			return true;
		}
		if (UndefinedRule1.compare(this)) return true;
		if (ExponentRule1.compare(this)) return true;
		if (ExponentRule2.compare(this)) return true;
		if (ExponentRule3.compare(this)) return true;
		if (ExponentRule4.compare(this)) return true;
		if (FractionsRule4.compare(this)) return true;
		if (FractionsRule5.compare(this)) return true;
		return false;
	}
	
	@Override
	public void generateGraphics() {
		variable1.setSmall(small);
		variable1.generateGraphics();
		
		variable2.setSmall(true);
		variable2.generateGraphics();
		
		height = variable1.getHeight() + variable2.getHeight() - 4;
		line = variable2.getHeight() - 4 + variable1.getLine();
		width = getVariable1().getWidth() + getVariable2().getWidth()+1;
	}

	@Override
	public ArrayList<Function> solve() throws Error {
		ArrayList<Function> result = new ArrayList<>();
		if (UndefinedRule1.compare(this)) {
			result.addAll(UndefinedRule1.execute(this));
		} else if (ExponentRule1.compare(this)) {
			result.addAll(ExponentRule1.execute(this));
		} else if (ExponentRule2.compare(this)) {
			result.addAll(ExponentRule2.execute(this));
		} else if (ExponentRule3.compare(this)) {
			result.addAll(ExponentRule3.execute(this));
		} else if (ExponentRule4.compare(this)) {
			result.addAll(ExponentRule4.execute(this));
		} else if (FractionsRule4.compare(this)) {
			result.addAll(FractionsRule4.execute(this));
		} else if (FractionsRule5.compare(this)) {
			result.addAll(FractionsRule5.execute(this));
		} else if (variable1 instanceof Number & variable2 instanceof Number) {
			result.add(((Number)variable1).pow((Number)variable2));
		}
		return result;
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
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Power) {
			FunctionTwoValues f = (FunctionTwoValues) o;
			return variable1.equals(f.variable1) && variable2.equals(f.variable2);
		}
		return false;
	}
}

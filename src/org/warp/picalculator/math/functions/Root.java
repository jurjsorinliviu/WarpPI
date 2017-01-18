package org.warp.picalculator.math.functions;

import static org.warp.picalculator.device.graphicengine.Display.Render.glDrawLine;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.MathematicalSymbols;

public class Root extends FunctionTwoValues {

	public Root(Calculator root, Function value1, Function value2) {
		super(root, value1, value2);
	}
	
	@Override
	protected Function NewInstance(Calculator root, Function value1, Function value2) {
		return new Root(root, value1, value2);
	}
	
	@Override
	public String getSymbol() {
		return MathematicalSymbols.NTH_ROOT;
	}

	@Override
	public void generateGraphics() {
		variable1.setSmall(true);
		variable1.generateGraphics();
		
		variable2.setSmall(small);
		variable2.generateGraphics();
		
		width = 1 + variable1.getWidth() + 2 + variable2.getWidth() + 2;
		height = variable1.getHeight() + variable2.getHeight() - 2;
		line = variable1.getHeight() + variable2.getLine() - 2;
	}

	@Override
	protected boolean isSolvable() {
		if (variable1 instanceof Number & variable2 instanceof Number) {
			if (root.exactMode == false) {
				return true;
			}
			try {
				Number exponent = new Number(root, BigDecimal.ONE);
				exponent = exponent.divide((Number) variable1);
				Number resultVal = ((Number)variable2).pow(exponent);
				Number originalVariable = resultVal.pow(new Number(root, 2));
				if (originalVariable.equals(variable2)) {
					return true;
				} 
			} catch (Exception | Error ex) {
				ex.printStackTrace();
			}
		}
		if (variable1 instanceof Number && ((Number)variable1).equals(new Number(root, 2))) {
			return true;
		}
		return false;
	}
	
	@Override
	public ArrayList<Function> solve() throws Error {
		ArrayList<Function> result = new ArrayList<>();
		if (root.exactMode) {
			if (variable1 instanceof Number && ((Number)variable1).equals(new Number(root, 2))) {
				result.add(new RootSquare(root, variable2));
			} else {
				Number exponent = new Number(root, BigInteger.ONE);
				exponent = exponent.divide((Number) variable1);
				result.add(((Number)variable2).pow(exponent));
			}
		} else {
			Number exp = (Number) variable1;
			Number numb = (Number) variable2;
			
			result.add(numb.pow(new Number(root, 1).divide(exp)));
		}
		return result;
	}

	@Override
	public void draw(int x, int y) {
//		glColor3f(0, 255, 0);
//		glFillRect(x,y,width,height);
//		glColor3f(0, 0, 0);
		
		int w1 = getVariable2().getWidth();
		int h1 = getVariable2().getHeight();
		int w2 = getVariable1().getWidth();
		int h2 = getVariable1().getHeight();
		int height = getHeight();
		int hh = (int) Math.ceil((double) h1 / 2);

		getVariable1().draw(x + 1, y);
		getVariable2().draw(x + 1 + w2 + 2, y + h2 - 2);

		glDrawLine(x + 1 + w2 - 2, y + height - 3, x + 1 + w2, y + height);
		glDrawLine(x + 1 + w2, y + height - 1 - hh, x + 1 + w2, y + height - 1);
		glDrawLine(x + 1 + w2 + 1, y + height - 2 - h1, x + 1 + w2 + 1, y + height - 1 - hh - 1);
		glDrawLine(x + 1 + w2 + 1, y + height - h1 - 2, x + 1 + w2 + 2 + w1 + 1, y + height - h1 - 2);
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
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Root) {
			FunctionTwoValues f = (FunctionTwoValues) o;
			return variable1.equals(f.variable1) && variable2.equals(f.variable2);
		}
		return false;
	}
}

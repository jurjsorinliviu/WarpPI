package org.warp.picalculator.math.functions;

import java.util.ArrayList;
import java.util.List;

import org.nevec.rjm.Rational;
import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.MathematicalSymbols;

public class RootSquare extends AnteriorFunction {

	public RootSquare(Function value) {
		super(value);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.SQUARE_ROOT;
	}
	
	@Override
	public void generateGraphics() {
		variable.setSmall(small);
		variable.generateGraphics();
		
		height = getVariable().getHeight() + 2;
		width = 1 + 4 + getVariable().getWidth() + 1;
		line = getVariable().getLine() + 2;
	}
	
	@Override
	public List<Function> solveOneStep() throws Error {
		if (variable == null) {
			throw new Error(Errors.SYNTAX_ERROR);
		}
		ArrayList<Function> result = new ArrayList<>();
		if (stepsCount == 1) {
			try {
				Number var = (Number) getVariable();
				result.add(var.pow(new Number(new Rational(1, 2))));
			} catch(NullPointerException ex) {
				throw new Error(Errors.ERROR);
			} catch(NumberFormatException ex) {
				throw new Error(Errors.SYNTAX_ERROR);
			} catch(ArithmeticException ex) {
				throw new Error(Errors.NUMBER_TOO_SMALL);
			}
		} else {
			List<Function> l1 = new ArrayList<Function>();
			if (variable.getStepsCount() >= stepsCount - 1) {
				l1.addAll(variable.solveOneStep());
			} else {
				l1.add(variable);
			}
			
			for (Function f : l1) {
				result.add(new RootSquare((Function)f));
			}
		}
		stepsCount=-1;
		return result;
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

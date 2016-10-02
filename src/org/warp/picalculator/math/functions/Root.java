package org.warp.picalculator.math.functions;

import static org.warp.picalculator.device.graphicengine.Display.Render.glDrawLine;

import java.util.ArrayList;
import java.util.List;

import org.nevec.rjm.NumeroAvanzatoVec;
import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.MathematicalSymbols;

public class Root extends FunctionTwoValues {

	public Root(Function value1, Function value2) {
		super(value1, value2);
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
	protected boolean isSolvable() throws Error {
		if (variable1 instanceof Number & variable2 instanceof Number) {
			if ((((Number)variable2).pow(new Number(NumeroAvanzatoVec.ONE).divide((Number) variable1)).getTerm().isBigInteger(true))) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<Function> solveOneStep() throws Error {
		ArrayList<Function> result = new ArrayList<>();
		if (variable1.isSolved() & variable2.isSolved()) {
			Number exponent = new Number(NumeroAvanzatoVec.ONE);
			exponent = exponent.divide((Number) variable1);
			result.add(((Number)variable2).pow(exponent));
		} else {
			List<Function> l1 = new ArrayList<Function>();
			List<Function> l2 = new ArrayList<Function>();
			if (variable1.isSolved()) {
				l1.add(variable1);
			} else {
				l1.addAll(variable1.solveOneStep());
			}
			if (variable2.isSolved()) {
				l2.add(variable2);
			} else {
				l2.addAll(variable2.solveOneStep());
			}

			Function[][] results = Utils.joinFunctionsResults(l1, l2);
			
			for (Function[] f : results) {
				result.add(new Root((Function)f[0], (Function)f[1]));
			}
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
}

package org.warp.picalculator.math.functions;

import java.util.ArrayList;
import java.util.List;

import org.nevec.rjm.NumeroAvanzatoVec;
import org.nevec.rjm.Rational;
import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.graphicengine.Display;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.rules.NumberRule1;
import org.warp.picalculator.math.rules.ExpandRule1;
import org.warp.picalculator.math.rules.ExpandRule5;

public class Negative extends AnteriorFunction {

	public Negative(Function parent, Function value) {
		super(parent, value);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.MINUS;
	}
	
	@Override
	public void generateGraphics() {
		variable.setSmall(small);
		variable.generateGraphics();
		
		height = getVariable().getHeight();
		width = Display.Render.glGetStringWidth("-") + getVariable().getWidth();
		line = getVariable().getLine();
	}

	@Override
	protected boolean isSolvable() throws Error {
		if (variable instanceof Number) return true;
		if (ExpandRule1.compare(this)) return true;
		if (ExpandRule5.compare(this)) return true;
		return false;
	}
	
	@Override
	public List<Function> solveOneStep() throws Error {
		if (variable == null) {
			throw new Error(Errors.SYNTAX_ERROR);
		}
		ArrayList<Function> result = new ArrayList<>();
		if (ExpandRule1.compare(this)) {
			result = ExpandRule1.execute(this);
		} else if (ExpandRule5.compare(this)) {
			result = ExpandRule5.execute(this);
		} else if (variable.isSolved()) {
			try {
				Number var = (Number) getVariable();
				result.add(var.multiply(new Number(null, "-1")));
			} catch(NullPointerException ex) {
				throw new Error(Errors.ERROR);
			} catch(NumberFormatException ex) {
				throw new Error(Errors.SYNTAX_ERROR);
			} catch(ArithmeticException ex) {
				throw new Error(Errors.NUMBER_TOO_SMALL);
			}
		} else {
			List<Function> l1 = new ArrayList<Function>();
			if (variable.isSolved()) {
				l1.add(variable);
			} else {
				l1.addAll(variable.solveOneStep());
			}
			
			for (Function f : l1) {
				result.add(new Negative(this.parent, (Function)f));
			}
		}
		return result;
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

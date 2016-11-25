package org.warp.picalculator.math.functions;

import static org.warp.picalculator.device.graphicengine.Display.Render.glGetStringWidth;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.rules.NumberRule3;
import org.warp.picalculator.math.rules.NumberRule5;
import org.warp.picalculator.math.rules.NumberRule7;
import org.warp.picalculator.math.rules.SyntaxRule1;
import org.warp.picalculator.math.rules.SyntaxRule2;
import org.warp.picalculator.math.rules.VariableRule1;
import org.warp.picalculator.math.rules.VariableRule2;
import org.warp.picalculator.math.rules.VariableRule3;
import org.warp.picalculator.math.rules.methods.SumMethod1;

public class Sum extends FunctionTwoValues {

	public Sum(Function parent, Function value1, Function value2) {
		super(parent, value1, value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.SUM;
	}
	
	@Override
	protected boolean isSolvable() {
		if (variable1 instanceof Number & variable2 instanceof Number) {
			return true;
		}
		if (SyntaxRule2.compare(this)) return true;
		if (VariableRule1.compare(this)) return true;
		if (VariableRule2.compare(this)) return true;
		if (VariableRule3.compare(this)) return true;
		if (NumberRule3.compare(this)) return true;
		if (NumberRule5.compare(this)) return true;
		if (NumberRule7.compare(this)) return true;
		if (SumMethod1.compare(this)) return true;
		return false;
	}

	@Override
	public List<Function> solveOneStep() throws Error {
		if (variable1 == null || variable2 == null) {
			throw new Error(Errors.SYNTAX_ERROR);
		}
		ArrayList<Function> result = new ArrayList<>();
		if (SyntaxRule2.compare(this)) {
			result = SyntaxRule2.execute(this);
		} else if (VariableRule1.compare(this)) {
			result = VariableRule1.execute(this);
		} else if (VariableRule2.compare(this)) {
			result = VariableRule2.execute(this);
		} else if (VariableRule3.compare(this)) {
			result = VariableRule3.execute(this);
		} else if (NumberRule3.compare(this)) {
			result = NumberRule3.execute(this);
		} else if (NumberRule5.compare(this)) {
			result = NumberRule5.execute(this);
		} else if (NumberRule7.compare(this)) {
			result = NumberRule7.execute(this);
		} else if (SumMethod1.compare(this)) {
			result = SumMethod1.execute(this);
		} else if (variable1.isSolved() & variable2.isSolved()) {
			if ((parent == null || parent.getParent() == null)) {
				if (((Number)variable1).term.compareTo(new BigInteger("2")) == 0 && ((Number)variable2).term.compareTo(new BigInteger("2")) == 0) {
					result.add(new Joke(Joke.FISH));
					return result;
				} else if (((Number)variable1).term.compareTo(new BigInteger("20")) == 0 && ((Number)variable2).term.compareTo(new BigInteger("20")) == 0) {
					result.add(new Joke(Joke.TORNADO));
					return result;
				} else if (((Number)variable1).term.compareTo(new BigInteger("29")) == 0 && ((Number)variable2).term.compareTo(new BigInteger("29")) == 0) {
					result.add(new Joke(Joke.SHARKNADO));
					return result;
				}
			}
			result.add(((Number)variable1).add((Number)variable2));
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
				result.add(new Sum(this.parent, (Function)f[0], (Function)f[1]));
			}
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
	public int getWidth() {
		return width;
	}
	
	@Override
	protected int calcWidth() {
		int dx = 0;
		dx += variable1.getWidth();
		dx += 1;
		dx += glGetStringWidth(Utils.getFont(small), getSymbol());
		return dx += variable2.getWidth();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Sum) {
			FunctionTwoValues f = (FunctionTwoValues) o;
			if (variable1.equals(f.variable1) && variable2.equals(f.variable2)) {
				return true;
			} else if (variable1.equals(f.variable2) && variable2.equals(f.variable1)) {
				return true;
			}
		}
		return false;
	}
}

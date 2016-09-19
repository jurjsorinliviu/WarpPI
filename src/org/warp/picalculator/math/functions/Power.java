package org.warp.picalculator.math.functions;

import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.math.MathematicalSymbols;

public class Power extends FunctionTwoValues {

	public Power(Function value1, Function value2) {
		super(value1, value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.POWER;
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
	public List<Function> solveOneStep() throws Error {
		if (variable1 == null || variable2 == null) {
			throw new Error(Errors.SYNTAX_ERROR);
		}
		ArrayList<Function> result = new ArrayList<>();
		if (stepsCount == 1) {
			result.add((Function) ((Number)variable1).pow((Number)variable2));
		} else {
			List<Function> l1 = new ArrayList<Function>();
			List<Function> l2 = new ArrayList<Function>();
			if (variable1.getStepsCount() >= stepsCount - 1) {
				l1.addAll(variable1.solveOneStep());
			} else {
				l1.add(variable1);
			}
			if (variable2.getStepsCount() >= stepsCount - 1) {
				l2.addAll(variable2.solveOneStep());
			} else {
				l2.add(variable2);
			}
			
			int size1 = l1.size();
			int size2 = l2.size();
			int cur1 = 0;
			int cur2 = 0;
			int total = l1.size()*l2.size();
			Function[][] results = new Function[total][2];
			for (int i = 0; i < total; i++) {
				results[i] = new Function[]{l1.get(cur1), l2.get(cur2)};
				if (cur1 < cur2 && cur2 % size1 == 0) {
					cur2+=1;
				} else if (cur2 < cur1 && cur1 % size2 == 0) {
					cur1+=1;
				}
				if (cur1 >= size1) cur1 = 0;
				if (cur2 >= size1) cur2 = 0;
			}
			for (Function[] f : results) {
				result.add(new Power((Function)f[0], (Function)f[1]));
			}
		}
		stepsCount=-1;
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
}

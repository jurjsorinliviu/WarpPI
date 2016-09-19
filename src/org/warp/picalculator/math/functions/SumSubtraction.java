package org.warp.picalculator.math.functions;

import static org.warp.picalculator.device.graphicengine.Display.Render.getStringWidth;
import static org.warp.picalculator.device.graphicengine.Display.Render.glDrawStringLeft;

import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.PIDisplay;
import org.warp.picalculator.device.graphicengine.Display;
import org.warp.picalculator.math.MathematicalSymbols;

public class SumSubtraction extends FunctionTwoValues {

	public SumSubtraction(Function value1, Function value2) {
		super(value1, value2);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.SUM_SUBTRACTION;
	}

	@Override
	public List<Function> solveOneStep() throws Error {
		if (variable1 == null || variable2 == null) {
			throw new Error(Errors.SYNTAX_ERROR);
		}
		ArrayList<Function> result = new ArrayList<>();
		if (stepsCount == 1) {
			result.add(((Number)variable1).add((Number)variable2));
			result.add(((Number)variable1).add(((Number)variable2).multiply(new Number("-1"))));
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
				result.add(new SumSubtraction(f[0], f[1]));
			}
		}
		stepsCount=-1;
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
		if (small) {
			Display.Render.setFont(PIDisplay.fonts[1]);
		} else {
			Display.Render.setFont(PIDisplay.fonts[0]);
		}
		dx += 1;
		glDrawStringLeft(dx + x, ln - Utils.getFontHeight(small) / 2 + y, getSymbol());
		dx += getStringWidth(getSymbol());
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
		dx += getStringWidth(getSymbol());
		return dx += variable2.getWidth();
	}
}

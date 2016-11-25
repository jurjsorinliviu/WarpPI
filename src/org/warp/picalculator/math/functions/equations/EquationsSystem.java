package org.warp.picalculator.math.functions.equations;

import static org.warp.picalculator.device.graphicengine.Display.Render.glDrawLine;

import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.functions.Expression;
import org.warp.picalculator.math.functions.Function;
import org.warp.picalculator.math.functions.FunctionMultipleValues;
import org.warp.picalculator.math.functions.Number;

public class EquationsSystem extends FunctionMultipleValues {
	static final int spacing = 2;
	
	public EquationsSystem(Function parent) {
		super(parent);
	}
	
	public EquationsSystem(Function parent, Function value) {
		super(parent, new Function[]{value});
	}
	
	public EquationsSystem(Function parent, Function[] value) {
		super(parent, value);
	}

	@Override
	public String getSymbol() {
		return null;
	}

	@Override
	protected boolean isSolvable() {
		if (functions.length >= 1) {
			return true;
		}
		return false;
	}
	
	@Override
	public List<Function> solveOneStep() throws Error {
		List<Function> ret = new ArrayList<>();
		if (functions.length == 1) {
			if (functions[0].isSolved()) {
				ret.add(functions[0]);
				return ret;
			} else {
				List<Function> l = functions[0].solveOneStep();
				for (Function f : l) {
					if (f instanceof Number) {
						ret.add(f);
					} else {
						ret.add(new Expression(this.parent, new Function[]{(Function) f}));
					}
				}
				return ret;
			}
		} else {
			for (Function f : functions) {
				if (f.isSolved() == false) {
					List<Function> partial = f.solveOneStep();
					for (Function fnc : partial) {
						ret.add(new Expression(this.parent, new Function[]{(Function) fnc}));
					}
				}
			}
			return ret;
		}
	}

	@Override
	public void generateGraphics() {
		for (Function f : functions) {
			f.setSmall(false);
			f.generateGraphics();
		}
		
		width = 0;
		for (Function f : functions) {
			if (f.getWidth() > width) {
				width = f.getWidth();
			}
		}
		width += 5;
		
		height = 3;
		for (Function f : functions) {
			height += f.getHeight()+spacing;
		}
		height = height - spacing + 2;
		
		line = height/2;
	}
	
	@Override
	public void draw(int x, int y) {

		final int h = this.getHeight() - 1;
		final int marginTop = 3;
		final int marginBottom = (h - 3 - 2) / 2 + marginTop;
		final int spazioSopra = h - marginBottom;
		int dy = marginTop;
		for (Function f : functions) {
			f.draw(x + 5, y + dy);
			dy+=f.getHeight()+spacing;
		}
		
		
		glDrawLine(x + 2, y + 0, x + 3, y + 0);
		glDrawLine(x + 1, y + 1, x + 1, y + marginBottom / 2);
		glDrawLine(x + 2, y + marginBottom / 2 + 1, x + 2, y + marginBottom - 1);
		glDrawLine(x + 0, y + marginBottom, x + 1, y + marginBottom);
		glDrawLine(x + 2, y + marginBottom + 1, x + 2, y + marginBottom + spazioSopra / 2 - 1);
		glDrawLine(x + 1, y + marginBottom + spazioSopra / 2, x + 1, y + h - 1);
		glDrawLine(x + 2, y + h, x + 3, y + h);
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

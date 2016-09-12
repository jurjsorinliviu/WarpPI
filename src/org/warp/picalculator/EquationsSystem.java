package org.warp.picalculator;

import static org.warp.engine.Display.Render.glDrawLine;

public class EquationsSystem extends FunctionMultipleValues {
	static final int spacing = 2;
	
	public EquationsSystem() {
		super();
	}
	
	public EquationsSystem(Function value) {
		super(new Function[]{value});
	}
	
	public EquationsSystem(Function[] value) {
		super(value);
	}

	@Override
	public String getSymbol() {
		return null;
	}

	@Override
	public Function solve() throws NumberFormatException, Error {
		// TODO implementare il calcolo dei sistemi
		return variables[0].solve();
	}

	@Override
	public void generateGraphics() {
		for (Function f : variables) {
			f.setSmall(false);
			f.generateGraphics();
		}
		
		width = 0;
		for (Function f : variables) {
			if (f.getWidth() > width) {
				width = f.getWidth();
			}
		}
		width += 5;
		
		height = 3;
		for (Function f : variables) {
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
		for (Function f : variables) {
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

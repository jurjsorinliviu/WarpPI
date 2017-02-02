package org.warp.picalculator.math.functions.equations;

import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.functions.AnteriorFunction;
import org.warp.picalculator.math.functions.Function;

public class EquationsSystemPart extends AnteriorFunction {

	public EquationsSystemPart(Calculator root, Equation equazione) {
		super(root, equazione);
	}

	@Override
	protected Function NewInstance(Calculator root, Function value) {
		return new EquationsSystemPart(root, (Equation) value);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.SYSTEM;
	}

	@Override
	public void generateGraphics() {
		variable.setSmall(false);
		variable.generateGraphics();

		width = 5 + getVariable().getWidth();
		height = 3 + getVariable().getHeight() + 2;
		line = 3 + getVariable().getLine();
	}

	@Override
	public void draw(int x, int y) {
		final int h = getHeight() - 1;
		final int paddingTop = 3;
		final int spazioSotto = (h - 3 - 2) / 2 + paddingTop;
		final int spazioSopra = h - spazioSotto;
		variable.draw(x + 5, y + paddingTop);
		DisplayManager.renderer.glDrawLine(x + 2, y + 0, x + 3, y + 0);
		DisplayManager.renderer.glDrawLine(x + 1, y + 1, x + 1, y + spazioSotto / 2);
		DisplayManager.renderer.glDrawLine(x + 2, y + spazioSotto / 2 + 1, x + 2, y + spazioSotto - 1);
		DisplayManager.renderer.glDrawLine(x + 0, y + spazioSotto, x + 1, y + spazioSotto);
		DisplayManager.renderer.glDrawLine(x + 2, y + spazioSotto + 1, x + 2, y + spazioSotto + spazioSopra / 2 - 1);
		DisplayManager.renderer.glDrawLine(x + 1, y + spazioSotto + spazioSopra / 2, x + 1, y + h - 1);
		DisplayManager.renderer.glDrawLine(x + 2, y + h, x + 3, y + h);
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
	protected ArrayList<Function> solve() throws Error {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isSolvable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}
}

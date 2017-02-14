package org.warp.picalculator.math.functions.equations;

import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathematicalSymbols;

public class EquationsSystemPart extends FunctionSingle {

	public EquationsSystemPart(MathContext root, Equation equazione) {
		super(root, equazione);
	}

	@Override
	protected Function NewInstance(MathContext root, Function value) {
		return new EquationsSystemPart(root, (Equation) value);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.SYSTEM;
	}

	@Override
	public void recomputeDimensions() {
		variable.setSmall(false);
		variable.recomputeDimensions();

		width = 5 + getParameter().getWidth();
		height = 3 + getParameter().getHeight() + 2;
		line = 3 + getParameter().getLine();
	}

	@Override
	public int draw(int x, int y, boolean small, int caretPos) {
		final int h = getHeight() - 1;
		final int paddingTop = 3;
		final int spazioSotto = (h - 3 - 2) / 2 + paddingTop;
		final int spazioSopra = h - spazioSotto;
		variable.draw(x + 5, y + paddingTop, null, null);
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

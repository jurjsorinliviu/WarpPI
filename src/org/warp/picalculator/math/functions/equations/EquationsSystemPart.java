package org.warp.picalculator.math.functions.equations;

import static org.warp.picalculator.device.graphicengine.Display.Render.glColor3f;
import static org.warp.picalculator.device.graphicengine.Display.Render.glDrawLine;

import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.functions.AnteriorFunction;
import org.warp.picalculator.math.functions.Function;

public class EquationsSystemPart extends AnteriorFunction {

	public EquationsSystemPart(Function parent, Equation equazione) {
		super(parent, equazione);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.SYSTEM;
	}

	@Override
	protected boolean isSolvable() throws Error {
		return variable.isSolved()==false;
	}

	@Override
	public List<Function> solveOneStep() throws NumberFormatException, Error {
		// TODO implementare il calcolo dei sistemi
		if (variable.isSolved()) {
			List<Function> ret = new ArrayList<>();
			ret.add(variable);
			return ret;
		}
		return variable.solveOneStep();
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
		final int h = this.getHeight() - 1;
		final int paddingTop = 3;
		final int spazioSotto = (h - 3 - 2) / 2 + paddingTop;
		final int spazioSopra = h - spazioSotto;
		variable.draw(x + 5, y + paddingTop);
		glColor3f(0, 0, 0);
		glDrawLine(x + 2, y + 0, x + 3, y + 0);
		glDrawLine(x + 1, y + 1, x + 1, y + spazioSotto / 2);
		glDrawLine(x + 2, y + spazioSotto / 2 + 1, x + 2, y + spazioSotto - 1);
		glDrawLine(x + 0, y + spazioSotto, x + 1, y + spazioSotto);
		glDrawLine(x + 2, y + spazioSotto + 1, x + 2, y + spazioSotto + spazioSopra / 2 - 1);
		glDrawLine(x + 1, y + spazioSotto + spazioSopra / 2, x + 1, y + h - 1);
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

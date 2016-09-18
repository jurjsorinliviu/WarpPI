package org.warp.picalculator;

import static org.warp.engine.Display.Render.getStringWidth;
import static org.warp.engine.Display.Render.glDrawStringLeft;

import java.util.List;

import org.nevec.rjm.NumeroAvanzatoVec;
import org.warp.device.PIDisplay;
import org.warp.engine.Display;

import com.rits.cloning.Cloner;

public abstract class AnteriorFunctionBase extends FunctionBase {
	public AnteriorFunctionBase(FunctionBase value) {
		setVariable(value);
	}

	protected FunctionBase variable = new Number(NumeroAvanzatoVec.ZERO);
	protected int width;
	protected int height;
	protected int line;
	protected boolean small;
	
	public FunctionBase getVariable() {
		return variable;
	}

	public void setVariable(FunctionBase value) {
		variable = value;
	}

	@Override
	public abstract String getSymbol();

	@Override
	public abstract List<Function> solveOneStep() throws Error;

	protected int stepsCount = -1;
	@Override
	public int getStepsCount() {
		if (stepsCount == -1) {
			stepsCount = variable.getStepsCount()+1;
		}
		return stepsCount;
	}
	
	@Override
	public void generateGraphics() {
		variable.setSmall(small);
		variable.generateGraphics();
		
		width = getStringWidth(getSymbol()) + 1 + getVariable().getWidth();
		height = variable.getHeight();
		line = variable.getLine();
	}

	@Override
	public void draw(int x, int y) {
		float h1 = getVariable().getHeight();
		int wsegno = getStringWidth(getSymbol());
		float hsegno = Utils.getFontHeight(small);
		float maxh = getHeight();
		if (small) {
			Display.Render.setFont(PIDisplay.fonts[1]);
		} else {
			Display.Render.setFont(PIDisplay.fonts[0]);
		}
		
		glDrawStringLeft(x, (int) Math.floor(y + (maxh - hsegno) / 2), getSymbol());
		getVariable().draw(x + wsegno + 1, (int) Math.floor(y + (maxh - h1) / 2));
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
	public String toString() {
//		try {
//			return solve().toString();
		return "TODO: fare una nuova alternativa a solve().toString()";
//		} catch (Error e) {
//			return e.id.toString();
//		}
	}

	@Override
	public AnteriorFunctionBase clone() {
		Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}
	
	@Override
	public void setSmall(boolean small) {
		this.small = small;
	}	
}

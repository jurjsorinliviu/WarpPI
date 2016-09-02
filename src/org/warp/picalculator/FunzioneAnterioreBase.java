package org.warp.picalculator;

import static org.warp.engine.Display.Render.getStringWidth;
import static org.warp.engine.Display.Render.glDrawStringLeft;

import org.nevec.rjm.NumeroAvanzatoVec;
import org.warp.device.PIDisplay;
import org.warp.engine.Display;

import com.rits.cloning.Cloner;

public abstract class FunzioneAnterioreBase extends FunzioneBase {
	public FunzioneAnterioreBase(FunzioneBase value) {
		setVariable(value);
	}

	protected FunzioneBase variable = new Termine(NumeroAvanzatoVec.ZERO);
	protected int width;
	protected int height;
	protected int line;
	protected boolean small;
	
	public FunzioneBase getVariable() {
		return variable;
	}

	public void setVariable(FunzioneBase value) {
		variable = value;
	}

	@Override
	public abstract String simbolo();

	@Override
	public abstract Termine calcola() throws Errore;

	@Override
	public void calcolaGrafica() {
		variable.setSmall(small);
		variable.calcolaGrafica();
		
		width = getStringWidth(simbolo()) + 1 + getVariable().getWidth();
		height = variable.getHeight();
		line = variable.getLine();
	}

	@Override
	public void draw(int x, int y) {
		float h1 = getVariable().getHeight();
		int wsegno = getStringWidth(simbolo());
		float hsegno = Utils.getFontHeight(small);
		float maxh = getHeight();
		if (small) {
			Display.Render.setFont(PIDisplay.fonts[1]);
		} else {
			Display.Render.setFont(PIDisplay.fonts[0]);
		}
		
		glDrawStringLeft(x, (int) Math.floor(y + (maxh - hsegno) / 2), simbolo());
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
		try {
			return calcola().toString();
		} catch (Errore e) {
			return e.id.toString();
		}
	}

	@Override
	public FunzioneAnterioreBase clone() {
		Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}
	
	@Override
	public void setSmall(boolean small) {
		this.small = small;
	}	
}

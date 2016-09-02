package org.warp.picalculator;

import static org.warp.engine.Display.Render.getStringWidth;
import static org.warp.engine.Display.Render.glDrawStringLeft;

import org.warp.device.PIDisplay;
import org.warp.engine.Display;

public class Somma extends FunzioneDueValoriBase {

	public Somma(FunzioneBase value1, FunzioneBase value2) {
		super(value1, value2);
	}

	@Override
	public String simbolo() {
		return Simboli.SUM;
	}

	@Override
	public Termine calcola() throws Errore {
		Termine val1 = getVariable1().calcola();
		Termine val2 = getVariable2().calcola();
		Termine result = val1.add(val2);
		return result;
	}

	@Override
	public void calcolaGrafica() {
		variable1.setSmall(small);
		variable1.calcolaGrafica();
		
		variable2.setSmall(small);
		variable2.calcolaGrafica();
		
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
		glDrawStringLeft(dx + x, ln - Utils.getFontHeight(small) / 2 + y, simbolo());
		dx += getStringWidth(simbolo());
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
		dx += getStringWidth(simbolo());
		return dx += variable2.getWidth();
	}
}

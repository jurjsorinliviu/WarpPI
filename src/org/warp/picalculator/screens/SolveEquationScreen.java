package org.warp.picalculator.screens;

import static org.warp.engine.Display.Render.glDrawStringCenter;

import org.warp.device.Keyboard.Key;
import org.warp.engine.Display;
import org.warp.engine.Screen;
import org.warp.picalculator.Calculator;
import org.warp.picalculator.Errore;
import org.warp.picalculator.Main;

public class SolveEquationScreen extends Screen {

	@SuppressWarnings("unused")
	private EquationScreen es;

	public SolveEquationScreen(EquationScreen es) {
		super();
		canBeInHistory = false;
		
		this.es = es;
	}
	
	@Override
	public void created() throws InterruptedException {
	}

	@Override
	public void init() throws InterruptedException {}

	@Override
	public void render() {
		glDrawStringCenter(Display.getWidth()/2, 29, "ciaoooooooooooooooooooooooooooooo");
	}

	@Override
	public void beforeRender(float dt) {
		
	}

	@Override
	public boolean mustBeRefreshed() {
		return true;
	}

	@Override
	public boolean keyPressed(Key k) {
		switch (k) {
			case LETTER_X:
				Main.d.goBack();
				try {
					Calculator.solve('X');
				} catch (Errore e) {
					Screen scr = Main.d.getScreen();
					if (scr instanceof EquationScreen) {
						EquationScreen escr = (EquationScreen) scr;
						escr.errorLevel = 1;
						escr.err2 = e;
					} else {
						e.printStackTrace();
					}
				}
				return true;
			default:
				return false;
		}
	}

	@Override
	public boolean keyReleased(Key k) {
		return false;
	}

}

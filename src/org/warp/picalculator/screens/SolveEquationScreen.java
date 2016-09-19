package org.warp.picalculator.screens;

import static org.warp.picalculator.device.graphicengine.Display.Render.*;

import org.warp.picalculator.Error;
import org.warp.picalculator.Main;
import org.warp.picalculator.device.Keyboard.Key;
import org.warp.picalculator.device.graphicengine.Screen;
import org.warp.picalculator.math.Calculator;

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
		glColor4f(0, 0, 0, 64);
		glDrawStringCenter(Main.screenSize[0]/2+1, Main.screenSize[1]/4, "WORK IN PROGRESS. THIS SCREEN MUST HAVE A GUI TO SELECT THE VARIABLE TO SOLVE.");
		glDrawStringCenter(Main.screenSize[0]/2, Main.screenSize[1]/4+1, "WORK IN PROGRESS. THIS SCREEN MUST HAVE A GUI TO SELECT THE VARIABLE TO SOLVE.");
		glDrawStringCenter(Main.screenSize[0]/2+1, Main.screenSize[1]/4+1, "WORK IN PROGRESS. THIS SCREEN MUST HAVE A GUI TO SELECT THE VARIABLE TO SOLVE.");
		glColor3f(255, 0, 0);
		glDrawStringCenter(Main.screenSize[0]/2, Main.screenSize[1]/4, "WORK IN PROGRESS. THIS SCREEN MUST HAVE A GUI TO SELECT THE VARIABLE TO SOLVE.");
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
				} catch (Error e) {
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

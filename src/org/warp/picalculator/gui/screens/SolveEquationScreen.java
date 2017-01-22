package org.warp.picalculator.gui.screens;

import org.warp.picalculator.Error;
import org.warp.picalculator.Main;
import org.warp.picalculator.device.Keyboard.Key;
import org.warp.picalculator.gui.PIDisplay;
import org.warp.picalculator.math.Calculator;

public class SolveEquationScreen extends Screen {

	@SuppressWarnings("unused")
	private MathInputScreen es;

	public SolveEquationScreen(MathInputScreen es) {
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
		PIDisplay.renderer.glColor4i(0, 0, 0, 64);
		PIDisplay.renderer.glDrawStringCenter(Main.screenSize[0]/2+1, Main.screenSize[1]/4, "WORK IN PROGRESS. THIS SCREEN MUST HAVE A GUI TO SELECT THE VARIABLE TO SOLVE.");
		PIDisplay.renderer.glDrawStringCenter(Main.screenSize[0]/2, Main.screenSize[1]/4+1, "WORK IN PROGRESS. THIS SCREEN MUST HAVE A GUI TO SELECT THE VARIABLE TO SOLVE.");
		PIDisplay.renderer.glDrawStringCenter(Main.screenSize[0]/2+1, Main.screenSize[1]/4+1, "WORK IN PROGRESS. THIS SCREEN MUST HAVE A GUI TO SELECT THE VARIABLE TO SOLVE.");
		PIDisplay.renderer.glColor3i(255, 0, 0);
		PIDisplay.renderer.glDrawStringCenter(Main.screenSize[0]/2, Main.screenSize[1]/4, "WORK IN PROGRESS. THIS SCREEN MUST HAVE A GUI TO SELECT THE VARIABLE TO SOLVE.");
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
				PIDisplay.INSTANCE.goBack();
				try {
					es.calc.solveExpression('X');
				} catch (Error e) {
					Screen scr = PIDisplay.INSTANCE.getScreen();
					if (scr instanceof MathInputScreen) {
						MathInputScreen escr = (MathInputScreen) scr;
						escr.errorLevel = 1;
						//escr.err2 = e; //TODO: What is this variable, and why it doesn't exists?
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

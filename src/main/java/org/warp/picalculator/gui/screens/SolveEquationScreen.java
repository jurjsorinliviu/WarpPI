package org.warp.picalculator.gui.screens;

import org.warp.picalculator.StaticVars;
import org.warp.picalculator.device.Keyboard.Key;
import org.warp.picalculator.gui.DisplayManager;

public class SolveEquationScreen extends Screen {

	@SuppressWarnings("unused")
	private final MathInputScreen es;

	public SolveEquationScreen(MathInputScreen es) {
		super();
		canBeInHistory = false;

		this.es = es;
	}

	@Override
	public void created() throws InterruptedException {}

	@Override
	public void initialized() throws InterruptedException {}

	@Override
	public void render() {
		DisplayManager.INSTANCE.renderer.glColor4i(0, 0, 0, 64);
		DisplayManager.INSTANCE.renderer.glDrawStringCenter(StaticVars.screenSize[0] / 2 + 1, StaticVars.screenSize[1] / 4, "WORK IN PROGRESS. THIS SCREEN MUST HAVE A GUI TO SELECT THE VARIABLE TO SOLVE.");
		DisplayManager.INSTANCE.renderer.glDrawStringCenter(StaticVars.screenSize[0] / 2, StaticVars.screenSize[1] / 4 + 1, "WORK IN PROGRESS. THIS SCREEN MUST HAVE A GUI TO SELECT THE VARIABLE TO SOLVE.");
		DisplayManager.INSTANCE.renderer.glDrawStringCenter(StaticVars.screenSize[0] / 2 + 1, StaticVars.screenSize[1] / 4 + 1, "WORK IN PROGRESS. THIS SCREEN MUST HAVE A GUI TO SELECT THE VARIABLE TO SOLVE.");
		DisplayManager.INSTANCE.renderer.glColor3i(255, 0, 0);
		DisplayManager.INSTANCE.renderer.glDrawStringCenter(StaticVars.screenSize[0] / 2, StaticVars.screenSize[1] / 4, "WORK IN PROGRESS. THIS SCREEN MUST HAVE A GUI TO SELECT THE VARIABLE TO SOLVE.");
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
				//TODO: far funzionare questa parte
				/*DisplayManager.INSTANCE.goBack();
				try {
					es.calc.solveExpression('X');
				} catch (final Error e) {
					final Screen scr = DisplayManager.INSTANCE.getScreen();
					if (scr instanceof MathInputScreen) {
						final MathInputScreen escr = (MathInputScreen) scr;
						escr.errorLevel = 1;
						//escr.err2 = e; //TODO: What is this variable, and why it doesn't exists?
					} else {
						e.printStackTrace();
					}
				}
				*/
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

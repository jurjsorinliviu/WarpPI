package org.warp.picalculator.screens;

import org.warp.device.Keyboard.Key;
import org.warp.device.PIDisplay;
import org.warp.engine.Screen;

public class EmptyScreen extends Screen {

	public float endLoading;

	public EmptyScreen() {
		super();
		canBeInHistory = false;
	}
	
	@Override
	public void created() throws InterruptedException {
		endLoading = 0;
	}

	@Override
	public void init() throws InterruptedException {}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeRender(float dt) {
		endLoading += dt;
		if (PIDisplay.loading & endLoading >= 2.5) {
			PIDisplay.loading = false;
		}
	}

	@Override
	public boolean mustBeRefreshed() {
		return true;
	}

	@Override
	public boolean keyPressed(Key k) {
		
		return false;
	}

	@Override
	public boolean keyReleased(Key k) {
		return false;
	}

}

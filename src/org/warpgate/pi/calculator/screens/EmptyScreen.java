package org.warpgate.pi.calculator.screens;

import org.warp.engine.lwjgl.Screen;

public class EmptyScreen extends Screen {

	public float endLoading;
	
	@Override
	public void init() throws InterruptedException {
		canBeInHistory = true;
		endLoading = 0;
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeRender(float dt) {
		endLoading += dt;
		if (d.loading & endLoading >= 2.5) {
			d.loading = false;
		}
	}

}

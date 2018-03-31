package org.warp.picalculator.gui.screens;

import org.warp.picalculator.StaticVars;
import org.warp.picalculator.device.Key;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.GraphicUtils;

public class LoadingScreen extends Screen {

	public float endLoading;
	boolean mustRefresh = true;
	public float loadingTextTranslation = 0.0f;
	public boolean loaded = false;
	private float previousZoomValue = 1;

	public LoadingScreen() {
		super();
		canBeInHistory = false;
	}

	@Override
	public void created() throws InterruptedException {
		endLoading = 0;
	}

	@Override
	public void initialized() throws InterruptedException {
		previousZoomValue = StaticVars.getCurrentZoomValue();
		StaticVars.windowZoom = 1;
	}

	@Override
	public void beforeRender(float dt) {
		loadingTextTranslation = GraphicUtils.sinDeg(endLoading * 90f) * 10f;

		endLoading += dt;
		if (loaded && (StaticVars.debugOn || endLoading >= 3.5f)) {
			StaticVars.windowZoom = previousZoomValue;
			DisplayManager.INSTANCE.setScreen(new MathInputScreen());
		}
		mustRefresh = true;
	}

	@Override
	public void render() {
		DisplayManager.INSTANCE.guiSkin.use(DisplayManager.INSTANCE.engine);
		DisplayManager.INSTANCE.renderer.glColor3i(255, 255, 255);
		DisplayManager.INSTANCE.renderer.glFillRect(StaticVars.screenSize[0] / 2f - 80, StaticVars.screenSize[1] / 2f - 64, 160, 48, 0, 32, 160, 48);
		DisplayManager.INSTANCE.renderer.glFillRect(StaticVars.screenSize[0] / 2f - 24, StaticVars.screenSize[1] / 2f - loadingTextTranslation, 48, 48, 160, 32, 48, 48);

		DisplayManager.INSTANCE.renderer.glFillRect(StaticVars.screenSize[0] - 224, StaticVars.screenSize[1] - 48, 224, 48, 0, 80, 224, 48);
		DisplayManager.INSTANCE.renderer.glFillRect(StaticVars.screenSize[0] - 160 - 24 - 224, StaticVars.screenSize[1] - 48, 160, 48, 224, 80, 160, 48);

	}

	@Override
	public boolean mustBeRefreshed() {
		if (mustRefresh) {
			mustRefresh = false;
			return true;
		} else {
			return false;
		}
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

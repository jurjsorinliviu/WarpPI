package org.warp.picalculator.gui.screens;

import static org.warp.picalculator.gui.DisplayManager.colore;
import static org.warp.picalculator.gui.DisplayManager.fonts;

import org.warp.picalculator.Main;
import org.warp.picalculator.device.Keyboard.Key;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.GraphicUtils;

public class LoadingScreen extends Screen {

	public float endLoading;
	boolean mustRefresh = true;
	public float loadingTextTranslation = 0.0f;
	private boolean loading;
	private static final String titleString = "Andrea Cavalli's Algebraic Calculator";

	public LoadingScreen() {
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
	public void beforeRender(float dt) {
		loadingTextTranslation = GraphicUtils.sinDeg(endLoading * 90f) * 10f;

		endLoading += dt;
		if (endLoading >= 5f) {
			loading = false;
			DisplayManager.INSTANCE.setScreen(new MathInputScreen());
		}
		mustRefresh = true;
	}

	@Override
	public void render() {
		fonts[2].use(DisplayManager.display);
		DisplayManager.renderer.glClearColor(0xFF000000);
		DisplayManager.renderer.glClear(DisplayManager.display.getWidth(), DisplayManager.display.getWidth());
		DisplayManager.renderer.glColor3i(230, 33, 23);
		DisplayManager.renderer.glDrawStringCenter((Main.screenSize[0] / 2) - 1, (int) ((Main.screenSize[1] / 2) - 25 + loadingTextTranslation), titleString);
		DisplayManager.renderer.glDrawStringCenter((Main.screenSize[0] / 2) - 2, (int) ((Main.screenSize[1] / 2) - 25 + loadingTextTranslation), titleString);
		DisplayManager.renderer.glDrawStringCenter((Main.screenSize[0] / 2) + 1, (int) ((Main.screenSize[1] / 2) - 25 + loadingTextTranslation), titleString);
		DisplayManager.renderer.glDrawStringCenter((Main.screenSize[0] / 2) + 2, (int) ((Main.screenSize[1] / 2) - 25 + loadingTextTranslation), titleString);
		DisplayManager.renderer.glDrawStringCenter((Main.screenSize[0] / 2), (int) ((Main.screenSize[1] / 2) - 25 - 1 + loadingTextTranslation), titleString);
		DisplayManager.renderer.glDrawStringCenter((Main.screenSize[0] / 2), (int) ((Main.screenSize[1] / 2) - 25 - 2 + loadingTextTranslation), titleString);
		DisplayManager.renderer.glDrawStringCenter((Main.screenSize[0] / 2), (int) ((Main.screenSize[1] / 2) - 25 + 1 + loadingTextTranslation), titleString);
		DisplayManager.renderer.glDrawStringCenter((Main.screenSize[0] / 2), (int) ((Main.screenSize[1] / 2) - 25 + 2 + loadingTextTranslation), titleString);

		DisplayManager.renderer.glDrawStringCenter((Main.screenSize[0] / 2) + 1, (int) ((Main.screenSize[1] / 2) - 25 + 1 + loadingTextTranslation), titleString);
		DisplayManager.renderer.glDrawStringCenter((Main.screenSize[0] / 2) - 1, (int) ((Main.screenSize[1] / 2) - 25 + 1 + loadingTextTranslation), titleString);
		DisplayManager.renderer.glDrawStringCenter((Main.screenSize[0] / 2) - 1, (int) ((Main.screenSize[1] / 2) - 25 - 1 + loadingTextTranslation), titleString);
		DisplayManager.renderer.glDrawStringCenter((Main.screenSize[0] / 2) + 1, (int) ((Main.screenSize[1] / 2) - 25 - 1 + loadingTextTranslation), titleString);
		DisplayManager.renderer.glColor3i(255, 255, 255);
		DisplayManager.renderer.glDrawStringCenter((Main.screenSize[0] / 2), (int) ((Main.screenSize[1] / 2) - 25 + loadingTextTranslation), titleString);
		DisplayManager.renderer.glColor4f(0.0f, 0.0f, 0.0f, 0.75f);
		fonts[0].use(DisplayManager.display);
		DisplayManager.renderer.glDrawStringCenter((Main.screenSize[0] / 2), (Main.screenSize[1] / 2) + 11, "LOADING");
		fonts[1].use(DisplayManager.display);
		DisplayManager.renderer.glColor4f(0.0f, 0.0f, 0.0f, 0.5f);
		DisplayManager.renderer.glDrawStringCenter((Main.screenSize[0] / 2), (Main.screenSize[1] / 2) + 22, "PLEASE WAIT...");

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

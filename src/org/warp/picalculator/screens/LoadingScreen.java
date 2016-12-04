package org.warp.picalculator.screens;

import static org.warp.picalculator.device.PIDisplay.colore;
import static org.warp.picalculator.device.PIDisplay.fonts;
import static org.warp.picalculator.device.graphicengine.Display.Render.glDrawStringCenter;
import static org.warp.picalculator.device.graphicengine.Display.Render.glSetFont;

import org.warp.picalculator.Main;
import org.warp.picalculator.device.Keyboard.Key;
import org.warp.picalculator.device.PIDisplay;
import org.warp.picalculator.device.graphicengine.Screen;

public class LoadingScreen extends Screen {

	public float endLoading;
	boolean mustRefresh = true;
	public float loadingTextTranslation = 0.0f;
	public boolean loadingTextTranslationTopToBottom = true;
	private boolean loading;
	private static final String titleString = "PICalculator by Andrea Cavalli";

	public LoadingScreen() {
		super();
		canBeInHistory = false;
	}
	
	@Override
	public void created() throws InterruptedException {
		endLoading = 0;
	}

	@Override
	public void init() throws InterruptedException {
	}
	
	@Override
	public void beforeRender(float dt) {
		if (loadingTextTranslation >= 10.0f) {
			loadingTextTranslation = 10.0f;
			loadingTextTranslationTopToBottom = false;
		} else if (loadingTextTranslation <= -10.0f) {
			loadingTextTranslation = -10.0f;
			loadingTextTranslationTopToBottom = true;
		}

		if (loadingTextTranslationTopToBottom) {
			loadingTextTranslation += dt * 15;
		} else {
			loadingTextTranslation -= dt * 15;
		}
		
		endLoading += dt;
		if (endLoading >= 2) {
			loading = false;
			PIDisplay.INSTANCE.setScreen(new MathInputScreen());
		}
		mustRefresh = true;
	}

	@Override
	public void render() {
		glSetFont(fonts[2]);
		colore(1.0f, 1.0f, 1.0f, 1.0f);
		glDrawStringCenter((Main.screenSize[0] / 2) - 1,(int) ((Main.screenSize[1]/ 2) - 25 + loadingTextTranslation), titleString);
		colore(1.0f, 1.0f, 1.0f, 1.0f);
		glDrawStringCenter((Main.screenSize[0] / 2) + 1,(int) ((Main.screenSize[1]/ 2) - 25 + loadingTextTranslation), titleString);
		colore(1.0f, 1.0f, 1.0f, 1.0f);
		glDrawStringCenter((Main.screenSize[0] / 2), (int) ((Main.screenSize[1]/ 2) - 25 - 1 + loadingTextTranslation), titleString);
		colore(1.0f, 1.0f, 1.0f, 1.0f);
		glDrawStringCenter((Main.screenSize[0] / 2), (int) ((Main.screenSize[1]/ 2) - 25 + 1 + loadingTextTranslation), titleString);
		colore(1.0f, 0.5f, 0.0f, 1.0f);
		glDrawStringCenter((Main.screenSize[0] / 2), (int) ((Main.screenSize[1]/ 2) - 25 + loadingTextTranslation), titleString);
		colore(0.0f, 0.0f, 0.0f, 0.75f);
		glSetFont(fonts[0]);
		glDrawStringCenter((Main.screenSize[0] / 2), (Main.screenSize[1]/ 2) + 11, "LOADING");
		glSetFont(fonts[1]);
		colore(0.0f, 0.0f, 0.0f, 0.5f);
		glDrawStringCenter((Main.screenSize[0] / 2), (Main.screenSize[1]/ 2) + 22, "PLEASE WAIT...");
	
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

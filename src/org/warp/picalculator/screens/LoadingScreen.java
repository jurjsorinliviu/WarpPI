package org.warp.picalculator.screens;

import static org.warp.picalculator.device.graphicengine.Display.Render.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Main;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.PIDisplay;
import org.warp.picalculator.device.Keyboard.Key;
import org.warp.picalculator.device.graphicengine.Display;
import org.warp.picalculator.device.graphicengine.Screen;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.functions.Function;
import static org.warp.picalculator.device.PIDisplay.fonts;
import static org.warp.picalculator.device.PIDisplay.glyphsHeight;
import static org.warp.picalculator.device.PIDisplay.colore;

public class LoadingScreen extends Screen {

	public float endLoading;
	boolean mustRefresh = true;
	public float loadingTextTranslation = 0.0f;
	public boolean loadingTextTranslationTopToBottom = true;
	private boolean loading;

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
		if (endLoading >= 1) {
			loading = false;
			PIDisplay.INSTANCE.setScreen(new EquationScreen());
		}
		mustRefresh = true;
	}

	@Override
	public void render() {
		setFont(fonts[2]);
		colore(1.0f, 1.0f, 1.0f, 1.0f);
		glDrawStringCenter((Main.screenSize[0] / 2) - 1,(int) ((Main.screenSize[1]/ 2) - 25 + loadingTextTranslation), "ANDREA CAVALLI'S CALCULATOR");
		colore(1.0f, 1.0f, 1.0f, 1.0f);
		glDrawStringCenter((Main.screenSize[0] / 2) + 1,(int) ((Main.screenSize[1]/ 2) - 25 + loadingTextTranslation), "ANDREA CAVALLI'S CALCULATOR");
		colore(1.0f, 1.0f, 1.0f, 1.0f);
		glDrawStringCenter((Main.screenSize[0] / 2), (int) ((Main.screenSize[1]/ 2) - 25 - 1 + loadingTextTranslation), "ANDREA CAVALLI'S CALCULATOR");
		colore(1.0f, 1.0f, 1.0f, 1.0f);
		glDrawStringCenter((Main.screenSize[0] / 2), (int) ((Main.screenSize[1]/ 2) - 25 + 1 + loadingTextTranslation), "ANDREA CAVALLI'S CALCULATOR");
		colore(1.0f, 0.5f, 0.0f, 1.0f);
		glDrawStringCenter((Main.screenSize[0] / 2), (int) ((Main.screenSize[1]/ 2) - 25 + loadingTextTranslation), "ANDREA CAVALLI'S CALCULATOR");
		colore(0.0f, 0.0f, 0.0f, 0.75f);
		setFont(fonts[0]);
		glDrawStringCenter((Main.screenSize[0] / 2), (Main.screenSize[1]/ 2) + 11, "LOADING");
		setFont(fonts[1]);
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

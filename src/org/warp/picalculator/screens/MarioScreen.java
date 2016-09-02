package org.warp.picalculator.screens;

import static org.warp.engine.Display.Render.getMatrixOfImage;
import static org.warp.engine.Display.Render.glClearColor;
import static org.warp.engine.Display.Render.glDrawSkin;
import static org.warp.engine.Display.Render.glDrawStringLeft;
import static org.warp.engine.Display.Render.setFont;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.warp.device.Keyboard;
import org.warp.device.Keyboard.Key;
import org.warp.device.PIDisplay;
import org.warp.engine.Screen;
import org.warp.picalculator.Main;

public class MarioScreen extends Screen {

	private int[] skin;
	private int[] skinSize;
	private boolean errored;
	public int posX = 0;
	
	public MarioScreen() {
		super();
		canBeInHistory = true;
	}

	@Override
	public void init() {
		BufferedImage img;
		try {
			img = ImageIO.read(Main.instance.getClass().getResource("/marioskin.png"));
			skin = getMatrixOfImage(img);
			skinSize = new int[] { img.getWidth(), img.getHeight() };
		} catch (IOException e) {
			e.printStackTrace();
			errored = true;
		}
	}
	
	@Override
	public void created() throws InterruptedException {
		if (!errored) {
			
		}
	}

	@Override
	public void beforeRender(float dt) {
		if (!errored) {
			if (Keyboard.isKeyDown(2, 5)) { //RIGHT
				posX+=1;
			}
		}
	}

	@Override
	public void render() {
		if (errored) {
			glDrawStringLeft(0, 20, "ERROR");
		} else {
			setFont(PIDisplay.fonts[0]);
			glClearColor(0xFFCCE7D4);
			glDrawSkin(skinSize[0], skin, posX, 25, 36, 1, 70, 27, true);
		}
	}

	@Override
	public boolean mustBeRefreshed() {
		return true;
	}

	@Override
	public boolean keyReleased(Key k) {
		return false;
	}

	@Override
	public boolean keyPressed(Key k) {
		return false;
	}

}

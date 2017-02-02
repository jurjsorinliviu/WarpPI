package org.warp.picalculator.gui.screens;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.warp.picalculator.Main;
import org.warp.picalculator.device.Keyboard;
import org.warp.picalculator.device.Keyboard.Key;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.graphicengine.Skin;

public class MarioScreen extends Screen {

	private Skin skin;
	private Skin groundskin;
	private boolean errored;
	public float[] marioPos = new float[] { 30, 0 };
	public float[] marioForces = new float[] { 0, 0 };
	public float walkAnimation = 0;
	public float jumptime = 0;
	public boolean walking = false;
	public boolean running = false;
	public boolean jumping = false;
	public boolean flipped = false;
	public boolean onGround = true;
	public int[] marioSkinPos = new int[] { 0, 0 };

	public MarioScreen() {
		super();
		canBeInHistory = false;
	}

	@Override
	public void init() {
		try {
			skin = DisplayManager.engine.loadSkin("marioskin.png");
			groundskin = DisplayManager.engine.loadSkin("marioground.png");
		} catch (IOException e) {
			e.printStackTrace();
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
			walkAnimation += dt;
			final boolean rightPressed = Keyboard.isKeyDown(2, 5);
			final boolean leftPressed = Keyboard.isKeyDown(2, 3);
			final boolean jumpPressed = Keyboard.isKeyDown(2, 1);
			if ((leftPressed || rightPressed) == (leftPressed & rightPressed)) {
				walking = false;
				walkAnimation = 0;
			} else {
				if (rightPressed) { //RIGHT
					if (marioForces[0] < 500f) {
						marioForces[0] += dt * 500f;
					}
					walking = true;
					flipped = false;
				}
				if (leftPressed) { //LEFT
					if (marioForces[0] > -500f) {
						marioForces[0] -= dt * 500f;
					}
					walking = true;
					flipped = true;
				}
			}
			if (jumpPressed) { //JUMP
				jumptime += dt;
				if (!jumping && onGround) {
					marioForces[1] = dt * (4 * 1569.6f);
					jumping = true;
					onGround = false;
				} else if (jumptime <= 0.5f) {
					marioForces[1] = dt * (4 * 1569.6f);
				}
			} else {
				jumping = false;
				jumptime = 0;
			}
			if (!walking & !running & !jumping) {
				marioSkinPos[0] = 0;
				marioSkinPos[1] = 0;
			} else if (onGround & walking & !running & !jumping && walkAnimation >= 0.08) {
				while (walkAnimation > 0.08) {
					walkAnimation -= 0.08;
					if (marioSkinPos[0] == 1 & marioSkinPos[1] == 0) {
						marioSkinPos[0] += 2;
					} else if (marioSkinPos[0] == 3 & marioSkinPos[1] == 0) {
						marioSkinPos[0] -= 1;
					} else if (marioSkinPos[0] == 2 & marioSkinPos[1] == 0) {
						marioSkinPos[0] -= 1;
					} else {
						marioSkinPos[0] = 1;
						marioSkinPos[1] = 0;
					}
				}
			} else if (jumping) {
				marioSkinPos[0] = 5;
				marioSkinPos[1] = 1;
			}
			marioForces[1] -= dt * 1569.6;
			marioPos[0] += dt * marioForces[0];
			if (!onGround) {
				marioPos[1] -= dt * marioForces[1];
			}
			marioForces[0] *= 0.75;

			DisplayManager.renderer.glClearColor(0xff9290ff);
		}
	}

	@Override
	public void render() {
		if (errored) {
			DisplayManager.renderer.glDrawStringLeft(0, 20, "ERROR");
		} else {
			DisplayManager.fonts[0].use(DisplayManager.engine);
			groundskin.use(DisplayManager.engine);
			DisplayManager.renderer.glFillRect(16 * 0, 25 + 25, 16, 16, 0, 0, 16, 16);
			DisplayManager.renderer.glFillRect(16 * 1, 25 + 25, 16, 16, 0, 0, 16, 16);
			DisplayManager.renderer.glFillRect(16 * 2, 25 + 25, 16, 16, 0, 0, 16, 16);
			DisplayManager.renderer.glFillRect(16 * 3, 25 + 25, 16, 16, 0, 0, 16, 16);
			DisplayManager.renderer.glFillRect(16 * 4, 25 + 25, 16, 16, 0, 0, 16, 16);
			DisplayManager.renderer.glFillRect(16 * 5, 25 + 25, 16, 16, 0, 0, 16, 16);
			DisplayManager.renderer.glFillRect(16 * 6, 25 + 25, 16, 16, 0, 0, 16, 16);
			DisplayManager.renderer.glFillRect(16 * 7, 25 + 25, 16, 16, 0, 0, 16, 16);
			DisplayManager.renderer.glFillRect(16 * 8, 25 + 25, 16, 16, 0, 0, 16, 16);

			DisplayManager.renderer.glFillRect(16 * 0, 25 + 25 + 16 * 1, 16, 16, 0, 0, 16, 16);
			DisplayManager.renderer.glFillRect(16 * 1, 25 + 25 + 16 * 1, 16, 16, 0, 0, 16, 16);
			DisplayManager.renderer.glFillRect(16 * 2, 25 + 25 + 16 * 1, 16, 16, 0, 0, 16, 16);
			DisplayManager.renderer.glFillRect(16 * 3, 25 + 25 + 16 * 1, 16, 16, 0, 0, 16, 16);
			DisplayManager.renderer.glFillRect(16 * 4, 25 + 25 + 16 * 1, 16, 16, 0, 0, 16, 16);
			DisplayManager.renderer.glFillRect(16 * 5, 25 + 25 + 16 * 1, 16, 16, 0, 0, 16, 16);
			DisplayManager.renderer.glFillRect(16 * 6, 25 + 25 + 16 * 1, 16, 16, 0, 0, 16, 16);
			DisplayManager.renderer.glFillRect(16 * 7, 25 + 25 + 16 * 1, 16, 16, 0, 0, 16, 16);
			DisplayManager.renderer.glFillRect(16 * 8, 25 + 25 + 16 * 1, 16, 16, 0, 0, 16, 16);

//		EASTER EGG
//			glSetFont(PIDisplay.fonts[4]);
//			glColor(0xFF000000);
//			glDrawStringRight(0, Main.screenSize[1]-glGetCurrentFontHeight(), "A");
//			glColor(0xFF800000);
//			glDrawStringRight(0, Main.screenSize[1]-glGetCurrentFontHeight(), "B");
//			glColor(0xFFeea28e);
//			glDrawStringRight(0, Main.screenSize[1]-glGetCurrentFontHeight(), "C");
//			glColor(0xFFee7255);
//			glDrawStringRight(0, Main.screenSize[1]-glGetCurrentFontHeight(), "D");
//			glColor(0xFFeac0b0);
//			glDrawStringRight(0, Main.screenSize[1]-glGetCurrentFontHeight(), "E");
//			glColor(0xFFf3d8ce);
//			glDrawStringRight(0, Main.screenSize[1]-glGetCurrentFontHeight(), "F");
//			glColor(0xffede7);
//			glDrawStringRight(0, Main.screenSize[1]-glGetCurrentFontHeight(), "G");

			//DRAW MARIO
			skin.use(DisplayManager.engine);
			DisplayManager.renderer.glFillRect(getPosX() - 18, 25 + getPosY(), 35, 27, 35 * (marioSkinPos[0] + 1), 27 * marioSkinPos[1], 35, 27);
//			PIDisplay.renderer.glDrawSkin(getPosX() - 18, 25 + getPosY(), 35 * (marioSkinPos[0] + (flipped ? 2 : 1)), 27 * marioSkinPos[1], 35 * (marioSkinPos[0] + (flipped ? 1 : 2)), 27 * (marioSkinPos[1] + 1), true);
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

	private int getPosX() {
		return (int) marioPos[0];
	}

	private int getPosY() {
		return (int) marioPos[1];
	}

}

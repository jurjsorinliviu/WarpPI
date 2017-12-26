package org.warp.picalculator.gui.screens;

import java.io.IOException;

import org.warp.picalculator.StaticVars;
import org.warp.picalculator.device.Keyboard;
import org.warp.picalculator.device.Keyboard.Key;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.gui.graphicengine.Skin;

public class MarioScreen extends Screen {

	private static Skin skin;
	private static Skin groundskin;
	private static BinaryFont easterfont;
	private static BinaryFont fu32font;
	private static Skin easterskin;
	private int easterNum = 0;
	private float easterElapsed = 0;
	private int easterMax = 21;
	private String[] easterFu32 = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "ò"};
	private int easterFu32Num = 0;
	private float easterFu32Elapsed = 0;
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
			if (skin == null)
				skin = DisplayManager.INSTANCE.engine.loadSkin("marioskin.png");
			if (groundskin == null)
				groundskin = DisplayManager.INSTANCE.engine.loadSkin("marioground.png");
			if (easterfont == null)
				try {
					easterfont = DisplayManager.INSTANCE.engine.loadFont("easter");
				} catch (Exception ex) {}
			if (fu32font == null)
				try {
					fu32font = DisplayManager.INSTANCE.engine.loadFont("fu32");
				} catch (Exception ex) {}
			if (easterskin == null)
				try {
					easterskin = DisplayManager.INSTANCE.engine.loadSkin("font_easter.png");
				} catch (Exception ex) {}
		} catch (final IOException e) {
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

			easterElapsed += dt;
			while (easterElapsed >= 0.04) {
				easterNum = (easterNum + 1) % easterMax;
				easterElapsed -= 0.04;
			}
			easterFu32Elapsed += dt;
			while (easterFu32Elapsed >= 1.5) {
				easterFu32Num = (easterFu32Num + 1) % easterFu32.length;
				easterFu32Elapsed -= 1.5;
			}
			
			DisplayManager.INSTANCE.renderer.glClearColor(0xff9290ff);
		}
	}

	@Override
	public void render() {
		if (errored) {
			DisplayManager.INSTANCE.renderer.glDrawStringLeft(0, 20, "ERROR");
		} else {
			groundskin.use(DisplayManager.INSTANCE.engine);
			DisplayManager.INSTANCE.renderer.glFillRect(16 * 0, 25 + 25, 16, 16, 0, 0, 16, 16);
			DisplayManager.INSTANCE.renderer.glFillRect(16 * 1, 25 + 25, 16, 16, 0, 0, 16, 16);
			DisplayManager.INSTANCE.renderer.glFillRect(16 * 2, 25 + 25, 16, 16, 0, 0, 16, 16);
			DisplayManager.INSTANCE.renderer.glFillRect(16 * 3, 25 + 25, 16, 16, 0, 0, 16, 16);
			DisplayManager.INSTANCE.renderer.glFillRect(16 * 4, 25 + 25, 16, 16, 0, 0, 16, 16);
			DisplayManager.INSTANCE.renderer.glFillRect(16 * 5, 25 + 25, 16, 16, 0, 0, 16, 16);
			DisplayManager.INSTANCE.renderer.glFillRect(16 * 6, 25 + 25, 16, 16, 0, 0, 16, 16);
			DisplayManager.INSTANCE.renderer.glFillRect(16 * 7, 25 + 25, 16, 16, 0, 0, 16, 16);
			DisplayManager.INSTANCE.renderer.glFillRect(16 * 8, 25 + 25, 16, 16, 0, 0, 16, 16);

			DisplayManager.INSTANCE.renderer.glFillRect(16 * 0, 25 + 25 + 16 * 1, 16, 16, 0, 0, 16, 16);
			DisplayManager.INSTANCE.renderer.glFillRect(16 * 1, 25 + 25 + 16 * 1, 16, 16, 0, 0, 16, 16);
			DisplayManager.INSTANCE.renderer.glFillRect(16 * 2, 25 + 25 + 16 * 1, 16, 16, 0, 0, 16, 16);
			DisplayManager.INSTANCE.renderer.glFillRect(16 * 3, 25 + 25 + 16 * 1, 16, 16, 0, 0, 16, 16);
			DisplayManager.INSTANCE.renderer.glFillRect(16 * 4, 25 + 25 + 16 * 1, 16, 16, 0, 0, 16, 16);
			DisplayManager.INSTANCE.renderer.glFillRect(16 * 5, 25 + 25 + 16 * 1, 16, 16, 0, 0, 16, 16);
			DisplayManager.INSTANCE.renderer.glFillRect(16 * 6, 25 + 25 + 16 * 1, 16, 16, 0, 0, 16, 16);
			DisplayManager.INSTANCE.renderer.glFillRect(16 * 7, 25 + 25 + 16 * 1, 16, 16, 0, 0, 16, 16);
			DisplayManager.INSTANCE.renderer.glFillRect(16 * 8, 25 + 25 + 16 * 1, 16, 16, 0, 0, 16, 16);

//		EASTER EGG
			if (fu32font != null) {
				DisplayManager.INSTANCE.renderer.glColor3f(1,1,1);
				DisplayManager.INSTANCE.renderer.glFillColor(DisplayManager.INSTANCE.engine.getWidth()-256, DisplayManager.INSTANCE.engine.getHeight() / 2 - 128, 256, 256);
				fu32font.use(DisplayManager.INSTANCE.engine);
				DisplayManager.INSTANCE.renderer.glColor3f(0, 0, 0);
				DisplayManager.INSTANCE.renderer.glDrawStringRight(DisplayManager.INSTANCE.engine.getWidth(), DisplayManager.INSTANCE.engine.getHeight() / 2 - 128, easterFu32[easterFu32Num]);
			}
			if (easterskin != null) {
				easterskin.use(DisplayManager.INSTANCE.engine);
				DisplayManager.INSTANCE.renderer.glColor4f(1, 1, 1, 0.7f);
				DisplayManager.INSTANCE.renderer.glFillRect(0, StaticVars.screenSize[1] - 128, 224, 128, easterNum * 224, 0, 224, 128);
			}
			if (easterfont != null) {
				easterfont.use(DisplayManager.INSTANCE.engine);
				DisplayManager.INSTANCE.renderer.glColor(0xFF000000);
				DisplayManager.INSTANCE.renderer.glDrawStringRight(StaticVars.screenSize[0], DisplayManager.INSTANCE.engine.getHeight() - easterfont.getCharacterHeight(), "A");
				DisplayManager.INSTANCE.renderer.glColor(0xFF800000);
				DisplayManager.INSTANCE.renderer.glDrawStringRight(StaticVars.screenSize[0], DisplayManager.INSTANCE.engine.getHeight() - easterfont.getCharacterHeight(), "B");
				DisplayManager.INSTANCE.renderer.glColor(0xFFeea28e);
				DisplayManager.INSTANCE.renderer.glDrawStringRight(StaticVars.screenSize[0], DisplayManager.INSTANCE.engine.getHeight() - easterfont.getCharacterHeight(), "C");
				DisplayManager.INSTANCE.renderer.glColor(0xFFee7255);
				DisplayManager.INSTANCE.renderer.glDrawStringRight(StaticVars.screenSize[0], DisplayManager.INSTANCE.engine.getHeight() - easterfont.getCharacterHeight(), "D");
				DisplayManager.INSTANCE.renderer.glColor(0xFFeac0b0);
				DisplayManager.INSTANCE.renderer.glDrawStringRight(StaticVars.screenSize[0], DisplayManager.INSTANCE.engine.getHeight() - easterfont.getCharacterHeight(), "E");
				DisplayManager.INSTANCE.renderer.glColor(0xFFf3d8ce);
				DisplayManager.INSTANCE.renderer.glDrawStringRight(StaticVars.screenSize[0], DisplayManager.INSTANCE.engine.getHeight() - easterfont.getCharacterHeight(), "F");
				DisplayManager.INSTANCE.renderer.glColor(0xFFffede7);
				DisplayManager.INSTANCE.renderer.glDrawStringRight(StaticVars.screenSize[0], DisplayManager.INSTANCE.engine.getHeight() - easterfont.getCharacterHeight(), "G");
			}

			//DRAW MARIO
			skin.use(DisplayManager.INSTANCE.engine);
			DisplayManager.INSTANCE.renderer.glFillRect(getPosX() - 18, 25 + getPosY(), 35, 27, 35 * (marioSkinPos[0] + 1), 27 * marioSkinPos[1], 35, 27);
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

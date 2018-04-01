package org.warp.picalculator.gui.screens;

import java.io.IOException;

import org.warp.picalculator.StaticVars;
import org.warp.picalculator.device.Key;
import org.warp.picalculator.device.Keyboard;
import org.warp.picalculator.extra.mario.MarioBlock;
import org.warp.picalculator.extra.mario.MarioGame;
import org.warp.picalculator.extra.mario.MarioWorld;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.gui.graphicengine.Skin;

public class MarioScreen extends Screen {

	private MarioGame g;
	
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
//	public float[] marioPos = new float[] { 30, 0 };
//	public float[] marioForces = new float[] { 0, 0 };
//	public float jumptime = 0;
//	public boolean walking = false;
//	public boolean running = false;
//	public boolean jumping = false;
//	public boolean flipped = false;
//	public boolean onGround = true;

	public MarioScreen() {
		super();
		canBeInHistory = false;
	}

	@Override
	public void initialized() {
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
			g = new MarioGame();
		}
	}

	@Override
	public void beforeRender(float dt) {
		if (!errored) {
			final boolean rightPressed = Keyboard.isKeyDown(2, 5);
			final boolean leftPressed = Keyboard.isKeyDown(2, 3);
			final boolean jumpPressed = Keyboard.isKeyDown(2, 1);
			final boolean upPressed = false, downPressed = false, runPressed = false;
			g.gameTick(dt, upPressed, downPressed, leftPressed, rightPressed, jumpPressed, runPressed);

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
			
			DisplayManager.INSTANCE.renderer.glClearColor(0xff000000);
		}
	}

	@Override
	public void render() {
		if (errored) {
			DisplayManager.INSTANCE.renderer.glDrawStringLeft(0, 20, "ERROR");
		} else {
			if (groundskin != null) {
				double playerX = g.getPlayer().getX();
				double playerY = g.getPlayer().getY();
				groundskin.use(DisplayManager.INSTANCE.engine);
				MarioWorld w = g.getCurrentWorld();
				int width = w.getWidth();
				int height = w.getHeight();
				float screenX = DisplayManager.INSTANCE.engine.getWidth()/2f - 8f;
				float screenY = DisplayManager.INSTANCE.engine.getHeight()/2f - 8f;
				float shiftX = -8 + 16 * (float)playerX;
				float shiftY = -8 + 16 * (height - (float)playerY);
				int blue = -1;
				for (int ix = 0; ix < width; ix++) {
					for (int iy = 0; iy < height; iy++) {
						double distX = Math.abs(playerX - ix);
						double distY = Math.abs(playerY - iy - 1.5d);
						if ((distX*distX + distY*distY/2d) < 25d) {
							byte b = w.getBlockIdAt(ix, iy);
							if (b == 0) {
								if (blue != 1) {
									blue = 1;
									DisplayManager.INSTANCE.renderer.glColor(0xff9290ff);
								}
								DisplayManager.INSTANCE.renderer.glFillColor(screenX - shiftX + 16 * ix, screenY - shiftY + 16 * (height - iy), 16, 16);
							} else {
								if (blue != 0) {
									blue = 0;
									DisplayManager.INSTANCE.renderer.glColor(0xffffffff);
								}
								DisplayManager.INSTANCE.renderer.glFillRect(screenX - shiftX+ 16 * ix, screenY - shiftY + 16 * (height - iy), 16, 16, 0, 0, 16, 16);
							}
						}
					}
				}
				if (blue != 0) {
					blue = 0;
					DisplayManager.INSTANCE.renderer.glColor(0xffffffff);
				}

				//DRAW MARIO
				skin.use(DisplayManager.INSTANCE.engine);
				DisplayManager.INSTANCE.renderer.glFillRect(screenX - (g.getPlayer().flipped ? 3 : 0), screenY,
						35, 27,
						35 * (g.getPlayer().marioSkinPos[0] + (g.getPlayer().flipped ? 2 : 1)), 27 * g.getPlayer().marioSkinPos[1],
						35 * (g.getPlayer().flipped ? -1 : 1), 27);
//				PIDisplay.renderer.glDrawSkin(getPosX() - 18, 25 + getPosY(), 35 * (marioSkinPos[0] + (flipped ? 2 : 1)), 27 * marioSkinPos[1], 35 * (marioSkinPos[0] + (flipped ? 1 : 2)), 27 * (marioSkinPos[1] + 1), true);
			}

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

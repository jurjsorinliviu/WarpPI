package org.warp.picalculator.gui.screens;

import java.io.IOException;

import org.warp.picalculator.StaticVars;
import org.warp.picalculator.device.Key;
import org.warp.picalculator.device.Keyboard;
import org.warp.picalculator.extra.mario.MarioGame;
import org.warp.picalculator.extra.mario.MarioWorld;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.gui.graphicengine.Skin;

public class MarioScreen extends Screen {

	private MarioGame g;
	
	private static Skin skin;
	private static Skin groundskin;
	private static BinaryFont gpuTest2;
	private static BinaryFont gpuTest1;
	private static boolean gpuTest12;
	private static Skin gpuTest3;
	private int gpuTestNum = 0;
	private float gpuTestElapsed = 0;
	private int gpuTestMax = 21;
	private String[] gpuCharTest1 = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "ò"};
	private int gpuCharTest1Num = 0;
	private float gpuCharTestt1Elapsed = 0;
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
			if (gpuTest2 == null)
				try {
					gpuTest2 = DisplayManager.INSTANCE.engine.loadFont("gputest2");
				} catch (Exception ex) {}
			if (gpuTest1 == null)
				try {
					gpuTest1 = DisplayManager.INSTANCE.engine.loadFont("gputest12");
					gpuTest12 = true;
				} catch (Exception ex) {
					gpuTest12 = false;
					try {
						gpuTest1 = DisplayManager.INSTANCE.engine.loadFont("gputest1");
					} catch (Exception ex2) {}
				}
			if (gpuTest3 == null)
				try {
					gpuTest3 = DisplayManager.INSTANCE.engine.loadSkin("font_gputest3.png");
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

			gpuTestElapsed += dt;
			while (gpuTestElapsed >= 0.04) {
				gpuTestNum = (gpuTestNum + 1) % gpuTestMax;
				gpuTestElapsed -= 0.04;
			}
			gpuCharTestt1Elapsed += dt;
			while (gpuCharTestt1Elapsed >= 1.5) {
				gpuCharTest1Num = (gpuCharTest1Num + 1) % gpuCharTest1.length;
				gpuCharTestt1Elapsed -= 1.5;
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

//		GPU PERFORMANCE TEST
			if (gpuTest1 != null) {
				DisplayManager.INSTANCE.renderer.glColor3f(1,1,1);
				DisplayManager.INSTANCE.renderer.glFillColor(DisplayManager.INSTANCE.engine.getWidth()-(gpuTest12 ? 512 : 256), DisplayManager.INSTANCE.engine.getHeight() / 2 - (gpuTest12 ? 256 : 128), gpuTest12 ? 512 : 256, gpuTest12 ? 512 : 256);
				gpuTest1.use(DisplayManager.INSTANCE.engine);
				DisplayManager.INSTANCE.renderer.glColor3f(0, 0, 0);
				DisplayManager.INSTANCE.renderer.glDrawStringRight(DisplayManager.INSTANCE.engine.getWidth(), DisplayManager.INSTANCE.engine.getHeight() / 2 - 128, gpuCharTest1[gpuCharTest1Num]);
			}
			if (gpuTest3 != null) {
				gpuTest3.use(DisplayManager.INSTANCE.engine);
				DisplayManager.INSTANCE.renderer.glColor4f(1, 1, 1, 0.7f);
				DisplayManager.INSTANCE.renderer.glFillRect(0, StaticVars.screenSize[1] - 128, 224, 128, gpuTestNum * 224, 0, 224, 128);
			}
			if (gpuTest2 != null) {
				gpuTest2.use(DisplayManager.INSTANCE.engine);
				DisplayManager.INSTANCE.renderer.glColor(0xFF000000);
				DisplayManager.INSTANCE.renderer.glDrawStringRight(StaticVars.screenSize[0], DisplayManager.INSTANCE.engine.getHeight() - gpuTest2.getCharacterHeight(), "A");
				DisplayManager.INSTANCE.renderer.glColor(0xFF800000);
				DisplayManager.INSTANCE.renderer.glDrawStringRight(StaticVars.screenSize[0], DisplayManager.INSTANCE.engine.getHeight() - gpuTest2.getCharacterHeight(), "B");
				DisplayManager.INSTANCE.renderer.glColor(0xFFeea28e);
				DisplayManager.INSTANCE.renderer.glDrawStringRight(StaticVars.screenSize[0], DisplayManager.INSTANCE.engine.getHeight() - gpuTest2.getCharacterHeight(), "C");
				DisplayManager.INSTANCE.renderer.glColor(0xFFee7255);
				DisplayManager.INSTANCE.renderer.glDrawStringRight(StaticVars.screenSize[0], DisplayManager.INSTANCE.engine.getHeight() - gpuTest2.getCharacterHeight(), "D");
				DisplayManager.INSTANCE.renderer.glColor(0xFFeac0b0);
				DisplayManager.INSTANCE.renderer.glDrawStringRight(StaticVars.screenSize[0], DisplayManager.INSTANCE.engine.getHeight() - gpuTest2.getCharacterHeight(), "E");
				DisplayManager.INSTANCE.renderer.glColor(0xFFf3d8ce);
				DisplayManager.INSTANCE.renderer.glDrawStringRight(StaticVars.screenSize[0], DisplayManager.INSTANCE.engine.getHeight() - gpuTest2.getCharacterHeight(), "F");
				DisplayManager.INSTANCE.renderer.glColor(0xFFffede7);
				DisplayManager.INSTANCE.renderer.glDrawStringRight(StaticVars.screenSize[0], DisplayManager.INSTANCE.engine.getHeight() - gpuTest2.getCharacterHeight(), "G");
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

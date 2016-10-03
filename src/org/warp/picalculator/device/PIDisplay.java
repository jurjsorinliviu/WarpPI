package org.warp.picalculator.device;

import static org.warp.picalculator.device.graphicengine.Display.Render.*;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.warp.picalculator.Main;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.graphicengine.Display;
import org.warp.picalculator.device.graphicengine.RAWFont;
import org.warp.picalculator.device.graphicengine.Screen;
import org.warp.picalculator.device.graphicengine.Display.Startable;
import org.warp.picalculator.math.Calculator;

import com.pi4j.wiringpi.Gpio;

/**
 * STB Truetype oversampling demo.
 *
 * <p>
 * This is a Java port of <a href=
 * "https://github.com/nothings/stb/blob/master/tests/oversample/main.c">https:/
 * /github
 * .com/nothings/stb/blob/master/tests/oversample/main.c</a>.
 * </p>
 */
public final class PIDisplay {
	public static PIDisplay INSTANCE;
	private static float brightness;

	private int[] skin;
	private int[] skinSize;
	public static RAWFont[] fonts = new RAWFont[4];

	public static String error = null;
	public String[] errorStackTrace = null;
	public final static int[] glyphsHeight = new int[] { 9, 6, 18, 12 };

	public static Screen screen;
	public static String displayDebugString = "";

	public PIDisplay(Screen screen) {
		setScreen(screen);
		INSTANCE = this;
		run();
	}
	/*
	 * private void load_skin() {
	 * try {
	 * skin_tex = glGenTextures();
	 * glBindTexture(GL_TEXTURE_2D, skin_tex);
	 * glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
	 * 
	 * InputStream in = new FileInputStream("skin.png");
	 * PNGDecoder decoder = new PNGDecoder(in);
	 * 
	 * System.out.println("width="+decoder.getWidth());
	 * System.out.println("height="+decoder.getHeight());
	 * 
	 * ByteBuffer buf =
	 * ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
	 * decoder.decode(buf, decoder.getWidth()*4, Format.RGBA);
	 * buf.flip();
	 * 
	 * skin = buf;
	 * skin_w = decoder.getWidth();
	 * skin_h = decoder.getHeight();
	 * glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, skin_w,
	 * skin_h, 0, GL_RGBA, GL_UNSIGNED_BYTE, skin);
	 * } catch (IOException ex) {
	 * ex.printStackTrace();
	 * }
	 * }
	 */

	public void setScreen(Screen screen) {
		if (screen.initialized == false) {
			if (screen.canBeInHistory) {
				Calculator.currentSession = 0;
				for (int i = Calculator.sessions.length - 1; i >= 1; i--) {
					Calculator.sessions[i] = Calculator.sessions[i - 1];
				}
				Calculator.sessions[0] = screen;
			} else {
				Calculator.currentSession = -1;
			}
		}
		screen.d = this;
		try {
			screen.create();
			PIDisplay.screen = screen;
			if (screen.initialized == false) {
				screen.initialize();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public boolean canGoBack() {
		if (Calculator.currentSession == -1) {
			return Calculator.sessions[0] != null;
		}
		if (PIDisplay.screen != Calculator.sessions[Calculator.currentSession]) {

		} else if (Calculator.currentSession + 1 < Calculator.sessions.length) {
			if (Calculator.sessions[Calculator.currentSession + 1] != null) {

			} else {
				return false;
			}
		} else {
			return false;
		}
		if (Calculator.sessions[Calculator.currentSession] != null) {
			return true;
		}
		return false;
	}

	public void goBack() {
		if (canGoBack()) {
			if (Calculator.currentSession >= 0 && PIDisplay.screen != Calculator.sessions[Calculator.currentSession]) {
			} else {
				Calculator.currentSession += 1;
			}
			PIDisplay.screen = Calculator.sessions[Calculator.currentSession];
			Utils.debug.println("Current session: " + Calculator.currentSession);
		}
	}

	public boolean canGoForward() {
		if (Calculator.currentSession <= 0) { // -1 e 0
			return false;
		}
		if (PIDisplay.screen != Calculator.sessions[Calculator.currentSession]) {

		} else if (Calculator.currentSession > 0) {
			if (Calculator.sessions[Calculator.currentSession - 1] != null) {

			} else {
				return false;
			}
		} else {
			return false;
		}
		if (Calculator.sessions[Calculator.currentSession] != null) {
			return true;
		}
		return false;
	}

	public void goForward() {
		if (canGoForward()) {
			if (PIDisplay.screen != Calculator.sessions[Calculator.currentSession]) {

			} else {
				Calculator.currentSession -= 1;
			}
			PIDisplay.screen = Calculator.sessions[Calculator.currentSession];
		}
	}

	public Screen getScreen() {
		return PIDisplay.screen;
	}

	private void load_skin() throws IOException {
		BufferedImage img = ImageIO.read(Main.instance.getClass().getResource("/skin.png"));
		skin = getMatrixOfImage(img);
		skinSize = new int[] { img.getWidth(), img.getHeight() };
	}

	private void load_fonts() {
		fonts[0] = new RAWFont();
		fonts[0].create("big");
		fonts[1] = new RAWFont();
		fonts[1].create("small");
		fonts[2] = new RAWFont();
		fonts[2].create("big_2x");
		fonts[3] = new RAWFont();
		fonts[3].create("small_2x");
		setFont(fonts[0]);
	}

	private void draw_init() {
		glClear();
	}
	
	public void drawSkinPart(int x, int y, int sx1, int sy1, int sx2, int sy2) {
		glDrawSkin(skinSize[0], skin, x, y, sx1, sy1, sx2, sy2, false);
	}

	private void draw_status() {
		glColor3f(204, 231, 212);
		glFillRect(0, 0, Main.screenSize[0], 20);
		glColor3f(0, 0, 0);
		glDrawLine(0, 20, Main.screenSize[0]-1, 20);
		glColor3f(0, 0, 0);
		if (Keyboard.shift) {
			drawSkinPart(2 + 18 * 0, 2, 16 * 2, 16 * 0, 16 + 16 * 2, 16 + 16 * 0);
		} else {
			drawSkinPart(2 + 18 * 0, 2, 16 * 3, 16 * 0, 16 + 16 * 3, 16 + 16 * 0);
		}
		if (Keyboard.alpha) {
			drawSkinPart(2 + 18 * 1, 2, 16 * 0, 16 * 0, 16 + 16 * 0, 16 + 16 * 0);
		} else {
			drawSkinPart(2 + 18 * 1, 2, 16 * 1, 16 * 0, 16 + 16 * 1, 16 + 16 * 0);
		}
		if (Calculator.angleMode == "deg") {
			drawSkinPart(8 + 18 * 2, 2, 16 * 4, 16 * 0, 16 + 16 * 4, 16 + 16 * 0);
			drawSkinPart(8 + 18 * 3, 2, 16 * 7, 16 * 0, 16 + 16 * 7, 16 + 16 * 0);
			drawSkinPart(8 + 18 * 4, 2, 16 * 9, 16 * 0, 16 + 16 * 9, 16 + 16 * 0);
		} else if (Calculator.angleMode == "rad") {
			drawSkinPart(8 + 18 * 2, 2, 16 * 5, 16 * 0, 16 + 16 * 5, 16 + 16 * 0);
			drawSkinPart(8 + 18 * 3, 2, 16 * 6, 16 * 0, 16 + 16 * 6, 16 + 16 * 0);
			drawSkinPart(8 + 18 * 4, 2, 16 * 9, 16 * 0, 16 + 16 * 9, 16 + 16 * 0);
		} else if (Calculator.angleMode == "gra") {
			drawSkinPart(8 + 18 * 2, 2, 16 * 5, 16 * 0, 16 + 16 * 5, 16 + 16 * 0);
			drawSkinPart(8 + 18 * 3, 2, 16 * 7, 16 * 0, 16 + 16 * 7, 16 + 16 * 0);
			drawSkinPart(8 + 18 * 4, 2, 16 * 8, 16 * 0, 16 + 16 * 8, 16 + 16 * 0);
		} else {
			drawSkinPart(8 + 18 * 2, 2, 16 * 5, 16 * 0, 16 + 16 * 5, 16 + 16 * 0);
			drawSkinPart(8 + 18 * 3, 2, 16 * 7, 16 * 0, 16 + 16 * 7, 16 + 16 * 0);
			drawSkinPart(8 + 18 * 4, 2, 16 * 9, 16 * 0, 16 + 16 * 9, 16 + 16 * 0);
		}

		int padding = 2;

		int brightness = (int) (Math.ceil(PIDisplay.brightness * 4));
		if (brightness <= 1) {
			drawSkinPart(Main.screenSize[0] - (padding + 16), 2, 16 * 10, 16 * 0, 16 + 16 * 10, 16 + 16 * 0);
		} else if (brightness == 2) {
			drawSkinPart(Main.screenSize[0] - (padding + 16), 2, 16 * 11, 16 * 0, 16 + 16 * 11, 16 + 16 * 0);
		} else if (brightness == 3) {
			drawSkinPart(Main.screenSize[0] - (padding + 16), 2, 16 * 12, 16 * 0, 16 + 16 * 12, 16 + 16 * 0);
		} else if (brightness >= 4) {
			drawSkinPart(Main.screenSize[0] - (padding + 16), 2, 16 * 13, 16 * 0, 16 + 16 * 13, 16 + 16 * 0);
		} else {
			Utils.debug.println("Brightness error");
		}

		padding += 18 + 6;

		boolean canGoBack = canGoBack();
		boolean canGoForward = canGoForward();

		if (Calculator.haxMode) {
			drawSkinPart(Main.screenSize[0] - (padding + 16), 2, 16 * 18, 16 * 0, 16 + 16 * 18, 16 + 16 * 0);
			padding += 18 + 6;
		}

		if (canGoBack && canGoForward) {
			drawSkinPart(Main.screenSize[0] - (padding + 16), 2, 16 * 14, 16 * 0, 16 + 16 * 14, 16 + 16 * 0);
		} else if (canGoBack) {
			drawSkinPart(Main.screenSize[0] - (padding + 16), 2, 16 * 15, 16 * 0, 16 + 16 * 15, 16 + 16 * 0);
		} else if (canGoForward) {
			drawSkinPart(Main.screenSize[0] - (padding + 16), 2, 16 * 16, 16 * 0, 16 + 16 * 16, 16 + 16 * 0);
		} else {
			drawSkinPart(Main.screenSize[0] - (padding + 16), 2, 16 * 17, 16 * 0, 16 + 16 * 17, 16 + 16 * 0);
		}

		padding += 18;

	}

	private void draw_screen() {
		screen.render();
	}

	private void draw_bottom() {
		glDrawStringLeft(2, 90, displayDebugString);
	}

	private void draw_world() {
		glColor3f(255, 255, 255);

		if (error != null) {
			setFont(Utils.getFont(false, false));
			glColor3f(129, 28, 22);
			glDrawStringRight(Main.screenSize[0] - 2, Main.screenSize[1]- this.glyphsHeight[1] - 2, "ANDREA CAVALLI'S CALCULATOR");
			glColor3f(149, 32, 26);
			glDrawStringCenter((Main.screenSize[0] / 2), 22, error);
			glColor3f(164, 34, 28);
			int i = 22;
			for (String stackPart : errorStackTrace) {
				glDrawStringLeft(2, 22 + i, stackPart);
				i += 11;
			}
			setFont(fonts[0]);
			glColor3f(129, 28, 22);
			glDrawStringCenter((Main.screenSize[0] / 2), 11, "UNEXPECTED EXCEPTION");
		} else {
			draw_screen();
			draw_status();
			draw_bottom();
		}
	}

	private void draw() {
		draw_init();
		draw_world();
	}

	private long precTime = -1;

	public void refresh(boolean forced) {
		float dt = 0;
		long newtime = System.nanoTime();
		if (precTime == -1) {
			dt = 0;
		} else {
			dt = (float) ((newtime - precTime) / 1000000000d);
			if (dt < 0.03 && !forced) {
				return;
			}
		}
		precTime = newtime;
		/*
		 * Calcoli
		 */
		checkDisplayResized();

		screen.beforeRender(dt);
		
		if(forced==true || screen.mustBeRefreshed()) {
			draw();
		}

	}

	private void checkDisplayResized() {
		if (Display.wasResized()) {
			Main.screenSize[0] = Display.getWidth();
			Main.screenSize[1]= Display.getHeight();
		}
	};
	
	public void run() {
		try {
			load_skin();
			load_fonts();
			Display.create();
			
			try {
				screen.initialize();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}

			Display.start();
			
			Main.instance.afterStart();

			double extratime = 0;
			while (Display.initialized) {
				long start = System.nanoTime();
				Display.repaint(false);
				long end = System.nanoTime();
				double delta = (end - start) / 1000000000;
				int deltaInt = (int) Math.floor(delta);
				int extraTimeInt = (int) Math.floor(extratime);
				if (extraTimeInt + deltaInt < 50) {
					Thread.sleep(50 - (extraTimeInt + deltaInt));
					extratime = 0;
				} else {
					extratime += delta - 50d;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
	}
	
	public static void changeBrightness(float change) {
		setBrightness(brightness + change);
	}

	public static void setBrightness(float newval) {
		if (newval >= 0.1 && newval <= 1) {
			brightness = newval;
			if (Utils.debugOn == false) {
				Gpio.pwmWrite(12, (int) Math.ceil(brightness*1024));
//				SoftPwm.softPwmWrite(12, (int)(Math.ceil(brightness*10)));
			}
		}
	}

	public static void cycleBrightness(boolean reverse) {
		final float step = reverse?-0.1f:0.1f;
		if (brightness + step > 1f) {
			setBrightness(0.1f);
		} else if (brightness + step < 0.1f) {
			setBrightness(1.0f);
		} else {
			changeBrightness(step);
		}
	}

	public static float getBrightness() {
		return brightness;
	}

	public float[] colore = new float[] { 0.0f, 0.0f, 0.0f, 1.0f };
	
	public static void colore(float f1, float f2, float f3, float f4) {
		PIDisplay.INSTANCE.colore = new float[] { f1, f2, f3, f4 };
		glColor4f((int) (f1 * 255), (int) (f2 * 255), (int) (f3 * 255), (int) (f4 * 255));
	}
}
package org.warp.picalculator.gui;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.warp.picalculator.Main;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.Keyboard;
import org.warp.picalculator.gui.graphicengine.Display;
import org.warp.picalculator.gui.graphicengine.Drawable;
import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.gui.graphicengine.cpu.CPUDisplay;
import org.warp.picalculator.gui.graphicengine.gpu.GPUDisplay;
import org.warp.picalculator.gui.graphicengine.RAWFont;
import org.warp.picalculator.gui.graphicengine.RAWSkin;
import org.warp.picalculator.gui.screens.Screen;

import com.pi4j.wiringpi.Gpio;

public final class DisplayManager implements Drawable {
	public static DisplayManager INSTANCE;
	private static float brightness;

	public static final Display display = chooseGraphicEngine();
	public static Renderer renderer;

	private static RAWSkin skin;
	public static RAWFont[] fonts;

	public static String error = null;
	public String[] errorStackTrace = null;
	public final static int[] glyphsHeight = new int[] { 9, 6, 12, 9 };

	public static Screen screen;
	public static String displayDebugString = "";

	public DisplayManager(Screen screen) {
		setScreen(screen);
		INSTANCE = this;
		loop();
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

	private static Display chooseGraphicEngine() {
		Display d;
		d = new GPUDisplay();
		if (d.isSupported()) return d;
		d = new CPUDisplay();
		if (d.isSupported()) return d;
		throw new UnsupportedOperationException("No graphic engines available.");
	}

	public void setScreen(Screen screen) {
		if (screen.initialized == false) {
			if (screen.canBeInHistory) {
				DisplayManager.currentSession = 0;
				for (int i = DisplayManager.sessions.length - 1; i >= 1; i--) {
					DisplayManager.sessions[i] = DisplayManager.sessions[i - 1];
				}
				DisplayManager.sessions[0] = screen;
			} else {
				DisplayManager.currentSession = -1;
			}
		}
		screen.d = this;
		try {
			screen.create();
			DisplayManager.screen = screen;
			if (screen.initialized == false) {
				screen.initialize();
			}
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void replaceScreen(Screen screen) {
		if (screen.initialized == false) {
			if (screen.canBeInHistory) {
				DisplayManager.sessions[DisplayManager.currentSession] = screen;
			} else {
				DisplayManager.currentSession = -1;
				for (int i = 0; i < DisplayManager.sessions.length - 2; i++) {
					DisplayManager.sessions[i] = DisplayManager.sessions[i + 1];
				}
			}
		}
		screen.d = this;
		try {
			screen.create();
			DisplayManager.screen = screen;
			if (screen.initialized == false) {
				screen.initialize();
			}
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public boolean canGoBack() {
		if (DisplayManager.currentSession == -1) {
			return DisplayManager.sessions[0] != null;
		}
		if (DisplayManager.screen != DisplayManager.sessions[DisplayManager.currentSession]) {

		} else if (DisplayManager.currentSession + 1 < DisplayManager.sessions.length) {
			if (DisplayManager.sessions[DisplayManager.currentSession + 1] != null) {

			} else {
				return false;
			}
		} else {
			return false;
		}
		if (DisplayManager.sessions[DisplayManager.currentSession] != null) {
			return true;
		}
		return false;
	}

	public void goBack() {
		if (canGoBack()) {
			if (DisplayManager.currentSession >= 0 && DisplayManager.screen != DisplayManager.sessions[DisplayManager.currentSession]) {} else {
				DisplayManager.currentSession += 1;
			}
			DisplayManager.screen = DisplayManager.sessions[DisplayManager.currentSession];
		}
	}

	public boolean canGoForward() {
		if (DisplayManager.currentSession <= 0) { // -1 e 0
			return false;
		}
		if (DisplayManager.screen != DisplayManager.sessions[DisplayManager.currentSession]) {

		} else if (DisplayManager.currentSession > 0) {
			if (DisplayManager.sessions[DisplayManager.currentSession - 1] != null) {

			} else {
				return false;
			}
		} else {
			return false;
		}
		if (DisplayManager.sessions[DisplayManager.currentSession] != null) {
			return true;
		}
		return false;
	}

	public void goForward() {
		if (canGoForward()) {
			if (DisplayManager.screen != DisplayManager.sessions[DisplayManager.currentSession]) {

			} else {
				DisplayManager.currentSession -= 1;
			}
			DisplayManager.screen = DisplayManager.sessions[DisplayManager.currentSession];
		}
	}

	public Screen getScreen() {
		return DisplayManager.screen;
	}

	private void load_skin() throws IOException {
		skin = display.loadSkin("skin.png");
	}

	private void load_fonts() throws IOException {
		fonts = new RAWFont[7];
		fonts[0] = display.loadFont("big");
		fonts[1] = display.loadFont("small");
		fonts[2] = display.loadFont("ex");
		fonts[3] = display.loadFont("big");
		fonts[4] = display.loadFont("32");
		fonts[5] = display.loadFont("square");
	}

	private void draw_init() {
		renderer.glClear(display.getWidth(), display.getHeight());
	}

	private void draw_status() {
		renderer.glColor(0xFFc5c2af);
		renderer.glFillColor(0, 0, display.getWidth(), 20);
		renderer.glColor3i(0, 0, 0);
		renderer.glDrawLine(0, 20, display.getWidth() - 1, 20);
		renderer.glColor3i(255, 255, 255);
		skin.use(display);
		if (Keyboard.shift) {
			renderer.glFillRect(2 + 18 * 0, 2, 16, 16, 16 * 2, 16 * 0, 16, 16);
		} else {
			renderer.glFillRect(2 + 18 * 0, 2, 16, 16, 16 * 3, 16 * 0, 16, 16);
		}
		if (Keyboard.alpha) {
			renderer.glFillRect(2 + 18 * 1, 2, 16, 16, 16 * 0, 16 * 0, 16, 16);
		} else {
			renderer.glFillRect(2 + 18 * 1, 2, 16, 16, 16 * 1, 16 * 0, 16, 16);
		}
		/*
		if (Calculator.angleMode == AngleMode.DEG) {
			drawSkinPart(8 + 18 * 2, 2, 16 * 4, 16 * 0, 16 + 16 * 4, 16 + 16 * 0);
			drawSkinPart(8 + 18 * 3, 2, 16 * 7, 16 * 0, 16 + 16 * 7, 16 + 16 * 0);
			drawSkinPart(8 + 18 * 4, 2, 16 * 9, 16 * 0, 16 + 16 * 9, 16 + 16 * 0);
		} else if (Calculator.angleMode == AngleMode.RAD) {
			drawSkinPart(8 + 18 * 2, 2, 16 * 5, 16 * 0, 16 + 16 * 5, 16 + 16 * 0);
			drawSkinPart(8 + 18 * 3, 2, 16 * 6, 16 * 0, 16 + 16 * 6, 16 + 16 * 0);
			drawSkinPart(8 + 18 * 4, 2, 16 * 9, 16 * 0, 16 + 16 * 9, 16 + 16 * 0);
		} else if (Calculator.angleMode == AngleMode.GRA) {
			drawSkinPart(8 + 18 * 2, 2, 16 * 5, 16 * 0, 16 + 16 * 5, 16 + 16 * 0);
			drawSkinPart(8 + 18 * 3, 2, 16 * 7, 16 * 0, 16 + 16 * 7, 16 + 16 * 0);
			drawSkinPart(8 + 18 * 4, 2, 16 * 8, 16 * 0, 16 + 16 * 8, 16 + 16 * 0);
		} else {
			drawSkinPart(8 + 18 * 2, 2, 16 * 5, 16 * 0, 16 + 16 * 5, 16 + 16 * 0);
			drawSkinPart(8 + 18 * 3, 2, 16 * 7, 16 * 0, 16 + 16 * 7, 16 + 16 * 0);
			drawSkinPart(8 + 18 * 4, 2, 16 * 9, 16 * 0, 16 + 16 * 9, 16 + 16 * 0);
		}*/

		int padding = 2;

		final int brightness = (int) (Math.ceil(DisplayManager.brightness * 9));
		if (brightness <= 10) {
			renderer.glFillRect(Main.screenSize[0] - (padding + 16), 2, 16, 16, 16 * brightness, 16 * 1, 16, 16);
		} else {
			Utils.debug.println("Brightness error");
		}

		padding += 18 + 6;

		final boolean canGoBack = canGoBack();
		final boolean canGoForward = canGoForward();

		if (Main.haxMode) {
			renderer.glFillRect(Main.screenSize[0] - (padding + 16), 2, 16, 16, 16 * 18, 16 * 0, 16, 16);
			padding += 18 + 6;
		}

		if (canGoBack && canGoForward) {
			renderer.glFillRect(Main.screenSize[0] - (padding + 16), 2, 16, 16, 16 * 14, 16 * 0, 16, 16);
		} else if (canGoBack) {
			renderer.glFillRect(Main.screenSize[0] - (padding + 16), 2, 16, 16, 16 * 15, 16 * 0, 16, 16);
		} else if (canGoForward) {
			renderer.glFillRect(Main.screenSize[0] - (padding + 16), 2, 16, 16, 16 * 16, 16 * 0, 16, 16);
		} else {
			renderer.glFillRect(Main.screenSize[0] - (padding + 16), 2, 16, 16, 16 * 17, 16 * 0, 16, 16);
		}

		padding += 18;

		screen.renderStatusbar();
	}

	private void draw_screen() {
		screen.render();
	}

	private void draw_bottom() {
		renderer.glDrawStringLeft(2, 90, displayDebugString);

		Utils.getFont(false, true).use(DisplayManager.display);
		DisplayManager.renderer.glColor4i(255, 0, 0, 40);
		DisplayManager.renderer.glDrawStringLeft(5 + 1, Main.screenSize[1] - 20 + 1, "WORK IN PROGRESS.");
		DisplayManager.renderer.glColor4i(255, 0, 0, 80);
		DisplayManager.renderer.glDrawStringLeft(5, Main.screenSize[1] - 20, "WORK IN PROGRESS.");
	}

	private void draw_world() {
		renderer.glColor3i(255, 255, 255);

		if (error != null) {
			Utils.getFont(false, false).use(display);
			renderer.glColor3i(129, 28, 22);
			renderer.glDrawStringRight(Main.screenSize[0] - 2, Main.screenSize[1] - DisplayManager.glyphsHeight[1] - 2, "ANDREA CAVALLI'S CALCULATOR");
			renderer.glColor3i(149, 32, 26);
			renderer.glDrawStringCenter((Main.screenSize[0] / 2), 22, error);
			renderer.glColor3i(164, 34, 28);
			int i = 22;
			for (final String stackPart : errorStackTrace) {
				renderer.glDrawStringLeft(2, 22 + i, stackPart);
				i += 11;
			}
			fonts[0].use(display);
			renderer.glColor3i(129, 28, 22);
			renderer.glDrawStringCenter((Main.screenSize[0] / 2), 11, "UNEXPECTED EXCEPTION");
		} else {
			fonts[0].use(display);
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

	@Override
	public void refresh() {
		float dt = 0;
		final long newtime = System.nanoTime();
		if (precTime == -1) {
			dt = 0;
		} else {
			dt = (float) ((newtime - precTime) / 1000000000d);
		}
		precTime = newtime;
		/*
		 * Calcoli
		 */
		checkDisplayResized();

		screen.beforeRender(dt);

		if (dt >= 0.03 || screen.mustBeRefreshed()) {
			draw();
		}

	}

	private void checkDisplayResized() {
		if (display.wasResized()) {
			Main.screenSize[0] = display.getWidth();
			Main.screenSize[1] = display.getHeight();
		}
	};

	public void loop() {
		try {
			load_skin();
			load_fonts();
			display.create();
			renderer = display.getRenderer();

			try {
				screen.initialize();
			} catch (final Exception e) {
				e.printStackTrace();
				System.exit(0);
			}

			//Debug thread
			Thread dbgthrd = new Thread(() -> {
				try {
					while (true) {
						for (int i = 0; i < 10; i++) {
							System.out.println("============");
							OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
							for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
								method.setAccessible(true);
								if (method.getName().startsWith("get") && Modifier.isPublic(method.getModifiers())) {
									Object value;
									try {
										value = method.invoke(operatingSystemMXBean);
									} catch (Exception e) {
										value = e;
									} // try
									boolean percent = false;
									boolean mb = false;
									String displayName = method.getName();
									String displayValue = value.toString();
									if (displayName.endsWith("CpuLoad")) {
										percent = true;
									}
									if (displayName.endsWith("MemorySize")) {
										mb = true;
									}
									ArrayList<String> arr = new ArrayList<>();
									arr.add("getFreePhysicalMemorySize");
									arr.add("getProcessCpuLoad");
									arr.add("getSystemCpuLoad");
									arr.add("getTotalPhysicalMemorySize");
									if (arr.contains(displayName)) {
										if (percent) {
											try {
												System.out.println(displayName + " = " + (((int)(Float.parseFloat(displayValue) * 10000f))/100f) + "%");
											}catch(Exception ex) {
												System.out.println(displayName + " = " + displayValue);
											}
										} else if (mb) {
											try {
												System.out.println(displayName + " = " + (Long.parseLong(displayValue) / 1024L / 1024L) + " MB");
											}catch(Exception ex) {
												System.out.println(displayName + " = " + displayValue);
											}
										} else {
											System.out.println(displayName + " = " + displayValue);
										}
									}
								} // if
							} // for
							System.out.println("============");
							Thread.sleep(5000);
						}
					}
				} catch (final InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			dbgthrd.setDaemon(true);
			dbgthrd.setName("Debug performance thread");
			dbgthrd.start();
			
			display.start(this);
			
			display.waitUntilExit();
		} catch (final Exception ex) {
			ex.printStackTrace();
		} finally {}
	}

	public static void changeBrightness(float change) {
		setBrightness(brightness + change);
	}

	public static void setBrightness(float newval) {
		if (newval >= 0 && newval <= 1) {
			brightness = newval;
			if (Utils.debugOn == false) {
				Gpio.pwmWrite(12, (int) Math.ceil(brightness * 1024));
//				SoftPwm.softPwmWrite(12, (int)(Math.ceil(brightness*10)));
			}
		}
	}

	public static void cycleBrightness(boolean reverse) {
		final float step = reverse ? -0.1f : 0.1f;
		if (brightness + step > 1f) {
			setBrightness(0f);
		} else if (brightness + step <= 0f) {
			setBrightness(1.0f);
		} else {
			changeBrightness(step);
		}
	}

	public static float getBrightness() {
		return brightness;
	}
	
	public static int currentSession = 0;
	public static Screen[] sessions = new Screen[5];

	@Deprecated
	public static void colore(float f1, float f2, float f3, float f4) {
		renderer.glColor4f(f1,f2,f3,f4);
	}

	public static Drawable getDrawable() {
		return INSTANCE;
	}

	@Deprecated
	public static void drawSkinPart(int x, int y, int uvX, int uvY, int uvX2, int uvY2) {
		renderer.glFillRect(x, y, uvX2-uvX, uvY2-uvY, uvX, uvY, uvX2-uvX, uvY2-uvY);
	}
}
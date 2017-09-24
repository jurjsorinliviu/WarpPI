package org.warp.picalculator.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import org.warp.picalculator.Main;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.Keyboard;
import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.gui.graphicengine.RenderingLoop;
import org.warp.picalculator.gui.graphicengine.Skin;
import org.warp.picalculator.gui.graphicengine.cpu.CPUEngine;
import org.warp.picalculator.gui.graphicengine.gpu.GPUEngine;
import org.warp.picalculator.gui.graphicengine.headless24bit.Headless24bitEngine;
import org.warp.picalculator.gui.graphicengine.headless256.Headless256Engine;
import org.warp.picalculator.gui.graphicengine.headless8.Headless8Engine;
import org.warp.picalculator.gui.screens.Screen;

import com.pi4j.wiringpi.Gpio;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public final class DisplayManager implements RenderingLoop {
	public static DisplayManager INSTANCE;
	private static float brightness;

	public static final GraphicEngine engine;
	public static final boolean supportsPauses;
	public static Renderer renderer;

	public static Skin guiSkin;
	public static BinaryFont[] fonts;

	public static String error;
	public String[] errorStackTrace;
	public final static int[] glyphsHeight;

	private static Screen screen;
	public static Semaphore screenChange = new Semaphore(0);
	public static String displayDebugString;
	public static ObjectArrayList<GUIErrorMessage> errorMessages;
	
	static {
		engine = chooseGraphicEngine();
		supportsPauses = engine.doesRefreshPauses();
		glyphsHeight = new int[] { 9, 6, 12, 9 };
		displayDebugString = "";
		errorMessages = new ObjectArrayList<>();
	}
	
	public static void preInitialization() {
		//Nothing. When called for the first time the static methods will be loaded
	}
	
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

	private static GraphicEngine chooseGraphicEngine() {
		GraphicEngine d;
		d = new GPUEngine();
		if (d.isSupported()) {
			Utils.out.println(1, "Using GPU Graphic Engine");
			return d;
		}
		d = new CPUEngine();
		if (d.isSupported()) {
			Utils.out.println(1, "Using CPU Graphic Engine");
			return d;
		}
		d = new Headless24bitEngine();
		if (d.isSupported()) {
			System.err.println("Using Headless 24 bit Engine! This is a problem! No other graphic engines are available.");
			return d;
		}
		d = new Headless256Engine();
		if (d.isSupported()) {
			System.err.println("Using Headless 256 Engine! This is a problem! No other graphic engines are available.");
			return d;
		}
		d = new Headless8Engine();
		if (d.isSupported()) {
			System.err.println("Using Headless basic Engine! This is a problem! No other graphic engines are available.");
			return d;
		}
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
			screenChange.release();
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
			screenChange.release();
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
			screenChange.release();
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
			screenChange.release();
		}
	}

	public static Screen getScreen() {
		return DisplayManager.screen;
	}

	private void load_skin() throws IOException {
		guiSkin = engine.loadSkin("skin.png");
	}

	private void load_fonts() throws IOException {
		fonts = new BinaryFont[7];
		fonts[0] = engine.loadFont("big");
		fonts[1] = engine.loadFont("small");
		fonts[2] = engine.loadFont("ex");
		fonts[3] = engine.loadFont("big");
		fonts[4] = engine.loadFont("32");
		fonts[5] = engine.loadFont("square");
	}

	private void draw_init() {
		if (engine.supportsFontRegistering()) {
			List<BinaryFont> fontsIterator = engine.getRegisteredFonts();
			for (BinaryFont f : fontsIterator) {
				if (!f.isInitialized()) {
					f.initialize(engine);
				}
			}
		}
		renderer.glClear(engine.getWidth(), engine.getHeight());
	}

	private void draw_status() {
		renderer.glColor(0xFFc5c2af);
		renderer.glFillColor(0, 0, engine.getWidth(), 20);
		renderer.glColor3i(0, 0, 0);
		renderer.glDrawLine(0, 20, engine.getWidth() - 1, 20);
		renderer.glColor3i(255, 255, 255);
		guiSkin.use(engine);
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
			Utils.out.println(1, "Brightness error");
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

		Utils.getFont(true, false).use(DisplayManager.engine);
		DisplayManager.renderer.glColor4i(255, 0, 0, 40);
		DisplayManager.renderer.glDrawStringLeft(1 + 1, Main.screenSize[1] - 7 - 7 + 1, "WORK IN");
		DisplayManager.renderer.glColor4i(255, 0, 0, 80);
		DisplayManager.renderer.glDrawStringLeft(1, Main.screenSize[1] - 7 - 7, "WORK IN");
		DisplayManager.renderer.glColor4i(255, 0, 0, 40);
		DisplayManager.renderer.glDrawStringLeft(1 + 1, Main.screenSize[1] - 7 + 1, "PROGRESS.");
		DisplayManager.renderer.glColor4i(255, 0, 0, 80);
		DisplayManager.renderer.glDrawStringLeft(1, Main.screenSize[1] - 7, "PROGRESS.");
	}

	private void draw_world() {
		renderer.glColor3i(255, 255, 255);

		if (error != null) {
			BinaryFont fnt = Utils.getFont(false, false);
			fnt.use(engine);
			renderer.glColor3i(129, 28, 22);
			renderer.glDrawStringRight(Main.screenSize[0] - 2, Main.screenSize[1] - (fnt.getCharacterHeight() + 2), Main.calculatorNameUPPER + " CALCULATOR");
			renderer.glColor3i(149, 32, 26);
			renderer.glDrawStringCenter((Main.screenSize[0] / 2), 22, error);
			renderer.glColor3i(164, 34, 28);
			int i = 22;
			for (final String stackPart : errorStackTrace) {
				renderer.glDrawStringLeft(2, 22 + i, stackPart);
				i += 11;
			}
			fonts[0].use(engine);
			renderer.glColor3i(129, 28, 22);
			renderer.glDrawStringCenter((Main.screenSize[0] / 2), 11, "UNEXPECTED EXCEPTION");
		} else {
			fonts[0].use(engine);
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
		if (supportsPauses == false || (Keyboard.popRefreshRequest() || screen.mustBeRefreshed())) {
			draw();
		}

	}

	private void checkDisplayResized() {
		if (engine.wasResized()) {
			Main.screenSize[0] = engine.getWidth();
			Main.screenSize[1] = engine.getHeight();
		}
	};

	public void loop() {
		try {
			engine.create();
			renderer = engine.getRenderer();

			load_skin();
			load_fonts();
			
			try {
				screen.initialize();
			} catch (final Exception e) {
				e.printStackTrace();
				System.exit(0);
			}

			//Working thread
			final Thread workThread = new Thread(() -> {
				try {
					while (true) {
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

						Thread.sleep(50);
//						for (int i = 0; i < 10; i++) {
//							System.out.println("============");
//							OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
//							for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
//								method.setAccessible(true);
//								if (method.getName().startsWith("get") && Modifier.isPublic(method.getModifiers())) {
//									Object value;
//									try {
//										value = method.invoke(operatingSystemMXBean);
//									} catch (Exception e) {
//										value = e;
//									} // try
//									boolean percent = false;
//									boolean mb = false;
//									String displayName = method.getName();
//									String displayValue = value.toString();
//									if (displayName.endsWith("CpuLoad")) {
//										percent = true;
//									}
//									if (displayName.endsWith("MemorySize")) {
//										mb = true;
//									}
//									ObjectArrayList<String> arr = new ObjectArrayList<>();
//									arr.add("getFreePhysicalMemorySize");
//									arr.add("getProcessCpuLoad");
//									arr.add("getSystemCpuLoad");
//									arr.add("getTotalPhysicalMemorySize");
//									if (arr.contains(displayName)) {
//										if (percent) {
//											try {
//												System.out.println(displayName + " = " + (((int)(Float.parseFloat(displayValue) * 10000f))/100f) + "%");
//											}catch(Exception ex) {
//												System.out.println(displayName + " = " + displayValue);
//											}
//										} else if (mb) {
//											try {
//												System.out.println(displayName + " = " + (Long.parseLong(displayValue) / 1024L / 1024L) + " MB");
//											}catch(Exception ex) {
//												System.out.println(displayName + " = " + displayValue);
//											}
//										} else {
//											System.out.println(displayName + " = " + displayValue);
//										}
//									}
//								} // if
//							} // for
//							System.out.println("============");
//							Thread.sleep(5000);
//						}
					}
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			});
			workThread.setDaemon(true);
			workThread.setName("Work thread");
			workThread.start();

			engine.start(getDrawable());

			engine.waitUntilExit();
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
				Gpio.pwmWrite(12, (int) Math.ceil(brightness * 1024f));
//				SoftPwm.softPwmWrite(12, (int)(Math.ceil(brightness*10)));
			} else {
				Utils.out.println(1, "Brightness: " + newval);
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
		renderer.glColor4f(f1, f2, f3, f4);
	}

	public static RenderingLoop getDrawable() {
		return INSTANCE;
	}

	@Deprecated
	public static void drawSkinPart(int x, int y, int uvX, int uvY, int uvX2, int uvY2) {
		renderer.glFillRect(x, y, uvX2 - uvX, uvY2 - uvY, uvX, uvY, uvX2 - uvX, uvY2 - uvY);
	}
}
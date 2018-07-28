package org.warp.picalculator.gui;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.warp.picalculator.ConsoleUtils;
import org.warp.picalculator.PlatformUtils;
import org.warp.picalculator.StaticVars;
import org.warp.picalculator.Utils;
import org.warp.picalculator.deps.DEngine;
import org.warp.picalculator.deps.DSemaphore;
import org.warp.picalculator.deps.DSystem;
import org.warp.picalculator.device.HardwareDevice;
import org.warp.picalculator.device.Keyboard;
import org.warp.picalculator.event.TouchCancelEvent;
import org.warp.picalculator.event.TouchEndEvent;
import org.warp.picalculator.event.TouchEventListener;
import org.warp.picalculator.event.TouchMoveEvent;
import org.warp.picalculator.event.TouchStartEvent;
import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.gui.graphicengine.RenderingLoop;
import org.warp.picalculator.gui.graphicengine.Skin;
import org.warp.picalculator.gui.graphicengine.nogui.NoGuiEngine;
import org.warp.picalculator.gui.screens.Screen;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public final class DisplayManager implements RenderingLoop, TouchEventListener {
	private HardwareDevice device;
	private float brightness;

	public final GraphicEngine engine;
	public final HardwareDisplay monitor;
	public final boolean supportsPauses;
	public Renderer renderer;

	public Skin guiSkin;
	public BinaryFont[] fonts;

	public String error;
	public String[] errorStackTrace;
	public final int[] glyphsHeight;

	private Screen screen;
	private final HUD hud;
	private final String initialTitle;
	private final Screen initialScreen;
	public DSemaphore screenChange = new DSemaphore(0);
	public String displayDebugString;
	public ObjectArrayList<GUIErrorMessage> errorMessages;
	/**
	 * Set to true when an event is fired
	 */
	private boolean forceRefresh;

	public DisplayManager(HardwareDisplay monitor, HUD hud, Screen screen, String title) {
		engine = chooseGraphicEngine();
		supportsPauses = engine.doesRefreshPauses();

		this.monitor = monitor;
		this.hud = hud;
		this.initialTitle = title;
		this.initialScreen = screen;

		glyphsHeight = new int[] { 9, 6, 12, 9 };
		displayDebugString = "";
		errorMessages = new ObjectArrayList<>();
	}
	
	public void initialize() {
		monitor.initialize();

		try {
			hud.d = this;
			hud.create();
			if (!hud.initialized) {
				hud.initialize();
			}
		} catch (final Exception e) {
			e.printStackTrace();
			DSystem.exit(0);
		}

		setScreen(initialScreen);
		try {
			engine.create();
			renderer = engine.getRenderer();
			engine.setTitle(initialTitle);
			loop();
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
		monitor.shutdown();
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

	private GraphicEngine chooseGraphicEngine() {
		GraphicEngine d;
		if (!StaticVars.debugOn) {
			d = DEngine.newFBEngine();
			if (d != null && d.isSupported()) {
				ConsoleUtils.out.println(1, "Using FB Graphic Engine");
				return d;
			}
		}
		d = DEngine.newGPUEngine();
		if (d != null && d.isSupported()) {
			ConsoleUtils.out.println(1, "Using GPU Graphic Engine");
			return d;
		}
		d = DEngine.newCPUEngine();
		if (d != null && d.isSupported()) {
			ConsoleUtils.out.println(1, "Using CPU Graphic Engine");
			return d;
		}
		d = DEngine.newHeadless24bitEngine();
		if (d != null && d.isSupported()) {
			System.err.println("Using Headless 24 bit Engine! This is a problem! No other graphic engines are available.");
			return d;
		}
		d = DEngine.newHeadless256Engine();
		if (d != null && d.isSupported()) {
			System.err.println("Using Headless 256 Engine! This is a problem! No other graphic engines are available.");
			return d;
		}
		d = DEngine.newHeadless8Engine();
		if (d != null && d.isSupported()) {
			System.err.println("Using Headless basic Engine! This is a problem! No other graphic engines are available.");
			return d;
		}
		d = DEngine.newHtmlEngine();
		if (d != null && d.isSupported()) {
			ConsoleUtils.out.println(ConsoleUtils.OUTPUTLEVEL_NODEBUG, "Using Html Graphic Engine");
			return d;
		}
		d = new NoGuiEngine();
		if (d != null && d.isSupported()) {
			ConsoleUtils.out.println(1, "Using NoGui Graphic Engine");
			return d;
		}
		throw new UnsupportedOperationException("No graphic engines available.");
	}

	public void setScreen(Screen screen) {
		if (screen.initialized == false) {
			if (screen.canBeInHistory) {
				if (this.currentSession > 0) {
					final int sl = this.sessions.length + 5; //TODO: I don't know why if i don't add +5 or more some items disappear
					this.sessions = Arrays.copyOfRange(this.sessions, this.currentSession, sl);
				}
				this.currentSession = 0;
				for (int i = this.sessions.length - 1; i >= 1; i--) {
					this.sessions[i] = this.sessions[i - 1];
				}
				this.sessions[0] = screen;
			} else {
				this.currentSession = -1;
			}
		}
		screen.d = this;
		try {
			screen.create();
			this.screen = screen;
			screenChange.release();
			if (screen.initialized == false) {
				screen.initialize();
			}
		} catch (final Exception e) {
			e.printStackTrace();
			DSystem.exit(0);
		}
	}

	public void replaceScreen(Screen screen) {
		if (screen.initialized == false) {
			if (screen.canBeInHistory) {
				this.sessions[this.currentSession] = screen;
			} else {
				this.currentSession = -1;
				for (int i = 0; i < this.sessions.length - 2; i++) {
					this.sessions[i] = this.sessions[i + 1];
				}
			}
		}
		screen.d = this;
		try {
			screen.create();
			this.screen = screen;
			screenChange.release();
			if (screen.initialized == false) {
				screen.initialize();
			}
		} catch (final Exception e) {
			e.printStackTrace();
			DSystem.exit(0);
		}
	}

	public boolean canGoBack() {
		if (this.currentSession == -1) {
			return this.sessions[0] != null;
		}
		if (this.screen != this.sessions[this.currentSession]) {

		} else if (this.currentSession + 1 < this.sessions.length) {
			if (this.sessions[this.currentSession + 1] != null) {

			} else {
				return false;
			}
		} else {
			return false;
		}
		if (this.sessions[this.currentSession] != null) {
			return true;
		}
		return false;
	}

	public void goBack() {
		if (canGoBack()) {
			if (this.currentSession >= 0 && this.screen != this.sessions[this.currentSession]) {} else {
				this.currentSession += 1;
			}
			this.screen = this.sessions[this.currentSession];
			screenChange.release();
		}
	}

	public boolean canGoForward() {
		if (this.currentSession <= 0) { // -1 e 0
			return false;
		}
		if (this.screen != this.sessions[this.currentSession]) {

		} else if (this.currentSession > 0) {
			if (this.sessions[this.currentSession - 1] != null) {

			} else {
				return false;
			}
		} else {
			return false;
		}
		if (this.sessions[this.currentSession] != null) {
			return true;
		}
		return false;
	}

	public void goForward() {
		if (canGoForward()) {
			if (this.screen != this.sessions[this.currentSession]) {

			} else {
				this.currentSession -= 1;
			}
			this.screen = this.sessions[this.currentSession];
			screenChange.release();
		}
	}

	public Screen getScreen() {
		return screen;
	}

	public HUD getHUD() {
		return hud;
	}

	private void load_skin() throws IOException {
		guiSkin = engine.loadSkin("skin.png");
	}

	private void load_fonts() throws IOException {
		fonts = new BinaryFont[7];
		fonts[0] = engine.loadFont("smal");
		fonts[1] = engine.loadFont("smallest");
		fonts[2] = engine.loadFont("norm");
		fonts[3] = engine.loadFont("smal");
		//4
		//fonts[5] = engine.loadFont("square");
	}

	private void draw_init() {
		if (engine.supportsFontRegistering()) {
			final List<BinaryFont> fontsIterator = engine.getRegisteredFonts();
			for (final BinaryFont f : fontsIterator) {
				if (!f.isInitialized()) {
					f.initialize(engine);
				}
			}
		}
		renderer.glClear(engine.getWidth(), engine.getHeight());
	}

	private void draw_world() {
		renderer.glColor3i(255, 255, 255);

		if (error != null) {
			final BinaryFont fnt = Utils.getFont(false, false);
			if (fnt != null && fnt != engine.getRenderer().getCurrentFont()) {
				fnt.use(engine);
			}
			renderer.glColor3i(129, 28, 22);
			renderer.glDrawStringRight(StaticVars.screenSize[0] - 2, StaticVars.screenSize[1] - (fnt.getCharacterHeight() + 2), StaticVars.calculatorNameUPPER + " CALCULATOR");
			renderer.glColor3i(149, 32, 26);
			renderer.glDrawStringCenter((StaticVars.screenSize[0] / 2), 22, error);
			renderer.glColor3i(164, 34, 28);
			int i = 22;
			for (final String stackPart : errorStackTrace) {
				renderer.glDrawStringLeft(2, 22 + i, stackPart);
				i += 11;
			}
			if (fonts[0] != null && fonts[0] != engine.getRenderer().getCurrentFont()) {
				fonts[0].use(engine);
			}
			renderer.glColor3i(129, 28, 22);
			renderer.glDrawStringCenter((StaticVars.screenSize[0] / 2), 11, "UNEXPECTED EXCEPTION");
		} else {
			if (fonts[0] != null && fonts[0] != engine.getRenderer().getCurrentFont()) {
				fonts[0].use(engine);
			}
			hud.renderBackground();
			screen.render();
			hud.render();
			hud.renderTopmostBackground();
			screen.renderTopmost();
			hud.renderTopmost();
		}
	}

	private void draw() {
		draw_init();
		draw_world();
	}

	private long precTime = -1;

	@Override
	public void refresh() {
		if (supportsPauses == false || (Keyboard.popRefreshRequest() || forceRefresh || screen.mustBeRefreshed())) {
			forceRefresh = false;
			draw();
		}

	}

	private void checkDisplayResized() {
		if (engine.wasResized()) {
			StaticVars.screenSize[0] = engine.getWidth();
			StaticVars.screenSize[1] = engine.getHeight();
		}
	};

	public void loop() {
		try {
			load_skin();
			load_fonts();

			try {
				screen.initialize();
			} catch (final Exception e) {
				e.printStackTrace();
				DSystem.exit(0);
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
			PlatformUtils.setDaemon(workThread);
			PlatformUtils.setThreadName(workThread, "Work thread");
			workThread.start();

			engine.start(getDrawable());
		} catch (final Exception ex) {
			ex.printStackTrace();
		} finally {}
	}

	public void changeBrightness(float change) {
		setBrightness(brightness + change);
	}

	public void setBrightness(float newval) {
		if (newval >= 0 && newval <= 1) {
			brightness = newval;
			monitor.setBrightness(brightness);
		}
	}

	public void cycleBrightness(boolean reverse) {
		final float step = reverse ? -0.1f : 0.1f;
		if (brightness + step > 1f) {
			setBrightness(0f);
		} else if (brightness + step <= 0f) {
			setBrightness(1.0f);
		} else {
			changeBrightness(step);
		}
	}

	public float getBrightness() {
		return brightness;
	}

	public int currentSession = 0;
	public Screen[] sessions = new Screen[5];

	@Deprecated
	public void colore(float f1, float f2, float f3, float f4) {
		renderer.glColor4f(f1, f2, f3, f4);
	}

	public RenderingLoop getDrawable() {
		return this;
	}

	@Deprecated
	public void drawSkinPart(int x, int y, int uvX, int uvY, int uvX2, int uvY2) {
		renderer.glFillRect(x, y, uvX2 - uvX, uvY2 - uvY, uvX, uvY, uvX2 - uvX, uvY2 - uvY);
	}

	public void waitForExit() {
		engine.waitForExit();
	}
	
	@Override
	public boolean onTouchStart(TouchStartEvent e) {
		final Screen scr = getScreen();
		boolean refresh = false;
		if (scr != null && scr.initialized && scr.onTouchStart(e)) {
			refresh = true;
		} else {
			//Default behavior
		}
		if (refresh) {
			forceRefresh = true;
		}
		return true;
	}

	@Override
	public boolean onTouchEnd(TouchEndEvent e) {
		final Screen scr = getScreen();
		boolean refresh = false;
		if (scr != null && scr.initialized && scr.onTouchEnd(e)) {
			refresh = true;
		} else {
			//Default behavior
		}
		if (refresh) {
			forceRefresh = true;
		}
		return true;
	}

	@Override
	public boolean onTouchCancel(TouchCancelEvent e) {
		final Screen scr = getScreen();
		boolean refresh = false;
		if (scr != null && scr.initialized && scr.onTouchCancel(e)) {
			refresh = true;
		} else {
			//Default behavior
		}
		if (refresh) {
			forceRefresh = true;
		}
		return true;
	}

	@Override
	public boolean onTouchMove(TouchMoveEvent e) {
		final Screen scr = getScreen();
		boolean refresh = false;
		if (scr != null && scr.initialized && scr.onTouchMove(e)) {
			refresh = true;
		} else {
			//Default behavior
		}
		if (refresh) {
			forceRefresh = true;
		}
		return true;
	}
}
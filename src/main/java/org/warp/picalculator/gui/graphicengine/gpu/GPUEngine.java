package org.warp.picalculator.gui.graphicengine.gpu;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;

import org.warp.picalculator.StaticVars;
import org.warp.picalculator.Utils;
import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.RenderingLoop;
import org.warp.picalculator.gui.graphicengine.Skin;

import com.jogamp.opengl.GLProfile;

public class GPUEngine implements GraphicEngine {

	private volatile boolean initialized = false;
	private volatile boolean created = false;
	private NEWTWindow wnd;
	private RenderingLoop d;
	private GPURenderer r;
	private Map<String, GPUFont> fontCache = new HashMap<String, GPUFont>();
	int[] size = new int[] { StaticVars.screenSize[0], StaticVars.screenSize[1] };
	private final CopyOnWriteArrayList<BinaryFont> registeredFonts = new CopyOnWriteArrayList<BinaryFont>();
	private Semaphore exitSemaphore = new Semaphore(0);

	@Override
	public int[] getSize() {
		return size;
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}

	@Override
	public void setTitle(String title) {
		wnd.window.setTitle(title);
	}

	@Override
	public void setResizable(boolean r) {
		if (!r) {
			wnd.window.setPosition(0, 0);
		}
		wnd.window.setResizable(r);
		wnd.window.setUndecorated(!r);
		wnd.window.setPointerVisible(r);
	}

	@Override
	public void setDisplayMode(int ww, int wh) {
		size[0] = ww;
		size[1] = wh;
		wnd.window.setSize((StaticVars.debugOn & StaticVars.debugWindow2x) ? ww * 2 : ww, (StaticVars.debugOn & StaticVars.debugWindow2x) ? wh * 2 : wh);
	}

	@Override
	public void create() {
		create(null);
	}

	@Override
	public void create(Runnable onInitialized) {
		created = true;
		r = new GPURenderer();
		wnd = new NEWTWindow(this);
		wnd.create();
		setDisplayMode(StaticVars.screenSize[0], StaticVars.screenSize[1]);
		setResizable(StaticVars.debugOn & !Utils.debugThirdScreen);
		initialized = true;
		wnd.onInitialized = onInitialized;
	}

	@Override
	public boolean wasResized() {
		return StaticVars.screenSize[0] != size[0] | StaticVars.screenSize[1] != size[1];
	}

	@Override
	public int getWidth() {
		return size[0];
	}

	@Override
	public int getHeight() {
		return size[1];
	}

	@Override
	public void destroy() {
		initialized = false;
		created = false;
		exitSemaphore.release();
		wnd.window.destroy();
	}

	@Override
	public void start(RenderingLoop d) {
		this.d = d;
		wnd.window.setVisible(true);
	}

	@Override
	public void repaint() {
		if (d != null & r != null && GPURenderer.gl != null) {
			d.refresh();
		}
	}

	@Override
	public GPURenderer getRenderer() {
		return r;
	}

	@Override
	public BinaryFont loadFont(String file) throws IOException {
		for (Entry<String, GPUFont> entry : fontCache.entrySet()) {
			if (entry.getKey().equals(file)) {
				return entry.getValue();
			}
		}
		GPUFont font = new GPUFont(this, file);
		fontCache.put(file, font);
		return font;
	}

	@Override
	public Skin loadSkin(String file) throws IOException {
		return new GPUSkin(this, file);
	}

	@Override
	public void waitForExit() {
		try {
			exitSemaphore.acquire();
		} catch (InterruptedException e) {}
	}

	@Override
	public boolean isSupported() {
		if (Utils.forceEngine != null && Utils.forceEngine != "gpu")
			return false;
		if (Utils.headlessOverride)
			return false;
		boolean available = false;
		boolean errored = false;
		try {
			available = GLProfile.isAvailable(GLProfile.GL2ES2);
		} catch (Exception ex) {
			errored = true;
			System.err.println(ex.getMessage());
		}
		if (!available && !errored) {
			System.err.println(GLProfile.glAvailabilityToString());
		}
		return available;
	}

	@Override
	public boolean doesRefreshPauses() {
		return false;
	}

	public void registerFont(GPUFont gpuFont) {
		registeredFonts.add(gpuFont);
	}

	@Override
	public boolean supportsFontRegistering() {
		return true;
	}

	@Override
	public CopyOnWriteArrayList<BinaryFont> getRegisteredFonts() {
		return registeredFonts;
	}

}

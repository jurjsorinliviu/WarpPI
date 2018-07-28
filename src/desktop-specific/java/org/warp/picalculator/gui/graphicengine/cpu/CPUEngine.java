package org.warp.picalculator.gui.graphicengine.cpu;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Semaphore;

import org.warp.picalculator.StaticVars;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.HardwareDevice;
import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.RenderingLoop;
import org.warp.picalculator.gui.graphicengine.Skin;

public class CPUEngine implements GraphicEngine {

	private SwingWindow INSTANCE;
	public final CPURenderer r = new CPURenderer();
	public BufferedImage g = new BufferedImage(r.size[0], r.size[1], BufferedImage.TYPE_INT_RGB);
	public volatile boolean initialized = false;
	public Semaphore exitSemaphore = new Semaphore(0);

	@Override
	public void setTitle(String title) {
		INSTANCE.setTitle(title);
	}

	@Override
	public void setResizable(boolean r) {
		INSTANCE.setResizable(r);
		if (!r) {
			INSTANCE.setUndecorated(true);
		}
	}

	@Override
	public void setDisplayMode(final int ww, final int wh) {
		INSTANCE.setSize(ww, wh);
		r.size = new int[] { ww, wh };
		CPURenderer.canvas2d = new int[ww * wh];
		g = new BufferedImage(ww, wh, BufferedImage.TYPE_INT_ARGB);
		INSTANCE.wasResized = false;
	}

	@Override
	public void create() {
		create(null);
	}

	@Override
	public void create(Runnable onInitialized) {
		INSTANCE = new SwingWindow(this);
		setResizable(StaticVars.debugOn & !Utils.debugThirdScreen);
		setDisplayMode(StaticVars.screenSize[0], StaticVars.screenSize[1]);
		INSTANCE.setVisible(true);
		initialized = true;
		if (onInitialized != null) {
			onInitialized.run();
		}
	}

	@Override
	public boolean wasResized() {
		if (INSTANCE.wasResized) {
			r.size = new int[] { INSTANCE.getWidth(), INSTANCE.getHeight() };
			if (r.size[0] <= 0) {
				r.size[0] = 1;
			}
			if (r.size[1] <= 0) {
				r.size[1] = 1;
			}
			CPURenderer.canvas2d = new int[r.size[0] * r.size[1]];
			g = new BufferedImage(r.size[0], r.size[1], BufferedImage.TYPE_INT_ARGB);
			INSTANCE.wasResized = false;
			return true;
		}
		return false;
	}

	@Override
	public int getWidth() {
		return INSTANCE.getWidth() - StaticVars.screenPos[0];
	}

	@Override
	public int getHeight() {
		return INSTANCE.getHeight() - StaticVars.screenPos[1];
	}

	@Override
	public void destroy() {
		initialized = false;
		exitSemaphore.release();
		INSTANCE.setVisible(false);
		INSTANCE.dispose();
	}

	@Override
	public void start(RenderingLoop d) {
		INSTANCE.setRenderingLoop(d);
		final Thread th = new Thread(() -> {
			try {
				double extratime = 0;
				while (initialized) {
					final long start = System.currentTimeMillis();
					repaint();
					final long end = System.currentTimeMillis();
					final double delta = (end - start) / 1000d;
					final int deltaInt = (int) Math.floor(delta);
					final int extraTimeInt = (int) Math.floor(extratime);
					if (extraTimeInt + deltaInt < 50) {
						Thread.sleep(50 - (extraTimeInt + deltaInt));
						extratime = 0;
					} else {
						extratime += delta - 50d;
					}
				}
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		});
		th.setName("CPU rendering thread");
		th.setDaemon(true);
		th.start();
	}

	@Deprecated()
	public void refresh() {
		if (HardwareDevice.INSTANCE.getDisplayManager().getScreen() == null || (HardwareDevice.INSTANCE.getDisplayManager().error != null && HardwareDevice.INSTANCE.getDisplayManager().error.length() > 0) || HardwareDevice.INSTANCE.getDisplayManager().getScreen() == null || HardwareDevice.INSTANCE.getDisplayManager().getScreen().mustBeRefreshed()) {
			INSTANCE.c.repaint();
		}
	}

	@Override
	public void repaint() {
		INSTANCE.c.repaint();
	}

	public abstract class Startable {
		public Startable() {
			force = false;
		}

		public Startable(boolean force) {
			this.force = force;
		}

		public boolean force = false;

		public abstract void run();
	}

	@Override
	public int[] getSize() {
		return r.size;
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}

	@Override
	public CPURenderer getRenderer() {
		return r;
	}

	@Override
	public BinaryFont loadFont(String fontName) throws IOException {
		return new CPUFont(fontName);
	}

	@Override
	public BinaryFont loadFont(String path, String fontName) throws IOException {
		return new CPUFont(path, fontName);
	}

	@Override
	public Skin loadSkin(String file) throws IOException {
		return new CPUSkin(file);
	}

	@Override
	public void waitForExit() {
		try {
			exitSemaphore.acquire();
		} catch (final InterruptedException e) {}
	}

	@Override
	public boolean isSupported() {
		if (Utils.forceEngine != null && Utils.forceEngine != "cpu") {
			return false;
		}
		return (Utils.headlessOverride || GraphicsEnvironment.isHeadless()) == false;
	}

	@Override
	public boolean doesRefreshPauses() {
		return true;
	}
}

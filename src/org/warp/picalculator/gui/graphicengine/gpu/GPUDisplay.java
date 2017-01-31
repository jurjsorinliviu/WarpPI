package org.warp.picalculator.gui.graphicengine.gpu;

import java.io.IOException;

import org.warp.picalculator.Main;
import org.warp.picalculator.Utils;
import org.warp.picalculator.gui.graphicengine.Drawable;
import org.warp.picalculator.gui.graphicengine.RAWFont;
import org.warp.picalculator.gui.graphicengine.RAWSkin;

import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.egl.EGL;

public class GPUDisplay implements org.warp.picalculator.gui.graphicengine.Display {

	private volatile boolean initialized = false;
	private volatile boolean created = false;
	private NEWTWindow wnd;
	private Drawable d;
	private GPURenderer r;
	int[] size = new int[]{Main.screenSize[0], Main.screenSize[1]};

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
		this.size[0] = ww;
		this.size[1] = wh;
		wnd.window.setSize(ww, wh);
	}

	@Override
	public void create() {
		created = true;
		r = new GPURenderer();
		wnd = new NEWTWindow(this);
		wnd.create();
		setDisplayMode(Main.screenSize[0], Main.screenSize[1]);
		setResizable(Utils.debugOn & !Utils.debugThirdScreen);
		initialized = true;
	}

	@Override
	public boolean wasResized() {
		return Main.screenSize[0] != size[0] | Main.screenSize[1] != size[1];
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
		wnd.window.destroy();
	}

	@Override
	public void start(Drawable d) {
		this.d = d;
		wnd.window.setVisible(true);
	}

	@Override
	public void repaint() {
		if (d != null & r != null && r.gl != null) {
			d.refresh();
		}
	}

	@Override
	public GPURenderer getRenderer() {
		return r;
	}

	@Override
	public RAWFont loadFont(String file) throws IOException {
		return new GPUFont(file);
	}

	@Override
	public RAWSkin loadSkin(String file) throws IOException {
		return new GPUSkin(file);
	}

	@Override
	public void waitUntilExit() {
		try {
			do {
				Thread.sleep(500);
			} while(initialized | created);
		} catch (InterruptedException e) {
			
		}
	}

	@Override
	public boolean isSupported() {
		return GLProfile.isAnyAvailable() == false;
	}

}

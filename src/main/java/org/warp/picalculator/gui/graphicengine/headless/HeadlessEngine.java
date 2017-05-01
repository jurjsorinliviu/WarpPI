package org.warp.picalculator.gui.graphicengine.headless;

import java.io.IOException;

import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.gui.graphicengine.RenderingLoop;
import org.warp.picalculator.gui.graphicengine.Skin;

public class HeadlessEngine implements org.warp.picalculator.gui.graphicengine.GraphicEngine {

	private HeadlessRenderer r = new HeadlessRenderer();
	private boolean stopped;
	
	@Override
	public int[] getSize() {
		return new int[]{480, 320};
	}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public void setTitle(String title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResizable(boolean r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDisplayMode(int ww, int wh) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean wasResized() {
		return false;
	}

	@Override
	public int getWidth() {
		return 480;
	}

	@Override
	public int getHeight() {
		return 320;
	}

	@Override
	public void destroy() {
		stopped = true;
	}

	@Override
	public void start(RenderingLoop d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void repaint() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Renderer getRenderer() {
		return r;
	}

	@Override
	public HeadlessFont loadFont(String file) throws IOException {
		return new HeadlessFont();
	}

	@Override
	public HeadlessSkin loadSkin(String file) throws IOException {
		return new HeadlessSkin();
	}

	@Override
	public void waitUntilExit() {
		try {
			do {
				Thread.sleep(500);
			} while (stopped==false);
		} catch (final InterruptedException e) {

		}
	}

	@Override
	public boolean isSupported() {
		return true;
	}

	@Override
	public boolean doesRefreshPauses() {
		return false;
	}
}

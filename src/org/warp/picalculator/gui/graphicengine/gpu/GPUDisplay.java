package org.warp.picalculator.gui.graphicengine.gpu;

import java.awt.image.BufferedImage;

import org.warp.picalculator.Main;
import org.warp.picalculator.Utils;
import org.warp.picalculator.gui.graphicengine.Drawable;
import org.warp.picalculator.gui.graphicengine.Renderer;

public class GPUDisplay implements org.warp.picalculator.gui.graphicengine.Display {

	private boolean initialized = false;
	private CalculatorWindow wnd;
	private Drawable d;
	private GPURenderer r;
	
	@Override
	public int[] getSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}

	@Override
	public void setTitle(String title) {
		// TODO Auto-generated method stub
		
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
		wnd.window.setSize(ww, wh);
	}

	@Override
	public void create() {
		r = new GPURenderer();
		wnd = new CalculatorWindow(this);
		wnd.create();
		setDisplayMode(Main.screenSize[0], Main.screenSize[1]);
		setResizable(Utils.debugOn&!Utils.debugThirdScreen);
		initialized = true;
	}

	@Override
	public boolean wasResized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void destroy() {
//		EGL.destroy(); (Automatic)
	}

	@Override
	public void start(Drawable d) {
        this.d = d;
        wnd.window.setVisible(true);
	}

	@Override
	public void repaint() {
		if (d != null) {
			r.gl.glClearColor(red, green, blue, alpha);
			d.refresh();
			r.glFlush();
		}
	}

	@Override
	public GPURenderer getRenderer() {
		return r;
	}

}

package org.warp.picalculator.gui.graphicengine;

import java.io.IOException;

public interface GraphicEngine {

	public int[] getSize();

	public boolean isInitialized();

	public void setTitle(String title);

	public void setResizable(boolean r);

	public void setDisplayMode(final int ww, final int wh);

	public void create();

	public boolean wasResized();

	public int getWidth();

	public int getHeight();

	public void destroy();

	public void start(RenderingLoop d);

	public void repaint();

	public Renderer getRenderer();

	public BinaryFont loadFont(String file) throws IOException;

	public Skin loadSkin(String file) throws IOException;

	public void waitUntilExit();

	public boolean isSupported();

	public boolean doesRefreshPauses();
}

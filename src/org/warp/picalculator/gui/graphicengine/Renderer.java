package org.warp.picalculator.gui.graphicengine;

import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

public interface Renderer {
	public void glColor3i(int r, int gg, int b);

	public void glColor(int c);

	public int glGetClearColor();

	public void glClearColor(int c);

	public void glColor4i(int red, int green, int blue, int alpha);

	public void glClearColor(int red, int green, int blue, int alpha);
	
	public void glClear();

	public void glDrawSkin(int skinwidth, int[] skin, int x0, int y0, int s0, int t0, int s1, int t1, boolean transparent);

	public void glDrawLine(int x0, int y0, int x1, int y1);

	public void glFillRect(int x0, int y0, int w1, int h1);

	@Deprecated
	public int[] getMatrixOfImage(BufferedImage bufferedImage);

	public void glDrawStringLeft(int x, int y, String text);

	public void glDrawStringCenter(int x, int y, String text);

	public void glDrawStringRight(int x, int y, String text);

	public void glSetFont(RAWFont font);

	public int glGetStringWidth(RAWFont rf, String text);

	public int glGetFontWidth(FontMetrics fm, String text);

	public RAWFont getCurrentFont();
}
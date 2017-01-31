package org.warp.picalculator.gui.graphicengine;

import java.awt.FontMetrics;

public interface Renderer {
	public void glColor3i(int r, int gg, int b);

	public void glColor(int c);

	public void glColor4i(int red, int green, int blue, int alpha);

	public void glColor3f(float red, float green, float blue);

	public void glColor4f(float red, float green, float blue, float alpha);

	public void glClearColor4i(int red, int green, int blue, int alpha);

	public void glClearColor4f(float red, float green, float blue, float alpha);

	public int glGetClearColor();

	public void glClearColor(int c);

	public void glClear(int screenWidth, int screenHeight);

	public void glDrawLine(int x0, int y0, int x1, int y1);

	public void glFillRect(int x, int y, int width, int height, float uvX, float uvY, float uvWidth, float uvHeight);

	public void glFillColor(int x, int y, int width, int height);

	public void glDrawStringLeft(int x, int y, String text);

	public void glDrawStringCenter(int x, int y, String text);

	public void glDrawStringRight(int x, int y, String text);
	
	public void glClearSkin();

	public RAWFont getCurrentFont();
}
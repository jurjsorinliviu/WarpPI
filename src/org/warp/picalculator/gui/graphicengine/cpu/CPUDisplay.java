package org.warp.picalculator.gui.graphicengine.cpu;

import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

import org.warp.picalculator.Main;
import org.warp.picalculator.Utils;
import org.warp.picalculator.gui.PIDisplay;
import org.warp.picalculator.gui.graphicengine.Display;
import org.warp.picalculator.gui.graphicengine.Drawable;
import org.warp.picalculator.gui.graphicengine.RAWFont;
import org.warp.picalculator.gui.graphicengine.Renderer;

public class CPUDisplay implements Display {

	private static SwingWindow INSTANCE;
	public static int[] size = new int[] { 1, 1 };
	public static BufferedImage g = new BufferedImage(size[0], size[1], BufferedImage.TYPE_INT_RGB);
	static int[] canvas2d = new int[1];
	public static int color = 0xFF000000;
	public static boolean initialized = false;

	public void setTitle(String title) {
		INSTANCE.setTitle(title);
	}

	public void setResizable(boolean r) {
		INSTANCE.setResizable(r);
		if (!r)
			INSTANCE.setUndecorated(true);
	}

	public void setDisplayMode(final int ww, final int wh) {
		INSTANCE.setSize(ww, wh);
		size = new int[] { ww, wh };
		canvas2d = new int[ww * wh];
		g = new BufferedImage(ww, wh, BufferedImage.TYPE_INT_ARGB);
		INSTANCE.wasResized = false;
	}

	public void create() {
		INSTANCE = new SwingWindow(PIDisplay.getDrawable());
		setResizable(Utils.debugOn&!Utils.debugThirdScreen);
		setDisplayMode(Main.screenSize[0], Main.screenSize[1]);
		INSTANCE.setVisible(true);
		initialized = true;
	}
	
	public boolean wasResized() {
		if (INSTANCE.wasResized) {
			size = new int[] { INSTANCE.getWidth(), INSTANCE.getHeight() };
			canvas2d = new int[size[0] * size[1]];
			g = new BufferedImage(size[0], size[1], BufferedImage.TYPE_INT_ARGB);
			INSTANCE.wasResized = false;
			return true;
		}
		return false;
	}

	public int getWidth() {
		return INSTANCE.getWidth()-Main.screenPos[0];
	}

	public int getHeight() {
		return INSTANCE.getHeight()-Main.screenPos[1];
	}

	public void destroy() {
		initialized = false;
		INSTANCE.setVisible(false);
		INSTANCE.dispose();
	}

	public void start(Drawable d) {
	}

	@Deprecated()
	public void refresh() {
		if (PIDisplay.screen == null || (PIDisplay.error != null && PIDisplay.error.length() > 0) || PIDisplay.screen == null || PIDisplay.screen.mustBeRefreshed()) {
			CPUDisplay.INSTANCE.c.repaint();
		}
	}

	public void repaint() {
		CPUDisplay.INSTANCE.c.repaint();
	}

	public abstract class Startable {
		public Startable() {
			this.force = false;
		}

		public Startable(boolean force) {
			this.force = force;
		}

		public boolean force = false;

		public abstract void run();
	}

	public class Render implements Renderer {
		public int clearcolor = 0xFFc5c2af;
		public RAWFont currentFont;

		public void glColor3i(int r, int gg, int b) {
			glColor4i(r, gg, b, 255);
		}

		public void glColor(int c) {
			color = c & 0xFFFFFFFF;
		}

		public void glClearColor(int c) {
			clearcolor = c & 0xFFFFFFFF;
		}

		public void glColor4i(int red, int green, int blue, int alpha) {
			color = (alpha << 24) + (red << 16) + (green << 8) + (blue);
		}

		public void glClearColor(int red, int green, int blue, int alpha) {
			clearcolor = (alpha << 24) + (red << 16) + (green << 8) + (blue);
		}

		public void glClear() {
			for (int x = 0; x < size[0]; x++) {
				for (int y = 0; y < size[1]; y++) {
					canvas2d[x + y * size[0]] = clearcolor;
				}
			}
		}

		public void glDrawSkin(int skinwidth, int[] skin, int x0, int y0, int s0, int t0, int s1, int t1, boolean transparent) {
			x0+=Main.screenPos[0];
			y0+=Main.screenPos[1];
			int oldColor;
			int newColor;
			int onex = s0 <= s1?1:-1;
			int oney = t0 <= t1?1:-1;
			int width = 0;
			int height = 0;
			if (onex == -1) {
				int s00 = s0;
				s0 = s1;
				s1 = s00;
				width = s1-s0;
			}
			if (oney == -1) {
				int t00 = t0;
				t0 = t1;
				t1 = t00;
				height = t1-t0;
			}
			if (x0 >= size[0] || y0 >= size[0]) {
				return;
			}
			if (x0 + width >= size[0]) {
				s1 = size[0] - x0 + s0;
			}
			if (y0 + height >= size[1]) {
				t1 = size[1] - y0 + t0;
			}
			if (x0 < 0) {
				if (onex == -1) {
					width += x0;
					s1 += x0 + 1;
				} else {
					s0 -= x0;
				}
				x0 = 0;
			}
			if (y0 < 0) {
				if (oney == -1) {
					height += y0;
					t1 += y0 + 1;
				} else {
					t0 -= y0;
				}
				y0 = 0;
			}
			for (int texx = 0; texx < s1 - s0; texx++) {
				for (int texy = 0; texy < t1 - t0; texy++) {
					newColor = skin[(s0 + texx) + (t0 + texy) * skinwidth];
					if (transparent) {
						oldColor = canvas2d[(x0 + texx*onex + width) + (y0 + texy*oney + height) * size[0]];
						float a2 = (newColor >> 24 & 0xFF) / 255f;
						float a1 = 1f-a2;
						int r = (int) ((oldColor >> 16 & 0xFF) * a1 + (newColor >> 16 & 0xFF) * a2);
						int g = (int) ((oldColor >> 8 & 0xFF) * a1 + (newColor >> 8 & 0xFF) * a2);
						int b = (int) ((oldColor & 0xFF) * a1 + (newColor & 0xFF) * a2);
						newColor = 0xFF000000 | r << 16 | g << 8 | b;
					}
					canvas2d[(x0 + texx*onex + width) + (y0 + texy*oney + height) * size[0]] = newColor;
				}
			}
		}

		public void glDrawLine(int x0, int y0, int x1, int y1) {
			x0+=Main.screenPos[0];
			x1+=Main.screenPos[0];
			y0+=Main.screenPos[1];
			y1+=Main.screenPos[1];
			if (x0 >= size[0] || y0 >= size[0]) {
				return;
			}
			if (y0 == y1) {
				for (int x = 0; x <= x1 - x0; x++) {
					canvas2d[x0 + x + y0 * size[0]] = color;
				}
			} else if (x0 == x1) {
				for (int y = 0; y <= y1 - y0; y++) {
					canvas2d[x0 + (y0 + y) * size[0]] = color;
				}
			} else {
				int m = (y1 - y0) / (x1 - x0);
				for (int texx = 0; texx <= x1 - x0; texx++) {
					if (x0 + texx < size[0] && y0 + (m * texx) < size[1]) {
						canvas2d[(x0 + texx) + (y0 + (m * texx)) * size[0]] = color;
					}
				}
			}
		}

		public void glFillRect(int x0, int y0, int w1, int h1) {
			x0+=Main.screenPos[0];
			y0+=Main.screenPos[1];
			int x1 = x0+w1;
			int y1 = y0+h1;
			if (x0 >= size[0] || y0 >= size[0]) {
				return;
			}
			if (x1 >= size[0]) {
				x1 = size[0];
			}
			if (y1 >= size[1]) {
				y1 = size[1];
			}
			final int sizeW = size[0];
			for (int x = x0; x < x1; x++) {
				for (int y = y0; y < y1; y++) {
					canvas2d[(x) + (y) * sizeW] = color;
				}
			}
		}

		public int[] getMatrixOfImage(BufferedImage bufferedImage) {
			int width = bufferedImage.getWidth(null);
			int height = bufferedImage.getHeight(null);
			int[] pixels = new int[width * height];
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					pixels[i + j * width] = bufferedImage.getRGB(i, j);
				}
			}

			return pixels;
		}

		public void glDrawStringLeft(int x, int y, String text) {
			x+=Main.screenPos[0];
			y+=Main.screenPos[1];
			final int[] chars = currentFont.getCharIndexes(text);
			currentFont.drawText(canvas2d, size, x, y, chars, color);
		}

		public void glDrawStringCenter(int x, int y, String text) {
			glDrawStringLeft(x - (glGetStringWidth(currentFont, text) / 2), y, text);
		}

		public void glDrawStringRight(int x, int y, String text) {
			glDrawStringLeft(x - glGetStringWidth(currentFont, text), y, text);
		}

		public void glSetFont(RAWFont font) {
			if (currentFont != font) {
				currentFont = font;
			}
		}

		public int glGetStringWidth(RAWFont rf, String text) {
			int w =(rf.charW+1)*text.length();
			if (text.length() > 0) {
				return w-1;
			} else {
				return 0;
			}
			// return text.length()*6;
		}

		public int glGetFontWidth(FontMetrics fm, String text) {
			return fm.stringWidth(text);
		}

		@Deprecated
		public int glGetCurrentFontHeight() {
			return currentFont.charH;
		}

		@Override
		public int glGetClearColor() {
			return clearcolor;
		}

		@Override
		public RAWFont getCurrentFont() {
			return currentFont;
		}

	}
	
	@Override
	public int[] getSize() {
		return size;
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}

	@Override
	public Renderer getRenderer() {
		return new Render();
	}
}

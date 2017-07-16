package org.warp.picalculator.gui.graphicengine.cpu;

import java.awt.FontMetrics;

import org.warp.picalculator.Main;
import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.gui.graphicengine.Renderer;

public class CPURenderer implements Renderer {
		public int clearcolor = 0xFFc5c2af;
		public CPUFont currentFont;
		public CPUSkin currentSkin;
		public int color = 0xFF000000;
		public int[] size = new int[] { 1, 1 };
		static int[] canvas2d = new int[1];

		@Override
		public void glColor3i(int r, int gg, int b) {
			glColor4i(r, gg, b, 255);
		}

		@Override
		public void glColor(int c) {
			color = c & 0xFFFFFFFF;
		}

		@Override
		public void glClearColor(int c) {
			clearcolor = c & 0xFFFFFFFF;
		}

		@Override
		public void glColor4i(int red, int green, int blue, int alpha) {
			color = (alpha << 24) + (red << 16) + (green << 8) + (blue);
		}

		@Override
		public void glClearColor4i(int red, int green, int blue, int alpha) {
			clearcolor = (alpha << 24) + (red << 16) + (green << 8) + (blue);
		}

		@Override
		public void glClearColor4f(float red, float green, float blue, float alpha) {
			clearcolor = ((int) (alpha * 255) << 24) + ((int) (red * 255) << 16) + ((int) (green * 255) << 8) + ((int) (blue * 255));
		}

		@Override
		public void glClear(int screenWidth, int screenHeight) {
			for (int x = 0; x < screenWidth; x++) {
				for (int y = 0; y < screenHeight; y++) {
					canvas2d[x + y * size[0]] = clearcolor;
				}
			}
		}

		private void glDrawSkin(int x0, int y0, int s0, int t0, int s1, int t1, boolean transparent) {
			x0 += Main.screenPos[0];
			y0 += Main.screenPos[1];
			int oldColor;
			int newColor;
			final int onex = s0 <= s1 ? 1 : -1;
			final int oney = t0 <= t1 ? 1 : -1;
			int width = 0;
			int height = 0;
			if (onex == -1) {
				final int s00 = s0;
				s0 = s1;
				s1 = s00;
				width = s1 - s0;
			}
			if (oney == -1) {
				final int t00 = t0;
				t0 = t1;
				t1 = t00;
				height = t1 - t0;
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
			int pixelX;
			int pixelY;
			for (int texx = 0; texx < s1 - s0; texx++) {
				for (int texy = 0; texy < t1 - t0; texy++) {
					pixelX = (x0 + texx * onex + width);
					pixelY = (y0 + texy * oney + height);
					if (pixelX - (pixelX % size[0]) == 0) {
						newColor = currentSkin.skinData[(s0 + texx) + (t0 + texy) * currentSkin.skinSize[0]];
						if (transparent) {
							oldColor = canvas2d[pixelX + pixelY * size[0]];
							final float a2 = (newColor >> 24 & 0xFF) / 255f;
							final float a1 = 1f - a2;
							final int r = (int) ((oldColor >> 16 & 0xFF) * a1 + (newColor >> 16 & 0xFF) * a2);
							final int g = (int) ((oldColor >> 8 & 0xFF) * a1 + (newColor >> 8 & 0xFF) * a2);
							final int b = (int) ((oldColor & 0xFF) * a1 + (newColor & 0xFF) * a2);
							newColor = 0xFF000000 | r << 16 | g << 8 | b;
						}
						canvas2d[pixelX + pixelY * size[0]] = newColor;
					}
				}
			}
		}

		@Override
		public void glDrawLine(float x0, float y0, float x1, float y1) {
			x0 += Main.screenPos[0];
			x1 += Main.screenPos[0];
			y0 += Main.screenPos[1];
			y1 += Main.screenPos[1];
			final int ix0 = (int) x0;
			final int ix1 = (int) x1;
			final int iy0 = (int) y0;
			final int iy1 = (int) y1;
			if (ix0 >= size[0] || iy0 >= size[0]) {
				return;
			}
			if (iy0 == iy1) {
				for (int x = 0; x <= ix1 - ix0; x++) {
					if ((ix0 + x < size[0]) & (iy0 < size[1])) {
						canvas2d[ix0 + x + iy0 * size[0]] = color;
					}
				}
			} else if (ix0 == ix1) {
				for (int y = 0; y <= iy1 - iy0; y++) {
					if ((ix0 < size[0]) & (iy0 + y < size[1])) {
						canvas2d[ix0 + (iy0 + y) * size[0]] = color;
					}
				}
			} else {
				final int m = (iy1 - iy0) / (ix1 - ix0);
				for (int texx = 0; texx <= ix1 - ix0; texx++) {
					if (ix0 + texx < size[0] && iy0 + (m * texx) < size[1]) {
						if ((ix0 + texx < size[0]) & ((iy0 + (m * texx)) < size[1])) {
							canvas2d[(ix0 + texx) + (iy0 + (m * texx)) * size[0]] = color;
						}
					}
				}
			}
		}

		@Override
		public void glFillRect(float x, float y, float width, float height, float uvX, float uvY, float uvWidth,
				float uvHeight) {
			if (currentSkin != null) {
				glDrawSkin((int) x, (int) y, (int) uvX, (int) uvY, (int) (uvWidth + uvX), (int) (uvHeight + uvY), true);
			} else {
				glFillColor(x, y, width, height);
			}
		}

		@Override
		public void glFillColor(float x, float y, float width, float height) {
			x += Main.screenPos[0];
			y += Main.screenPos[1];

			final int ix = (int) x;
			final int iy = (int) y;
			final int iw = (int) width;
			final int ih = (int) height;

			int x0 = ix;
			int y0 = iy;
			int x1 = ix + iw;
			int y1 = iy + ih;
			if (ix >= size[0] || iy >= size[0]) {
				return;
			}
			if (x0 < 0) {
				x0 = 0;
			}
			if (x1 >= size[0]) {
				x1 = size[0];
			}
			if (y0 < 0) {
				y0 = 0;
			}
			if (y1 >= size[1]) {
				y1 = size[1];
			}
			final int sizeW = size[0];
			for (int px = x0; px < x1; px++) {
				for (int py = y0; py < y1; py++) {
					canvas2d[(px) + (py) * sizeW] = color;
				}
			}
		}

		@Override
		public void glDrawStringLeft(float x, float y, String textString) {
			x += Main.screenPos[0];
			y += Main.screenPos[1];

			final int ix = (int) x;
			final int iy = (int) y;

			final int[] text = currentFont.getCharIndexes(textString);
			final int[] screen = canvas2d;
			final int[] screenSize = size;
			final int screenLength = screen.length;
			int screenPos = 0;

			int currentInt;
			int currentIntBitPosition;
			int bitData;
			int cpos;
			int j;
			final int l = text.length;
			for (int i = 0; i < l; i++) {
				cpos = (i * (currentFont.charW + 1));
				final int charIndex = text[i];
				for (int dy = 0; dy < currentFont.charH; dy++) {
					for (int dx = 0; dx < currentFont.charW; dx++) {
						j = ix + cpos + dx;
						if (j > 0 & j < screenSize[0]) {
							final int bit = dx + dy * currentFont.charW;
							currentInt = (int) (Math.floor(bit) / (CPUFont.intBits));
							currentIntBitPosition = bit - (currentInt * CPUFont.intBits);
							bitData = (currentFont.chars32[charIndex * currentFont.charIntCount + currentInt] >> currentIntBitPosition) & 1;
							screenPos = ix + cpos + dx + (iy + dy) * screenSize[0];
							if (bitData == 1 & screenLength > screenPos) {
								screen[screenPos] = color;
							}
						}
					}
				}
			}
		}

		@Override
		public void glDrawStringCenter(float x, float y, String text) {
			glDrawStringLeft(x - (currentFont.getStringWidth(text) / 2), y, text);
		}

		@Override
		public void glDrawStringRight(float x, float y, String text) {
			glDrawStringLeft(x - currentFont.getStringWidth(text), y, text);
		}

		@Deprecated
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
		public BinaryFont getCurrentFont() {
			return currentFont;
		}

		@Override
		public void glColor3f(float red, float green, float blue) {
			glColor3i((int) (red * 255f), (int) (green * 255f), (int) (blue * 255f));
		}

		@Override
		public void glColor4f(float red, float green, float blue, float alpha) {
			glColor4i((int) (red * 255f), (int) (green * 255f), (int) (blue * 255f), (int) (alpha * 255f));
		}

		@Override
		public void glClearSkin() {
			currentSkin = null;
		}

		@Override
		public void glDrawCharLeft(int x, int y, char ch) {
			glDrawStringLeft(x, y, ch + "");
		}

		@Override
		public void glDrawCharCenter(int x, int y, char ch) {
			glDrawStringCenter(x, y, ch + "");
		}

		@Override
		public void glDrawCharRight(int x, int y, char ch) {
			glDrawStringRight(x, y, ch + "");
		}

	}

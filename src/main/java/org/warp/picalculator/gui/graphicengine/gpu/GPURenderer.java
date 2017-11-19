package org.warp.picalculator.gui.graphicengine.gpu;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.warp.picalculator.StaticVars;
import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.gui.graphicengine.Renderer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

public class GPURenderer implements Renderer {

	public static GL2ES1 gl;

	private static final int ELEMENT_VERTICES_COUNT = 6;
	private static final int ELEMENTS_MAX_COUNT_PER_BUFFER = StaticVars.enableVBO ? 128 : 1;

	private final DeallocationHelper deallocationHelper = new DeallocationHelper();
	FloatBuffer fbVertices;
	FloatBuffer txVertices;
	FloatBuffer colVertices;
	int fbElements;

	float[] currentColor = new float[24];
	float[] currentClearColorARGBf = new float[] { 1f, 197f / 255f, 194f / 255f, 175f / 255f };
	boolean currentTexEnabled;
	Texture currentTex;
	float currentTexWidth;
	float currentTexHeight;

	GPUFont currentFont;

	@Override
	public void glColor3i(int r, int gg, int b) {
		final float red = (r) / 255f;
		final float gre = (gg) / 255f;
		final float blu = (b) / 255f;
		//currentColor = new float[] { red, gre, blu, 1.0f, red, gre, blu, 1.0f, red, gre, blu, 1.0f, red, gre, blu, 1.0f, };
		currentColor = new float[] { red, gre, blu, 1.0f, red, gre, blu, 1.0f, red, gre, blu, 1.0f, red, gre, blu, 1.0f, red, gre, blu, 1.0f, red, gre, blu, 1.0f, }; //OK
	}

	@Override
	public void glColor3f(float red, float gre, float blu) {
		// currentColor = new float[] { red, gre, blu, 1.0f, red, gre, blu, 1.0f, red, gre, blu, 1.0f, red, gre, blu, 1.0f, };
		currentColor = new float[] { red, gre, blu, 1.0f, red, gre, blu, 1.0f, red, gre, blu, 1.0f, red, gre, blu, 1.0f, red, gre, blu, 1.0f, red, gre, blu, 1.0f, };//OK
	}

	@Override
	public void glColor4f(float red, float gre, float blu, float alp) {
		// currentColor = new float[] { red, gre, blu, alp, red, gre, blu, alp, red, gre, blu, alp, red, gre, blu, alp, };
		currentColor = new float[] { red, gre, blu, alp, red, gre, blu, alp, red, gre, blu, alp, red, gre, blu, alp, red, gre, blu, alp, red, gre, blu, alp, };//ok

	}

	@Override
	public void glColor(int rgb) {
		final int alpha = (rgb >> 24) & 0xFF;
		final int red = (rgb >> 16) & 0xFF;
		final int green = (rgb >> 8) & 0xFF;
		final int blue = rgb & 0xFF;
		glColor4i(red, green, blue, alpha);
	}

	@Override
	public int glGetClearColor() {
		return (int) (currentClearColorARGBf[0] * 255) << 24 | (int) (currentClearColorARGBf[1] * 255) << 16 | (int) (currentClearColorARGBf[2] * 255) << 8 | (int) (currentClearColorARGBf[3] * 255);
	}

	@Override
	public void glClearColor(int rgb) {
		final float alpha = ((rgb >> 24) & 0xFF) / 255f;
		final float red = ((rgb >> 16) & 0xFF) / 255f;
		final float green = ((rgb >> 8) & 0xFF) / 255f;
		final float blue = (rgb & 0xFF) / 255f;
		glClearColor4f(red, green, blue, alpha);
	}

	@Override
	public void glColor4i(int r, int g, int b, int a) {
		final float red = (r) / 255f;
		final float gre = (g) / 255f;
		final float blu = (b) / 255f;
		final float alp = (a) / 255f;
		//currentColor = new float[] { red, gre, blu, alp, red, gre, blu, alp, red, gre, blu, alp, red, gre, blu, alp, };
		currentColor = new float[] { red, gre, blu, alp, red, gre, blu, alp, red, gre, blu, alp, red, gre, blu, alp, red, gre, blu, alp, red, gre, blu, alp, };//OK
	}

	@Override
	public void glClearColor4i(int red, int green, int blue, int alpha) {
		final float ros = (red) / 255f;
		final float gre = (green) / 255f;
		final float blu = (blue) / 255f;
		final float alp = (alpha) / 255f;
		currentClearColorARGBf = new float[] { alp, ros, gre, blu };
	}

	@Override
	public void glClearColor4f(float red, float green, float blue, float alpha) {
		currentClearColorARGBf = new float[] { alpha, red, green, blue };
	}

	@Override
	public void glClear(int screenWidth, int screenHeight) {
		glColor(glGetClearColor());
		glFillColor(0, 0, screenWidth, screenHeight);
	}

	@Override
	public void glDrawLine(float x0, float y0, float x1, float y1) {
		glFillColor(x0, y0, x1 - x0 + 1, y1 - y0 + 1);
	}

	@Override
	public void glFillRect(float x, float y, float width, float height, float uvX, float uvY, float uvWidth,
			float uvHeight) {
		enableTexture();
		uvWidth /= currentTexWidth;
		uvX /= currentTexWidth;
		uvHeight /= currentTexHeight;
		uvY = 1 - uvY / currentTexHeight - uvHeight;
//		final float[] vertices = { x, y, 0.0f, x, y + height, 0.0f, x + width, y, 0.0f, x + width, y + height, 0.0f, };
//		final float[] tex_vertices = { uvX, uvY + uvHeight, uvX, uvY, uvX + uvWidth, uvY + uvHeight, uvX + uvWidth, uvY, };
		//V0	x, y, 0.0f
		//V1	x, y + height, 0.0f
		//V2	x + width, y, 0.0f
		//V3	x + width, y + height, 0.0f

		//NV0 = V1
		//NV1 = V3
		//NV2 = V0

		//NV3 = V0
		//NV4 = V3
		//NV5 = V2

		final float[] vertices = { x, y + height, 0.0f, x + width, y + height, 0.0f, x, y, 0.0f, x, y, 0.0f, x + width, y + height, 0.0f, x + width, y, 0.0f };
		final float[] tex_vertices = { uvX, uvY, uvX + uvWidth, uvY, uvX, uvY + uvHeight, uvX, uvY + uvHeight, uvX + uvWidth, uvY, uvX + uvWidth, uvY + uvHeight };
		fbElements++;
		fbVertices.put(vertices);
		txVertices.put(tex_vertices);
		colVertices.put(currentColor);
	}

	@Override
	public void glFillColor(float x0, float y0, float w1, float h1) {
		disableTexture();
//		final float[] vertices = { x0, y0, 0.0f, x0, y0 + h1, 0.0f, x0 + w1, y0, 0.0f, x0 + w1, y0 + h1, 0.0f, };
//		final float[] tex_vertices = { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, };
		//V0	x0, y0, 0.0f
		//V1	x0, y0 + h1, 0.0f
		//V2	x0 + w1, y0, 0.0f
		//V3	x0 + w1, y0 + h1, 0.0f

		//NV0 = V1
		//NV1 = V3
		//NV2 = V0

		//NV3 = V0
		//NV4 = V3
		//NV5 = V2

		final float[] vertices = { x0, y0 + h1, 0.0f, x0 + w1, y0 + h1, 0.0f, x0, y0, 0.0f, x0, y0, 0.0f, x0 + w1, y0 + h1, 0.0f, x0 + w1, y0, 0.0f, };
		final float[] tex_vertices = { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, };
		fbElements++;
		fbVertices.put(vertices);
		txVertices.put(tex_vertices);
		colVertices.put(currentColor);
	}

	@Override
	public void glDrawStringLeft(float x, float y, String text) {
		final int txtLen = text.length();
		final int[] txtArray = currentFont.getCharIndexes(text);
		int tableIndexX;
		int tableIndexY;
		for (int currentCharIndex = 0; currentCharIndex < txtLen; currentCharIndex++) {
			tableIndexX = txtArray[currentCharIndex] % currentFont.memoryWidthOfEachColumn;
			tableIndexY = (txtArray[currentCharIndex] - tableIndexX) / currentFont.memoryWidthOfEachColumn;
			glFillRect(x + ((float) currentCharIndex) * ((float) (currentFont.charW)), y, currentFont.charW, currentFont.charH, tableIndexX * currentFont.charW, tableIndexY * currentFont.charH, currentFont.charW, currentFont.charH);
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

	@Override
	public void glDrawCharLeft(int x, int y, char ch) {
		final int index = currentFont.getCharIndex(ch);
		final int tableIndexX = index % currentFont.memoryWidthOfEachColumn;
		final int tableIndexY = (index - tableIndexX) / currentFont.memoryWidthOfEachColumn;
		glFillRect(x, y, currentFont.charW, currentFont.charH, tableIndexX * currentFont.charW, tableIndexY * currentFont.charH, currentFont.charW, currentFont.charH);
	}

	@Override
	public void glDrawCharCenter(int x, int y, char ch) {
		glDrawCharLeft(x - (currentFont.charW / 2), y, ch);
	}

	@Override
	public void glDrawCharRight(int x, int y, char ch) {
		glDrawCharLeft(x - currentFont.charW, y, ch);
	}

	@Override
	public BinaryFont getCurrentFont() {
		return currentFont;
	}

	static Texture importTexture(GL gl, String string) throws IOException {
		final FileInputStream f = new FileInputStream("test.png");
		final TextureData tx_dat = TextureIO.newTextureData(gl.getGLProfile(), f, false, TextureIO.PNG);
		final Texture tex = new Texture(gl, tx_dat);
		tex.setTexParameteri(gl, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
		tex.setTexParameteri(gl, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
//		tex.setTexParameteri(gl, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
//		tex.setTexParameteri(gl, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
		return tex;
	}

	static OpenedTextureData openTexture(String file) throws GLException, IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		BufferedImage img = ImageIO.read(!Files.exists(Paths.get(file)) ? GPURenderer.class.getResource("/" + file) : new File(file).toURI().toURL());
		ImageIO.write(img, "png", os);
		return new OpenedTextureData(img.getWidth(), img.getHeight(), os);
	}
	
	public static class OpenedTextureData {
		public final int w;
		public final int h;
		public final ByteArrayOutputStream os;
		
		/**
		 * @param w
		 * @param h
		 * @param os
		 */
		public OpenedTextureData(int w, int h, ByteArrayOutputStream os) {
			this.w = w;
			this.h = h;
			this.os = os;
		}
		
	}

	static Texture importTexture(ByteArrayOutputStream os) throws GLException, IOException {
		final InputStream fis = new ByteArrayInputStream(os.toByteArray());
		final Texture tex = TextureIO.newTexture(fis, false, TextureIO.PNG);
		tex.setTexParameteri(gl, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
		tex.setTexParameteri(gl, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
		os = null;
		return tex;
	}

	public void startDrawCycle(boolean first) {
		if (fbVertices == null) {
			fbVertices = Buffers.newDirectFloatBuffer(3 * ELEMENT_VERTICES_COUNT * ELEMENTS_MAX_COUNT_PER_BUFFER);
			txVertices = Buffers.newDirectFloatBuffer(2 * ELEMENT_VERTICES_COUNT * ELEMENTS_MAX_COUNT_PER_BUFFER);
			colVertices = Buffers.newDirectFloatBuffer(4 * ELEMENT_VERTICES_COUNT * ELEMENTS_MAX_COUNT_PER_BUFFER);
		}
		if (first || fbVertices == null || cycleEnded) {
			fbElements = 0;
		}
		cycleEnded = false;
	}

	private boolean precTexEnabled;
	private Texture precTex;
	private boolean cycleEnded = true;

	public void updateDrawCycle() {
		updateDrawCycle(false, false);
	}

	public void updateDrawCycle(boolean first, boolean last) {
		final boolean textureChange = precTexEnabled != currentTexEnabled || precTex != currentTex;
		final boolean changeRequired = last || fbElements >= ELEMENTS_MAX_COUNT_PER_BUFFER;
		if (first) {
			startDrawCycle(true);
		}
		if (textureChange) {
			if (!first && fbElements > 0) {
				endDrawCycle();
				if (!last) {
					startDrawCycle(true);
				}
			}
			precTexEnabled = currentTexEnabled;
			precTex = currentTex;
			if (currentTexEnabled) {
				gl.glEnable(GL.GL_TEXTURE_2D);
				currentTex.bind(gl);
			} else {
				gl.glDisable(GL.GL_TEXTURE_2D);
			}
		} else if (!first) {
			if (fbElements > 0 && changeRequired) {
				endDrawCycle();
				if (!last) {
					startDrawCycle(false);
				}
			}
		}
	}

	public void endDrawCycle() {
		fbVertices.limit(fbVertices.position());
		txVertices.limit(txVertices.position());
		colVertices.limit(colVertices.position());
		fbVertices.rewind();
		txVertices.rewind();
		colVertices.rewind();

		gl.glVertexPointer(3, GL.GL_FLOAT, 0, fbVertices);
		gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, txVertices);
		gl.glColorPointer(4, GL.GL_FLOAT, 0, colVertices);
		fbVertices.limit(fbVertices.capacity());
		txVertices.limit(txVertices.capacity());
		colVertices.limit(colVertices.capacity());

		gl.glDrawArrays(GL.GL_TRIANGLES, 0, fbElements * ELEMENT_VERTICES_COUNT);
		//gl.glDrawArrays(GL.GL_TRIANGLE_STRIP, 0, fbElements * ELEMENT_VERTICES_COUNT);
		cycleEnded = true;

//		deleteBuffer(fbVertices);
//		deleteBuffer(txVertices);
//		deleteBuffer(colVertices);
//		fbVertices = null;
//		txVertices = null;
//		colVertices = null;
	}

	public void deleteBuffer(final Buffer realNioBuffer) {
		if (deallocationHelper != null) {
			deallocationHelper.deallocate(realNioBuffer);
		}
	}

	@Override
	public void glClearSkin() {
		if (currentTex != null) {
			currentTex = null;
			updateDrawCycle();
		}
	}

	void disableTexture() {
		currentTexEnabled = false;
		updateDrawCycle();
	}

	void enableTexture() {
		currentTexEnabled = true;
		updateDrawCycle();
	}

	void useTexture(Texture t, float w, float h) {
		currentTex = t;
		currentTexWidth = w;
		currentTexHeight = h;
		enableTexture();
	}
}

package org.warp.picalculator.gui.graphicengine.gpu;

import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import javax.imageio.ImageIO;

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

	private final DeallocationHelper deallocationHelper = new DeallocationHelper();
	FloatBuffer fbVertices;
	FloatBuffer txVertices;
	FloatBuffer colVertices;
	int fbElements;

	float[] currentColor = new float[16];
	float[] currentClearColorARGBf = new float[]{1f, 197f/255f, 194f/255f, 175f/255f};
	boolean currentTexEnabled;
	Texture currentTex;
	float currentTexWidth;
	float currentTexHeight;
	
	GPUFont currentFont;

	@Override
	public void glColor3i(int r, int gg, int b) {
		final float red = ((float)r) / 255f;
		final float gre = ((float)gg) / 255f;
		final float blu = ((float)b) / 255f;
		currentColor = new float[] { red, gre, blu, 1.0f, red, gre, blu, 1.0f, red, gre, blu, 1.0f, red, gre, blu, 1.0f, };
	}

	@Override
	public void glColor3f(float red, float gre, float blu) {
		currentColor = new float[] { red, gre, blu, 1.0f, red, gre, blu, 1.0f, red, gre, blu, 1.0f, red, gre, blu, 1.0f, };
	}

	@Override
	public void glColor4f(float red, float gre, float blu, float alp) {
		currentColor = new float[] { red, gre, blu, alp, red, gre, blu, alp, red, gre, blu, alp, red, gre, blu, alp, };
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
		return (int)(currentClearColorARGBf[0] * 255)  << 24 | (int)(currentClearColorARGBf[1] * 255) << 16 | (int)(currentClearColorARGBf[2] * 255) << 8 | (int)(currentClearColorARGBf[3] * 255);
	}

	@Override
	public void glClearColor(int rgb) {
		final float alpha = (float)((rgb >> 24) & 0xFF) / 255f;
		final float red = (float)((rgb >> 16) & 0xFF) / 255f;
		final float green = (float)((rgb >> 8) & 0xFF) / 255f;
		final float blue = (float)(rgb & 0xFF) / 255f;
		glClearColor4f(red, green, blue, alpha);
	}

	@Override
	public void glColor4i(int r, int g, int b, int a) {
		final float red = (r) / 255f;
		final float gre = (g) / 255f;
		final float blu = (b) / 255f;
		final float alp = (a) / 255f;
		currentColor = new float[] { red, gre, blu, alp, red, gre, blu, alp, red, gre, blu, alp, red, gre, blu, alp, };
	}

	@Override
	public void glClearColor4i(int red, int green, int blue, int alpha) {
		final float ros = (red) / 255f;
		final float gre = (green) / 255f;
		final float blu = (blue) / 255f;
		final float alp = (alpha) / 255f;
		currentClearColorARGBf = new float[]{alp, ros, gre, blu};
	}

	@Override
	public void glClearColor4f(float red, float green, float blue, float alpha) {
		currentClearColorARGBf = new float[]{alpha, red, green, blue};
	}

	@Override
	public void glClear(int screenWidth, int screenHeight) {
		glColor(glGetClearColor());
		glFillColor(0, 0, screenWidth, screenHeight);
	}

	@Override
	public void glDrawLine(float x0, float y0, float x1, float y1) {
		glFillColor(x0, y0, x1-x0+1, y1-y0+1);
	}

	@Override
	public void glFillRect(float x, float y, float width, float height, float uvX, float uvY, float uvWidth, float uvHeight) {
		enableTexture();
		uvWidth/=currentTexWidth;
		uvX/=currentTexWidth;
		uvHeight/=currentTexHeight;
		uvY = 1 - uvY/currentTexHeight - uvHeight;
		final float[] vertices = { x, y, 0.0f, x, y + height, 0.0f, x + width, y, 0.0f, x + width, y + height, 0.0f, };
		final float[] tex_vertices = { uvX, uvY + uvHeight, uvX, uvY, uvX + uvWidth, uvY + uvHeight, uvX + uvWidth, uvY, };
		fbElements++;
		fbVertices.put(vertices);
		txVertices.put(tex_vertices);
		colVertices.put(currentColor);
	}

	@Override
	public void glFillColor(float x0, float y0, float w1, float h1) {
		disableTexture();
		final float[] vertices = { x0, y0, 0.0f, x0, y0 + h1, 0.0f, x0 + w1, y0, 0.0f, x0 + w1, y0 + h1, 0.0f, };
		final float[] tex_vertices = { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, };
		fbElements++;
		fbVertices.put(vertices);
		txVertices.put(tex_vertices);
		colVertices.put(currentColor);
	}

	@Override
	public void glDrawStringLeft(float x, float y, String text) {
		final int txtLen = text.length();
		int[] txtArray = currentFont.getCharIndexes(text);
		int tableIndexX;
		int tableIndexY;
		for (int currentCharIndex = 0; currentCharIndex < txtLen; currentCharIndex++) {
			tableIndexX = txtArray[currentCharIndex] % currentFont.memoryWidthOfEachColumn;
			tableIndexY = (txtArray[currentCharIndex] - tableIndexX) / currentFont.memoryWidthOfEachColumn;
			glFillRect(x + ((float)currentCharIndex) * ((float)(currentFont.charW)), y, currentFont.charW, currentFont.charH, tableIndexX*currentFont.charW, tableIndexY*currentFont.charH, currentFont.charW, currentFont.charH);
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
		int index = currentFont.getCharIndex(ch);
		int tableIndexX = index % currentFont.memoryWidthOfEachColumn;
		int tableIndexY = (index - tableIndexX) / currentFont.memoryWidthOfEachColumn;
		glFillRect(x, y, currentFont.charW, currentFont.charH, tableIndexX*currentFont.charW, tableIndexY*currentFont.charH, currentFont.charW, currentFont.charH);
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

	static BufferedImage openTexture(String file) throws GLException, IOException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		return ImageIO.read(GPURenderer.class.getClassLoader().getResource(file));
	}

	static Texture importTexture(BufferedImage img) throws GLException, IOException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(img, "png", os);
		final InputStream fis = new ByteArrayInputStream(os.toByteArray());
		Texture tex = TextureIO.newTexture(fis, false, TextureIO.PNG);
		tex.setTexParameteri(gl, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
		tex.setTexParameteri(gl, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
		return tex;
	}

	@Override
	public void glClearSkin() {
		if (currentTex != null) {
			currentTex = null;
			endDrawCycle();
			startDrawCycle();
		}
	}

	public void startDrawCycle() {
		fbVertices = Buffers.newDirectFloatBuffer(3 * 4);
		txVertices = Buffers.newDirectFloatBuffer(2 * 4);
		colVertices = Buffers.newDirectFloatBuffer(4 * 4);
		fbElements = 0;
	}

	public void endDrawCycle() {
		fbVertices.rewind();
		txVertices.rewind();
		colVertices.rewind();

		gl.glColorPointer(4, GL.GL_FLOAT, 0, colVertices);
		gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, txVertices);
		gl.glVertexPointer(3, GL.GL_FLOAT, 0, fbVertices);

		if (currentTexEnabled) {
			gl.glEnable(GL2ES1.GL_TEXTURE_2D);
			currentTex.bind(gl);
		} else {
			gl.glDisable(GL2ES1.GL_TEXTURE_2D);
		}

		gl.glDrawArrays(GL.GL_TRIANGLE_STRIP, 0, 4);

		deleteBuffer(fbVertices);
		deleteBuffer(txVertices);
		deleteBuffer(colVertices);
		fbVertices = null;
		txVertices = null;
		colVertices = null;
	}

	public void deleteBuffer(final Buffer realNioBuffer) {
		if (deallocationHelper != null) {
			deallocationHelper.deallocate(realNioBuffer);
		}
	}

	void disableTexture() {
		endDrawCycle();
		startDrawCycle();
		currentTexEnabled = false;
	}

	void enableTexture() {
		endDrawCycle();
		startDrawCycle();
		currentTexEnabled = true;
	}

	void useTexture(Texture t, float w, float h) {
		enableTexture();
		currentTex = t;
		currentTexWidth = w;
		currentTexHeight = h;
	}
}

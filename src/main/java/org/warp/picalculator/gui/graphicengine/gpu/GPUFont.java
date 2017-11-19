package org.warp.picalculator.gui.graphicengine.gpu;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.cpu.CPUFont;

import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;

import ar.com.hjg.pngj.IImageLine;
import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngReader;
import ar.com.hjg.pngj.PngWriter;
import ar.com.hjg.pngj.chunks.ChunkCopyBehaviour;
import ar.com.hjg.pngj.chunks.PngChunkTextVar;

public class GPUFont implements BinaryFont {

	public Texture texture;
	public int textureW;
	public int textureH;
	public int charW;
	public int charH;
	public int minCharIndex;
	public int maxCharIndex;
	public LinkedList<Integer[]> intervals;
	public int memoryWidth;
	public int memoryHeight;
	public int memoryWidthOfEachColumn;

	private boolean initialized = false;
	private ByteArrayOutputStream tmpFont;

	GPUFont(GraphicEngine g, String name) throws IOException {
		this((GPUEngine) g, null, name);
	}

	public GPUFont(GraphicEngine g, String path, String name) throws IOException {
		load(path, name);
		((GPUEngine) g).registerFont(this);
	}
	
	@Override
	public void load(String name) throws IOException {
		load(null, name);
	}

	public void load(String path, String name) throws IOException {
		CPUFont font;
		if (path == null) {
			font = CPUFont.loadTemporaryFont(name);
		} else {
			font = CPUFont.loadTemporaryFont(path, name);
		}
		charW = font.charW;
		charH = font.charH;
		minCharIndex = font.minBound;
		maxCharIndex = font.maxBound;
		intervals = font.intervals;
		pregenTexture(font.rawchars);
		font = null;
	}

	public int[] getCharIndexes(String txt) {
		final int l = txt.length();
		final int[] indexes = new int[l];
		final char[] chars = txt.toCharArray();
		for (int i = 0; i < l; i++) {
			indexes[i] = getCharIndex(chars[i]);
		}
		return indexes;
	}

	public int getCharIndex(char ch) {
		return (ch & 0xFFFF) - minCharIndex;
	}
	
	private void pregenTexture(boolean[][] chars) {
		final int totalChars = maxCharIndex - minCharIndex;
		int w = powerOf2((int) (Math.ceil(Math.sqrt(totalChars) * charW)));
		int h = powerOf2((int) (Math.ceil(Math.sqrt(totalChars) * charH)));
		int maxIndexW = (int) Math.floor(((double) w) / ((double) charW)) - 1;
		int maxIndexH = (int) Math.floor(((double) h) / ((double) charH)) - 1;
		if (w > h) {
			System.out.println("w > h");
			h = powerOf2((int) (Math.ceil((((double)totalChars)/((double)(maxIndexW))) * charH)));
			maxIndexH = (int) Math.floor(((double) h) / ((double) charH)) - 1;
		} else {
			System.out.println("w <= h");
			w = powerOf2((int) (Math.ceil((((double)totalChars)/((double)(maxIndexH))) * charW)));
			maxIndexW = (int) Math.floor(((double) w) / ((double) charW)) - 1;
		}
//		final int h = powerOf2((int) (Math.ceil(Math.sqrt(totalChars) * charH)));

		System.out.println(((int)Math.ceil(Math.sqrt(totalChars) * charW)) + " * " + ((int)Math.ceil(Math.sqrt(totalChars) * charH)) + " --> " + w + " * " + h);
		
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(w*h*8*4);
		final ImageInfo imi = new ImageInfo(w, h, 8, true); // 8 bits per channel, alpha
		// open image for writing to a output stream
		final PngWriter png = new PngWriter(outputStream, imi);
		for (int y = 0; y < png.imgInfo.rows; y++) {
			ImageLineInt iline = new ImageLineInt(imi);
			int[] xValues = new int[imi.cols];
			for (int indexX = 0; indexX < maxIndexW; indexX++) {// this line will be written to all rows
				final int charY = (y % charH);
				final int indexY = (y - charY)/charH;
				final int i = indexY * maxIndexW + indexX;
				boolean[] currentChar;
				if (i < totalChars && (currentChar=chars[i]) != null) {
					for (int charX = 0; charX < charW; charX++) {
						if (i > 0 & i < totalChars && currentChar != null && currentChar[charX + charY * charW]) {
							xValues[indexX * charW + charX] = 0xFFFFFFFF;
						}
//						ImageLineHelper.setPixelRGBA8(iline, x, color, color, color, color);
					}
				}
			}
			ImageLineHelper.setPixelsRGBA8(iline, xValues);
			if (y % 200 == 0) {
				System.out.println(y + "/" + png.imgInfo.rows);
			}
			png.writeRow(iline);
		}
		png.end();
		
		try {
			memoryWidth = w;
			memoryHeight = h;
			memoryWidthOfEachColumn = maxIndexW;
			textureW = w;
			textureH = h;
			outputStream.flush();
			this.tmpFont = outputStream;
		} catch (GLException | IOException e) {
			e.printStackTrace();
		}
	}

	private void genTexture() {
		try {
			texture = GPURenderer.importTexture(tmpFont);
			tmpFont = null;
		} catch (GLException | IOException e) {
			e.printStackTrace();
		}
	}

	private int powerOf2(int i) {
		return i >1 ? Integer.highestOneBit(i-1)<<1 : 1;
	}

	@Override
	public void initialize(GraphicEngine d) {
		genTexture();
		tmpFont = null;
		initialized = true;
	}

	@Override
	public void use(GraphicEngine d) {
		if (!initialized) {
			initialize(d);
		}
		final GPURenderer r = (GPURenderer) d.getRenderer();
		r.currentFont = this;
		r.useTexture(texture, textureW, textureH);
	}

	@Override
	public int getStringWidth(String text) {
		final int w = (charW) * text.length();
		if (text.length() > 0) {
			return w;
		} else {
			return 0;
		}
	}

	@Override
	public int getCharacterWidth() {
		return charW;
	}

	@Override
	public int getCharacterHeight() {
		return charH;
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}
}
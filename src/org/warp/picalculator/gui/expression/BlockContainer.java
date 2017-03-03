package org.warp.picalculator.gui.expression;

import java.io.IOException;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import org.warp.picalculator.device.graphicengine.Display;
import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.gui.graphicengine.gpu.GPUFont;

public class BlockContainer implements GraphicalElement {

	private final int minWidth;
	private final int minHeight;
	private final ObjectArrayList<Block> content;
	private boolean small;
	private int width;
	private int height;
	private int line;
	
	public BlockContainer() {
		this(false, BlockContainer.getDefaultCharWidth(true), BlockContainer.getDefaultCharHeight(true));
	}
	
	public BlockContainer(boolean small) {
		this(small, BlockContainer.getDefaultCharWidth(true), BlockContainer.getDefaultCharHeight(true));
	}
	
	public BlockContainer(boolean small, int minWidth, int minHeight) {
		this(small, minWidth, minHeight, new ObjectArrayList<>());
	}
	
	public BlockContainer(boolean small, int minWidth, int minHeight, ObjectArrayList<Block> content) {
		this.small = small;
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		for (Block b: content) {
			b.setSmall(small);
		}
		this.content = content;
		recomputeDimensions();
	}
	
	public void addBlock(Block b) {
		b.setSmall(small);
		content.add(b);
		recomputeDimensions();
	}

	public void removeBlock(Block b) {
		content.remove(b);
		recomputeDimensions();
	}

	public void removeAt(int i) {
		content.remove(i);
		recomputeDimensions();
	}
	
	public Block getBlockAt(int i) {
		return content.get(i);
	}
	
	public void clear() {
		content.clear();
		recomputeDimensions();
	}
	
	/**
	 * 
	 * @param ge Graphic Engine class.
	 * @param r Graphic Renderer class of <b>ge</b>.
	 * @param x Position relative to the window.
	 * @param y Position relative to the window.
	 * @param caret Position of the caret.
	 */
	public void draw(GraphicEngine ge, Renderer r, int x, int y, Caret caret) {
		int paddingX = 0;

		if (caret.getRemaining() == 0) {
			if (content.size() > 0) {
				BlockContainer.drawCaret(ge, r, caret, x, y, content.get(0).getHeight());
			} else {
				BlockContainer.drawCaret(ge, r, caret, x, y, height);
			}
		}
		
		for (Block b : content) {
			caret.skip(1);
			b.draw(ge, r, x+paddingX, y+line-b.getLine(), caret);
			paddingX += b.getWidth();
			if (caret.getRemaining() == 0) BlockContainer.drawCaret(ge, r, caret, x + paddingX, y+line-b.getLine(), b.height);
		}
		caret.skip(1);
	}

	@Override
	public void recomputeDimensions() {
		int l = 0; //Line
		int w = 0; //Width
		int h2 = 0; //Height under the line. h = h2 + l
		int h = 0; //Height
		
		for (Block b : content) {
			w += b.getWidth();
			final int bl = b.getLine();
			final int bh = b.getHeight();
			final int bh2 = bh - bl;
			if (bl > l) {
				l = bl;
			}
			if (bh2 > h2) {
				h2 = bh2;
			}
		}
		
		h = h2 + l;

		line = l;
		if (w > minWidth) {
			width = w;
		} else {
			width = minWidth;
		}
		if (h > minHeight) {
			height = h;
		} else {
			height = minHeight;
		}
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getLine() {
		return line;
	}

	private static final BinaryFont[] defFonts = new BinaryFont[2];
	private static final int[] defFontSizes = new int[4];
	private static final int defColor = 0xFF000000;
	
	public static void initializeFonts(BinaryFont big, BinaryFont small) {
		defFonts[0] = big;
		defFonts[1] = small;
		defFontSizes[0] = big.getCharacterWidth();
		defFontSizes[1] = big.getCharacterHeight();
		defFontSizes[2] = small.getCharacterWidth();
		defFontSizes[3] = small.getCharacterHeight();
	}
	
	public static BinaryFont getDefaultFont(boolean small) {
		return defFonts[small?1:0];
	}


	public static int getDefaultColor() {
		return defColor;
	}

	public static int getDefaultCharWidth(boolean b) {
		return defFontSizes[b?2:0];
	}
	
	public static int getDefaultCharHeight(boolean b) {
		return defFontSizes[b?3:1];
	}
	
	public static void drawCaret(GraphicEngine ge, Renderer r, Caret caret, int x, int y, int height) {
		r.glColor(getDefaultColor());
		r.glDrawLine(x, y, x, y-1+height);
		r.glDrawLine(x+1, y, x+1, y-1+height);
	}

	public void setSmall(boolean small) {
		this.small = small;
		recomputeDimensions();
	}

	public ObjectArrayList<Block> getContent() {
		return content.clone();
	}
	
}
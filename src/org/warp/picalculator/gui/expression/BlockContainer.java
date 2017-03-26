package org.warp.picalculator.gui.expression;

import java.io.IOException;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import org.warp.picalculator.device.graphicengine.Display;
import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.gui.graphicengine.gpu.GPUFont;

public class BlockContainer implements GraphicalElement {

	private static boolean initialized = false;
	
	private final int minWidth;
	private final int minHeight;
	private final ObjectArrayList<Block> content;
	private boolean small;
	private int width;
	private int height;
	private int line;
	public final boolean withBorder;
	
	public BlockContainer() {
		this(false, BlockContainer.getDefaultCharWidth(false), BlockContainer.getDefaultCharHeight(false), true);
	}
	
	public BlockContainer(boolean small) {
		this(small, BlockContainer.getDefaultCharWidth(small), BlockContainer.getDefaultCharHeight(small), true);
	}
	
	public BlockContainer(boolean small, boolean withBorder) {
		this(small, BlockContainer.getDefaultCharWidth(small), BlockContainer.getDefaultCharHeight(small), withBorder);
	}
	
	public BlockContainer(boolean small, int minWidth, int minHeight, boolean withBorder) {
		this(small, minWidth, minHeight, new ObjectArrayList<>(), withBorder);
	}
	
	public BlockContainer(boolean small, int minWidth, int minHeight, ObjectArrayList<Block> content, boolean withBorder) {
		this.small = small;
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		this.withBorder = withBorder;
		for (Block b: content) {
			if (b.isSmall() != small) {
				b.setSmall(small);
			}
		}
		this.content = content;
		recomputeDimensions();
	}

	public void addBlock(int position, Block b) {
		if (b.isSmall() != small) {
			b.setSmall(small);
		}
		if (position >= content.size()) {
			content.add(b);
		} else {
			content.add(position, b);
		}
		recomputeDimensions();
	}
	
	public void appendBlock(Block b) {
		if (b.isSmall() != small) {
			b.setSmall(small);
		}
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
		int paddingX = 1;

		if (caret.getRemaining() == 0) {
			if (content.size() > 0) {
				BlockContainer.drawCaret(ge, r, caret, x, y+line-content.get(0).line, content.get(0).height);
			} else {
				BlockContainer.drawCaret(ge, r, caret, x, y, height);
			}
		}
		
		if (withBorder && content.size() == 0) {
			r.glDrawLine(x+paddingX, y, x+paddingX+width-1, y);
			r.glDrawLine(x+paddingX, y, x+paddingX, y+height-1);
			r.glDrawLine(x+paddingX+width-1, y, x+paddingX+width-1, y+height-1);
			r.glDrawLine(x+paddingX, y+height-1, x+paddingX+width-1, y+height-1);
		} else {
			for (Block b : content) {
				caret.skip(1);
				b.draw(ge, r, x+paddingX, y+line-b.line, caret);
				paddingX += b.getWidth();
				if (caret.getRemaining() == 0) BlockContainer.drawCaret(ge, r, caret, x + paddingX, y+line-b.line, b.height);
				paddingX += 1;
			}
		}
		caret.skip(1);
	}

	public boolean putBlock(Caret caret, Block newBlock) {
		boolean added = false;

		if (caret.getRemaining() == 0) {
			this.addBlock(0, newBlock);
			added = true;
		}
		
		int pos = 0;
		for (Block b : content) {
			caret.skip(1);
			pos++;
			added = added|b.putBlock(caret, newBlock);
			if (caret.getRemaining() == 0) {
				this.addBlock(pos, newBlock);
				added = true;
			}
		}
		caret.skip(1);
		if (added) {
			recomputeDimensions();
		}
		return added;
	}

	public boolean delBlock(Caret caret) {
		boolean removed = false;
		
		int pos = 0;
		for (Block b : content) {
			caret.skip(1);
			pos++;
			int deltaCaret = caret.getRemaining();
			removed = removed|b.delBlock(caret);
			if (caret.getRemaining() == 0 || (removed == false && deltaCaret >= 0 && caret.getRemaining() < 0)) {
				this.removeAt(pos-1);
				caret.setPosition(caret.getPosition()-deltaCaret);
				removed = true;
			}
		}
		caret.skip(1);
		if (removed) {
			recomputeDimensions();
		}
		return removed;
	}


	@Override
	public void recomputeDimensions() {
		int l = 0; //Line
		int w = 0; //Width
		int h2 = 0; //Height under the line. h = h2 + l
		int h = 0; //Height
		
		for (Block b : content) {
			w += b.getWidth() + 1;
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
		
		if (content.size() > 0) {
			w -= 1;
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
			line = height/2;
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
		initialized = true;
	}
	
	public static BinaryFont getDefaultFont(boolean small) {
		checkInitialized();
		return defFonts[small?1:0];
	}

	public static int getDefaultColor() {
		return defColor;
	}

	public static int getDefaultCharWidth(boolean b) {
		checkInitialized();
		return defFontSizes[b?2:0];
	}
	
	public static int getDefaultCharHeight(boolean b) {
		checkInitialized();
		return defFontSizes[b?3:1];
	}
	
	public static void drawCaret(GraphicEngine ge, Renderer r, Caret caret, int x, int y, int height) {
		if (caret.getState() == CaretState.VISIBLE_ON) {
			r.glColor(getDefaultColor());
			r.glDrawLine(x, y, x, y-1+height);
			r.glDrawLine(x+1, y, x+1, y-1+height);
			r.glDrawLine(x+2, y, x+2, y-1+height);
		}
	}

	public void setSmall(boolean small) {
		this.small = small;
		recomputeDimensions();
	}

	public ObjectArrayList<Block> getContent() {
		return content.clone();
	}
	
	private static void checkInitialized() {
		if (!initialized) throw new ExceptionInInitializerError("Please initialize BlockContainer by running the method BlockContainer.initialize(...) first!");
	}
	
}
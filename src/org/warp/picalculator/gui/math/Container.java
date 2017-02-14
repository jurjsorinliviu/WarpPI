package org.warp.picalculator.gui.math;

import java.util.ArrayList;

import org.warp.picalculator.device.graphicengine.Display;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;

public class Container implements GraphicalElement {

	private final int minWidth;
	private final int minHeight;
	private final ArrayList<Block> content;
	private int width;
	private int height;
	private int line;
	
	public Container(int minWidth, int minHeight) {
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		this.content = new ArrayList<>();
	}
	
	public Container(int minWidth, int minHeight, ArrayList<Block> content) {
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		this.content = content;
	}
	
	public void addBlock(Block b) {
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
	 * @param x Position relative to the window.
	 * @param y Position relative to the window.
	 * @param small size of the element.
	 * @param caretPos remaining positions of the caret.
	 * @return <code>caretPos - currentElementLength</code>
	 */
	public int draw(GraphicEngine g, int x, int y, boolean small, int caretPos) {
		Renderer r = g.getRenderer();
		return caretPos;
		
	}
	
	@Override
	public void recomputeDimensions() {
		int w = 0;
		int h = 0;
		int l = 0;
		
		for (Block b : content) {
			w += b.getWidth();
		}
		
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
		line = l;
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

	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
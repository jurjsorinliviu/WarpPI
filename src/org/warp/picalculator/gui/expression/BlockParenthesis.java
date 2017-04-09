package org.warp.picalculator.gui.expression;

import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;

public class BlockParenthesis extends Block {
	
	public static final int CLASS_ID = 0x00000004;

	private final BlockContainer containerNumber;

	public BlockParenthesis() {
		this.containerNumber = new BlockContainer(false);
		recomputeDimensions();
	}
	
	@Override
	public void draw(GraphicEngine ge, Renderer r, int x, int y, Caret caret) {
		BlockContainer.getDefaultFont(small).use(ge);
		r.glColor(BlockContainer.getDefaultColor());
		containerNumber.draw(ge, r, x+7, y+3, caret);
	}

	@Override
	public boolean putBlock(Caret caret, Block newBlock) {
		boolean added = false;
		added = added|containerNumber.putBlock(caret, newBlock);
		if (added) {
			recomputeDimensions();
		}
		return added;
	}

	@Override
	public boolean delBlock(Caret caret) {
		boolean removed = false;
		removed = removed|containerNumber.delBlock(caret);
		if (removed) {
			recomputeDimensions();
		}
		return removed;
	}

	@Override
	public void recomputeDimensions() {
		this.width = containerNumber.getWidth()+BlockContainer.getDefaultCharWidth(small)*2;
		this.height = containerNumber.getHeight();
		this.line = containerNumber.getLine();
	}

	@Override
	public void setSmall(boolean small) {
		this.small = small;
		this.containerNumber.setSmall(small);
		recomputeDimensions();
	}

	public BlockContainer getNumberContainer() {
		return containerNumber;
	}

	@Override
	public int getClassID() {
		return CLASS_ID;
	}

	@Override
	public int computeCaretMaxBound() {
		return containerNumber.computeCaretMaxBound();
	}

}

package org.warp.picalculator.gui.expression.blocks;

import org.warp.picalculator.gui.expression.Caret;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.gui.expression.blocks.BlockContainer;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;

public class BlockPower extends Block {

	public static final int CLASS_ID = 0x00000005;

	private final BlockContainer containerNumber;
	private final BlockContainer containerExponent;

	private int h1;
	private int w1;

	public BlockPower() {
		containerNumber = new BlockContainer(false);
		containerExponent = new BlockContainer(true);
		recomputeDimensions();
	}

	@Override
	public void draw(GraphicEngine ge, Renderer r, int x, int y, Caret caret) {
		BlockContainer.getDefaultFont(small).use(ge);
		r.glColor(BlockContainer.getDefaultColor());
		containerNumber.draw(ge, r, x, y+height-h1, caret);
		BlockContainer.getDefaultFont(true).use(ge);
		containerExponent.draw(ge, r, x+w1+getSpacing(), y, caret);
	}

	@Override
	public boolean putBlock(Caret caret, Block newBlock) {
		boolean added = false;
		added = added | containerNumber.putBlock(caret, newBlock);
		added = added | containerExponent.putBlock(caret, newBlock);
		if (added) {
			recomputeDimensions();
		}
		return added;
	}

	@Override
	public boolean delBlock(Caret caret) {
		boolean removed = false;
		removed = removed | containerNumber.delBlock(caret);
		removed = removed | containerExponent.delBlock(caret);
		if (removed) {
			recomputeDimensions();
		}
		return removed;
	}

	@Override
	public void recomputeDimensions() {
		w1 = containerNumber.getWidth();
		final int w2 = containerExponent.getWidth();
		h1 = containerNumber.getHeight();
		final int h2 = containerExponent.getHeight();
		final int l1 = containerNumber.getLine();
		width = w1+getSpacing()+1+w2;
		height = h1 + h2 - 3;
		line = height-h1+l1;
	}

	@Override
	public void setSmall(boolean small) {
		this.small = small;
		containerNumber.setSmall(small);
		containerExponent.setSmall(true);
		recomputeDimensions();
	}

	public BlockContainer getNumberContainer() {
		return containerNumber;
	}

	public BlockContainer getExponentContainer() {
		return containerExponent;
	}

	@Override
	public int getClassID() {
		return CLASS_ID;
	}

	@Override
	public int computeCaretMaxBound() {
		return containerNumber.computeCaretMaxBound() + containerExponent.computeCaretMaxBound();
	}
	
	protected int getSpacing() {
		return 1;
	}
}

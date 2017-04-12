package org.warp.picalculator.gui.expression.blocks;

import org.warp.picalculator.gui.expression.Caret;
import org.warp.picalculator.gui.graphicengine.BinaryFont;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;

public class BlockParenthesis extends Block {

	public static final int CLASS_ID = 0x00000004;

	private final BlockContainer containerNumber;

	private int chw;
	private int chh;

	public BlockParenthesis() {
		containerNumber = new BlockContainer(false);
		recomputeDimensions();
	}

	@Override
	public void draw(GraphicEngine ge, Renderer r, int x, int y, Caret caret) {
		BlockContainer.getDefaultFont(small).use(ge);
		r.glColor(BlockContainer.getDefaultColor());
		r.glDrawCharLeft(x, y, '╭');
		r.glDrawCharLeft(x, y+height-chh, '╰');
		r.glFillColor(x+3, y+6, 2, height-6*2);
		r.glFillColor(x+width-5, y+6, 2, height-6*2);
		r.glDrawCharLeft(x+width-chw, y, '╮');
		r.glDrawCharLeft(x+width-chw, y+height-chh, '╯');
		containerNumber.draw(ge, r, x+chw, y, caret);
	}

	@Override
	public boolean putBlock(Caret caret, Block newBlock) {
		boolean added = false;
		added = added | containerNumber.putBlock(caret, newBlock);
		if (added) {
			recomputeDimensions();
		}
		return added;
	}

	@Override
	public boolean delBlock(Caret caret) {
		boolean removed = false;
		removed = removed | containerNumber.delBlock(caret);
		if (removed) {
			recomputeDimensions();
		}
		return removed;
	}

	@Override
	public void recomputeDimensions() {
		chw = BlockContainer.getDefaultCharWidth(small);
		chh = BlockContainer.getDefaultCharHeight(small);
		width = containerNumber.getWidth() + chw * 2 + 3;
		height = containerNumber.getHeight();
		line = containerNumber.getLine();
	}

	@Override
	public void setSmall(boolean small) {
		this.small = small;
		containerNumber.setSmall(small);
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

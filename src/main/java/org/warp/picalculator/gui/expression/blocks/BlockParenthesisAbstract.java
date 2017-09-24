package org.warp.picalculator.gui.expression.blocks;

import org.warp.picalculator.Error;
import org.warp.picalculator.gui.expression.Caret;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.parser.features.interfaces.Feature;

public abstract class BlockParenthesisAbstract extends Block {

	private final BlockContainer containerNumber;

	private final String prefix;
	private int prw;
	private int chw;
	private int chh;

	protected BlockParenthesisAbstract(String prefix) {
		containerNumber = new BlockContainer(false);
		this.prefix = prefix;

		recomputeDimensions();
	}

	public BlockParenthesisAbstract() {
		containerNumber = new BlockContainer(false);
		this.prefix = null;
		recomputeDimensions();
	}

	@Override
	public void draw(GraphicEngine ge, Renderer r, int x, int y, Caret caret) {
		BlockContainer.getDefaultFont(small).use(ge);
		r.glColor(BlockContainer.getDefaultColor());
		if (prefix != null) {
			r.glDrawStringLeft(x + 1, y + line - chh / 2, prefix);
		}
		r.glDrawCharLeft(x + prw, y, '╭');
		r.glDrawCharLeft(x + prw, y + height - chh, '╰');
		r.glFillColor(x + prw + 3, y + 6, 2, height - 6 * 2);
		r.glFillColor(x + width - 5, y + 6, 2, height - 6 * 2);
		r.glDrawCharLeft(x + width - chw, y, '╮');
		r.glDrawCharLeft(x + width - chw, y + height - chh, '╯');
		containerNumber.draw(ge, r, x + prw + chw, y, caret);
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
	public Block getBlock(Caret caret) {
		return containerNumber.getBlock(caret);
	}

	@Override
	public void recomputeDimensions() {
		if (prefix == null) {
			prw = 0;
		} else {
			prw = 1 + BlockContainer.getDefaultCharWidth(small) * prefix.length() + 2;
		}
		chw = BlockContainer.getDefaultCharWidth(small);
		chh = BlockContainer.getDefaultCharHeight(small);
		width = prw + chw + containerNumber.getWidth() + chw + 3;
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
	public int computeCaretMaxBound() {
		return containerNumber.computeCaretMaxBound();
	}

	public abstract Feature toFeature(MathContext context) throws Error;

}

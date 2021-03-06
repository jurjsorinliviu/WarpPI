package org.warp.picalculator.gui.expression.blocks;

import org.warp.picalculator.Error;
import org.warp.picalculator.gui.expression.Caret;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.parser.features.FeatureSquareRoot;
import org.warp.picalculator.math.parser.features.interfaces.Feature;

public class BlockSquareRoot extends Block {

	private final BlockContainer containerNumber;

	private int h1;

	public BlockSquareRoot() {
		containerNumber = new BlockContainer(false);
		recomputeDimensions();
	}

	@Override
	public void draw(GraphicEngine ge, Renderer r, int x, int y, Caret caret) {
		BlockContainer.getDefaultFont(small).use(ge);
		r.glColor(BlockContainer.getDefaultColor());
		r.glDrawLine(x, y + height - 10 + 1, x, y + height - 10 + 2); // /
		r.glDrawLine(x + 1, y + height - 10, x + 1, y + height - 10 + 1); // /
		r.glDrawLine(x + 2, y + height - 10 + 2, x + 2, y + height - 10 + 6); // \
		r.glDrawLine(x + 3, y + height - 10 + 7, x + 3, y + height - 10 + 9); // \
		r.glDrawLine(x + 5, y + height - h1 - 1 - 2, x + width - 1, y + height - h1 - 1 - 2); // ----
		r.glDrawLine(x + 5, y + height - h1 - 1 - 2, x + 5, y + height - (h1 - 2) / 3f * 2f - 1); // |
		r.glDrawLine(x + 4, y + height - (h1 - 2) / 3f * 2f - 1, x + 4, y + height - (h1 - 2) / 3f - 1); // |
		r.glDrawLine(x + 3, y + height - (h1 - 2) / 3f - 1, x + 3, y + height - 1); // |
		containerNumber.draw(ge, r, x + 7, y + 3, caret);
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
	public BlockReference<?> getBlock(Caret caret) {
		return containerNumber.getBlock(caret);
	}

	@Override
	public void recomputeDimensions() {
		final int w1 = containerNumber.getWidth();
		h1 = containerNumber.getHeight();
		final int l1 = containerNumber.getLine();
		width = 8 + w1 + 2;
		height = 3 + h1;
		line = 3 + l1;
		if (height < 9) {
			height = 9;
			line += (9 - (3 + h1));
		}
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

	@Override
	public Feature toFeature(MathContext context) throws Error {
		final Function contnt = getNumberContainer().toFunction(context);
		return new FeatureSquareRoot(contnt);
	}
}

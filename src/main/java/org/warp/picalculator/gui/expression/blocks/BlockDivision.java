package org.warp.picalculator.gui.expression.blocks;

import org.warp.picalculator.Error;
import org.warp.picalculator.gui.expression.Caret;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.parser.features.FeatureDivision;
import org.warp.picalculator.math.parser.features.interfaces.Feature;

public class BlockDivision extends Block {

	private final BlockContainer containerUp;
	private final BlockContainer containerDown;

	private int paddingLeftUpper;
	private int paddingLeftLower;
	private int h1;

	public BlockDivision() {
		containerUp = new BlockContainer(false);
		containerDown = new BlockContainer(false);
		recomputeDimensions();
	}

	@Override
	public void draw(GraphicEngine ge, Renderer r, int x, int y, Caret caret) {
		BlockContainer.getDefaultFont(small).use(ge);
		r.glColor(BlockContainer.getDefaultColor());
		containerUp.draw(ge, r, x + 1 + paddingLeftUpper, y, caret);
		r.glColor(BlockContainer.getDefaultColor());
		r.glDrawLine(x, y + h1 + 1, x + width - 1, y + h1 + 1);
		containerDown.draw(ge, r, x + 1 + paddingLeftLower, y + h1 + 3, caret);
	}

	@Override
	public boolean putBlock(Caret caret, Block newBlock) {
		boolean added = false;
		added = added | containerUp.putBlock(caret, newBlock);
		added = added | containerDown.putBlock(caret, newBlock);
		if (added) {
			recomputeDimensions();
		}
		return added;
	}

	@Override
	public boolean delBlock(Caret caret) {
		boolean removed = false;
		removed = removed | containerUp.delBlock(caret);
		removed = removed | containerDown.delBlock(caret);
		if (removed) {
			recomputeDimensions();
		}
		return removed;
	}

	@Override
	public Block getBlock(Caret caret) {
		Block bl = null;
		bl = containerUp.getBlock(caret);
		if (bl != null) {
			return bl;
		}
		bl = containerDown.getBlock(caret);
		return bl;
	}

	@Override
	public void recomputeDimensions() {
		final int w1 = containerUp.getWidth();
		final int w2 = containerDown.getWidth();
		final int h1 = containerUp.getHeight();
		final int h2 = containerDown.getHeight();
		width = (w1 > w2 ? w1 : w2) + 4;
		height = h1 + 3 + h2;
		line = h1 + 1;
		this.h1 = h1;
		if (w1 != w2) {
			if (w1 > w2) {
				paddingLeftUpper = 0;
				paddingLeftLower = (w1 - w2) / 2;
			} else {
				paddingLeftUpper = (w2 - w1) / 2;
				paddingLeftLower = 0;
			}
		} else {
			paddingLeftUpper = 0;
			paddingLeftLower = 0;
		}
	}

	@Override
	public void setSmall(boolean small) {
		this.small = small;
		containerUp.setSmall(small);
		containerDown.setSmall(small);
		recomputeDimensions();
	}

	public BlockContainer getUpperContainer() {
		return containerUp;
	}

	public BlockContainer getLowerContainer() {
		return containerDown;
	}

	@Override
	public int computeCaretMaxBound() {
		return containerUp.computeCaretMaxBound() + containerDown.computeCaretMaxBound();
	}

	@Override
	public Feature toFeature(MathContext context) throws Error {
		final Function upper = getUpperContainer().toFunction(context);
		final Function lower = getLowerContainer().toFunction(context);
		return new FeatureDivision(upper, lower);
	}
}

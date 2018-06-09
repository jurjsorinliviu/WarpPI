package org.warp.picalculator.gui.expression.blocks;

import org.warp.picalculator.Error;
import org.warp.picalculator.gui.expression.Caret;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.parser.features.FeaturePowerChar;
import org.warp.picalculator.math.parser.features.interfaces.Feature;

public class BlockPower extends Block {

	private final BlockContainer containerExponent;

	public BlockPower() {
		containerExponent = new BlockContainer(true);
		recomputeDimensions();
	}

	@Override
	public void draw(GraphicEngine ge, Renderer r, int x, int y, Caret caret) {
		BlockContainer.getDefaultFont(true).use(ge);
		r.glColor(BlockContainer.getDefaultColor());
		containerExponent.draw(ge, r, x, y, caret);
	}

	@Override
	public boolean putBlock(Caret caret, Block newBlock) {
		boolean added = false;
		added = added | containerExponent.putBlock(caret, newBlock);
		if (added) {
			recomputeDimensions();
		}
		return added;
	}

	@Override
	public boolean delBlock(Caret caret) {
		boolean removed = false;
		removed = removed | containerExponent.delBlock(caret);
		if (removed) {
			recomputeDimensions();
		}
		return removed;
	}

	@Override
	public BlockReference<?> getBlock(Caret caret) {
		return containerExponent.getBlock(caret);
	}

	@Override
	public void recomputeDimensions() {
		final int w2 = containerExponent.getWidth();
		final int h2 = containerExponent.getHeight();
		width = w2 + 1;
		height = h2 + BlockContainer.getDefaultCharHeight(small) - 3;
		line = h2 + BlockContainer.getDefaultCharHeight(small) / 2 - 3;
	}

	@Override
	public void setSmall(boolean small) {
		this.small = small;
		containerExponent.setSmall(true);
		recomputeDimensions();
	}

	public BlockContainer getExponentContainer() {
		return containerExponent;
	}

	@Override
	public int computeCaretMaxBound() {
		return containerExponent.computeCaretMaxBound();
	}

	@Override
	public Feature toFeature(MathContext context) throws Error {
		final Function exp = getExponentContainer().toFunction(context);
		return new FeaturePowerChar(exp);
	}
}

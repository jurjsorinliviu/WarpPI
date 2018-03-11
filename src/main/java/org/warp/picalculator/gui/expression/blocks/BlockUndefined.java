package org.warp.picalculator.gui.expression.blocks;

import org.warp.picalculator.gui.expression.Caret;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.parser.features.FeatureChar;
import org.warp.picalculator.math.parser.features.interfaces.Feature;

public class BlockUndefined extends Block {

	public BlockUndefined() {
		recomputeDimensions();
	}

	@Override
	public void draw(GraphicEngine ge, Renderer r, int x, int y, Caret caret) {
		BlockContainer.getDefaultFont(small).use(ge);
		r.glColor(BlockContainer.getDefaultColor());
		r.glDrawStringLeft(x, y, "UNDEFINED");
	}

	@Override
	public boolean putBlock(Caret caret, Block newBlock) {
		return false;
	}

	@Override
	public boolean delBlock(Caret caret) {
		return false;
	}

	@Override
	public BlockReference<?> getBlock(Caret caret) {
		return null;
	}

	@Override
	public void recomputeDimensions() {
		width = BlockContainer.getDefaultFont(small).getStringWidth("UNDEFINED");
		height = BlockContainer.getDefaultCharHeight(small);
		line = height / 2;
	}

	@Override
	public void setSmall(boolean small) {
		this.small = small;
		recomputeDimensions();
	}

	@Override
	public int computeCaretMaxBound() {
		return 0;
	}

	@Override
	public Feature toFeature(MathContext context) {
		return new FeatureChar(MathematicalSymbols.UNDEFINED);
	}

}

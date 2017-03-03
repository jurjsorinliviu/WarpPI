package org.warp.picalculator.gui.expression;

import org.warp.picalculator.Main;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.math.parser.features.interfaces.Feature;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class BlockDivision extends Block {

	private final BlockContainer containerUp;
	private final BlockContainer containerDown;

	private int paddingLeftUpper;
	private int paddingLeftLower;
	private int h1;
	
	public BlockDivision() {
		this.containerUp = new BlockContainer(false);
		this.containerDown = new BlockContainer(false);
		recomputeDimensions();
	}

	@Override
	public void draw(GraphicEngine ge, Renderer r, int x, int y, Caret caret) {
		BlockContainer.getDefaultFont(small).use(ge);
		r.glColor(BlockContainer.getDefaultColor());
		containerUp.draw(ge, r, x+1+paddingLeftUpper, y, caret);
		r.glDrawLine(x, y+h1+1, x+width, y+h1+1);
		containerDown.draw(ge, r, x+1+paddingLeftLower, y + h1+3, caret);
	}

	@Override
	public void recomputeDimensions() {
		final int w1 = containerUp.getWidth();
		final int w2 = containerDown.getWidth();
		final int h1 = containerUp.getHeight();
		final int h2 = containerDown.getHeight();
		width = (w1>w2?w1:w2) + 2;
		height = h1+3+h2;
		line = h1+1;
		this.h1 = h1;
		if (w1 != w2) {
			if (w1 > w2) {
				paddingLeftUpper = 0;
				paddingLeftLower = (w1 - w2) / 2;
			} else {
				paddingLeftUpper = (w2 - w1) / 2;
				paddingLeftLower = 0;
			}
		}
	}

	@Override
	public void setSmall(boolean small) {
		this.small = small;
		this.containerUp.setSmall(small);
		this.containerDown.setSmall(small);
		recomputeDimensions();
	}

	public BlockContainer getUpperContainer() {
		return containerUp;
	}
	
	public BlockContainer getLowerContainer() {
		return containerDown;
	}
}

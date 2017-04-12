package org.warp.picalculator.gui.expression.layouts;

import org.warp.picalculator.gui.expression.blocks.Block;

public interface InputLayout {
	public Block parseChar(char c);
}

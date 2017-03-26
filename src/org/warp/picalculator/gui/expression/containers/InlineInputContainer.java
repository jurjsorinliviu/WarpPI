package org.warp.picalculator.gui.expression.containers;

import org.warp.picalculator.gui.expression.Block;
import org.warp.picalculator.gui.expression.BlockChar;
import org.warp.picalculator.math.MathematicalSymbols;

public class InlineInputContainer extends InputContainer {
	@Override
	public Block parseChar(char c) {
		return new BlockChar(MathematicalSymbols.DIVISION);
	}
}

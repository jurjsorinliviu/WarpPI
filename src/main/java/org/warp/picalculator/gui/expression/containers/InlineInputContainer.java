package org.warp.picalculator.gui.expression.containers;

import org.warp.picalculator.gui.expression.InputContext;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.gui.expression.blocks.BlockChar;
import org.warp.picalculator.math.MathematicalSymbols;

public class InlineInputContainer extends InputContainer {

	private static final long serialVersionUID = 4307434049083324966L;

	@Deprecated()
	/**
	 * Use InlineInputContainer(InputContext) instead
	 */
	public InlineInputContainer() {
		super();
	}
	
	public InlineInputContainer(InputContext ic) {
		super(ic);
	}

	public InlineInputContainer(InputContext ic, boolean small) {
		super(ic, small);
	}

	public InlineInputContainer(InputContext ic, boolean small, int minWidth, int minHeight) {
		super(ic, small, minWidth, minHeight);
	}

	@Override
	public Block parseChar(char c) {
		return new BlockChar(c);
	}
}

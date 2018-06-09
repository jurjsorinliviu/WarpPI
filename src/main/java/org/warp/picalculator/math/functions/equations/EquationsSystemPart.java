package org.warp.picalculator.math.functions.equations;

import org.warp.picalculator.Error;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathContext;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class EquationsSystemPart extends FunctionSingle {

	public EquationsSystemPart(MathContext root, Equation equazione) {
		super(root, equazione);
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EquationsSystemPart clone() {
		return new EquationsSystemPart(mathContext, (Equation) parameter);
	}

	@Override
	public ObjectArrayList<Block> toBlock(MathContext context) throws Error {
		// TODO Auto-generated method stub
		return null;
	}

}

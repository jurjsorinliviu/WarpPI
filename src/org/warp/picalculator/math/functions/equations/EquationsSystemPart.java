package org.warp.picalculator.math.functions.equations;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathematicalSymbols;

public class EquationsSystemPart extends FunctionSingle {

	public EquationsSystemPart(MathContext root, Equation equazione) {
		super(root, equazione);
	}

	@Override
	protected ObjectArrayList<Function> solve() throws Error {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isSolvable() {
		// TODO Auto-generated method stub
		return false;
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
	
}

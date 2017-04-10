package org.warp.picalculator.math.functions.trigonometry;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionSingle;

public class Tangent extends FunctionSingle {

	public Tangent(MathContext root, Function value) {
		super(root, value);
	}

	@Override
	public ObjectArrayList<Function> solve() throws Error {
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
	public FunctionSingle clone() {
		// TODO Auto-generated method stub
		return null;
	}

}

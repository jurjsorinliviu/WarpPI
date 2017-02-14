package org.warp.picalculator.math.functions.trigonometry;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathematicalSymbols;

public class ArcTangent extends FunctionSingle {

	public ArcTangent(MathContext root, Function value) {
		super(root, value);
	}

	@Override
	public Function NewInstance(MathContext root, Function value) {
		return new ArcTangent(root, value);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.getGraphicRepresentation(MathematicalSymbols.ARC_TANGENT);
	}

	@Override
	public ArrayList<Function> solve() throws Error {
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

}

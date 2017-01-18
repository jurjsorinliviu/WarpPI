package org.warp.picalculator.math.functions.trigonometry;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Calculator;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.functions.AnteriorFunction;
import org.warp.picalculator.math.functions.Function;

public class ArcCosine extends AnteriorFunction {
	
	public ArcCosine(Calculator root, Function value) {
		super(root, value);
	}
	
	@Override
	public Function NewInstance(Calculator root, Function value) {
		return new ArcCosine(root, value);
	}
	
	@Override
	public String getSymbol() {
		return MathematicalSymbols.getGraphicRepresentation(MathematicalSymbols.ARC_COSINE);
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

package org.warp.picalculator.math.functions.trigonometry;

import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathematicalSymbols;
import org.warp.picalculator.math.functions.AnteriorFunction;
import org.warp.picalculator.math.functions.Function;

public class ArcSine extends AnteriorFunction {
	
	public ArcSine(Function parent, Function value) {
		super(parent, value);
	}
	
	@Override
	public Function NewInstance(Function parent, Function value) {
		return new ArcSine(parent, value);
	}
	
	@Override
	public String getSymbol() {
		return MathematicalSymbols.getGraphicRepresentation(MathematicalSymbols.ARC_SINE);
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

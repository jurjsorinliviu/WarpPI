package org.warp.picalculator.math.parser.features;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.RootSquare;

public class FeatureSquareRoot extends FeatureSingleImpl {

	public FeatureSquareRoot(Object child) {
		super(child);
	}

	@Override
	public RootSquare toFunction(MathContext context) throws Error {
		return new RootSquare(context, getFunction1());
	}

}

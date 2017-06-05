package org.warp.picalculator.math.parser.features;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Sum;

public class FeatureSum extends FeatureDoubleImpl {

	public FeatureSum(Object child1, Object child2) {
		super(child1, child2);
	}

	@Override
	public Sum toFunction(MathContext context) throws Error {
		return new Sum(context, getFunction1(), getFunction2());
	}

}

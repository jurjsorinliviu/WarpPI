package org.warp.picalculator.math.parser.features;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Power;

public class FeaturePower extends FeatureDoubleImpl {

	public FeaturePower(Object child1, Object child2) {
		super(child1, child2);
	}

	@Override
	public Power toFunction(MathContext context) throws Error {
		return new Power(context, getFunction1(), getFunction2());
	}

}

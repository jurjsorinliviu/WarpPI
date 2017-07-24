package org.warp.picalculator.math.parser.features;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.SumSubtraction;
import org.warp.picalculator.math.parser.features.interfaces.Feature;

public class FeatureSumSubtraction extends FeatureDoubleImpl {

	public FeatureSumSubtraction(Object child1, Object child2) {
		super(child1, child2);
	}

	@Override
	public SumSubtraction toFunction(MathContext context) throws Error {
		return new SumSubtraction(context, getFunction1(), getFunction2());
	}

}

package org.warp.picalculator.math.parser.features;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Logarithm;

public class FeatureLogarithm extends FeatureDoubleImpl {

	public FeatureLogarithm(Object child1, Object child2) {
		super(child1, child2);
	}

	@Override
	public Logarithm toFunction(MathContext context) throws Error {
		return new Logarithm(context, getFunction1(), getFunction2());
	}

}

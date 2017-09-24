package org.warp.picalculator.math.parser.features;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Subtraction;

public class FeatureSubtraction extends FeatureDoubleImpl {

	public FeatureSubtraction(Object child1, Object child2) {
		super(child1, child2);
	}

	@Override
	public Function toFunction(MathContext context) throws Error {
		return new Subtraction(context, getFunction1(), getFunction2());
	}

}

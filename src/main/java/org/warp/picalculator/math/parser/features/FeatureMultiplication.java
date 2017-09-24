package org.warp.picalculator.math.parser.features;

import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Multiplication;

public class FeatureMultiplication extends FeatureDoubleImpl {

	public FeatureMultiplication(Object child1, Object child2) {
		super(child1, child2);
	}

	@Override
	public Multiplication toFunction(MathContext context) {
		return new Multiplication(context, getFunction1(), getFunction2());
	}

}

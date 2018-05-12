package org.warp.picalculator.math.parser.features;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Expression;

public class FeatureParenthesis extends FeatureSingleImpl {

	public FeatureParenthesis(Object child) {
		super(child);
	}

	@Override
	public Function toFunction(MathContext context) throws Error {
		return new Expression(context, getFunction1());
	}

}

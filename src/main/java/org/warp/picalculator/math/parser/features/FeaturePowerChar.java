package org.warp.picalculator.math.parser.features;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;

public class FeaturePowerChar extends FeatureSingleImpl {

	public FeaturePowerChar(Object child) {
		super(child);
	}

	@Override
	public Function toFunction(MathContext context) throws Error {
		return null;
	}

}

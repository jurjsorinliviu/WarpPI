package org.warp.picalculator.math.parser.features.interfaces;

import org.warp.picalculator.Error;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;

public abstract interface Feature {

	public Function toFunction(MathContext context) throws Error;

}

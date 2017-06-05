package org.warp.picalculator.math.parser.features;

import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.parser.features.interfaces.Feature;

public class FeatureChar implements Feature {

	public final char ch;

	public FeatureChar(char ch) {
		this.ch = ch;
	}

	@Override
	public Function toFunction(MathContext context) {
		return null;
	}

}

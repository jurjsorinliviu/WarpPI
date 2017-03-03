package org.warp.picalculator.math.parser.features;

import org.warp.picalculator.math.parser.features.interfaces.Feature;

public class FeatureChar implements Feature {

	public final char ch;
	
	public FeatureChar(char ch) {
		this.ch = ch;
	}

}
